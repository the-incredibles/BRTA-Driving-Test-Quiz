package net.incredibles.brtaprovider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.cryptic.dbutil.exporterimporter.SQLiteDbImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author sharafat
 * @Created 2/15/12 12:05 PM
 */
class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "traffic_sign.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SIGN_IMAGE_NAME_PREFIX = "sign_";

    private static final Logger LOG = LoggerFactory.getLogger(DbHelper.class);

    private Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            SQLiteDbImporter.importDb(context.getResources().openRawResource(R.raw.db), db, true);
            updateSignTableWithSignImages(db);
        } catch (IOException e) {
            LOG.error("Error creating database.", e);
        }
    }

    private void updateSignTableWithSignImages(SQLiteDatabase db) {
        Cursor cursor = db.query(BrtaSignsContract.Sign.TABLE_NAME,
                new String[]{BrtaSignsContract.Sign._ID}, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int signId = cursor.getInt(0);
                int signImageResId = context.getResources().getIdentifier(
                        "drawable/" + SIGN_IMAGE_NAME_PREFIX + signId, null, context.getPackageName());

                if (signImageResId != 0) {
                    updateSignImage(db, signId, signImageResId);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    private void updateSignImage(SQLiteDatabase db, int signId, int signImageResId) {
        byte[] signImage = drawable2ByteArray(context.getResources().getDrawable(signImageResId));

        db.execSQL("UPDATE " + BrtaSignsContract.Sign.TABLE_NAME
                + " SET " + BrtaSignsContract.Sign.COL_IMAGE + " = ?"
                + " WHERE " + BrtaSignsContract.Sign._ID + " = ?", new Object[]{signImage, signId});
    }

    private byte[] drawable2ByteArray(Drawable drawable) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        return out.toByteArray();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
