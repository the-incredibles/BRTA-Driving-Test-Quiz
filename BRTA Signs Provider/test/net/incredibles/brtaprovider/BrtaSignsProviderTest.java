package net.incredibles.brtaprovider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.cryptic.dbutil.exporterimporter.SQLiteDbImporter;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author sharafat
 * @Created 2/15/12 12:05 PM
 */
@RunWith(RobolectricTestRunner.class)
public class BrtaSignsProviderTest {
    private static final Uri SIGN_SET_URI = BrtaSignsContract.SignSet.CONTENT_URI;
    private static final Uri SIGN_URI = BrtaSignsContract.Sign.CONTENT_URI;

    private BrtaSignsProvider brtaSignsProvider;
    private Cursor cursor;

    @Before
    public void setup() {
        brtaSignsProvider = new BrtaSignsProvider();
        brtaSignsProvider.onCreate();
        brtaSignsProvider.setSQLiteOpenHelper(new SQLiteOpenHelper(null, "test.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                try {
                    SQLiteDbImporter.importDb(new FileInputStream("res/raw/db.sql"), db);
                } catch (IOException e) {
                    fail(e.getMessage());
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        });
        brtaSignsProvider.setQueryWithLoggingEnabledCursorFactory(false);
    }

    @After
    public void tearDown() {
        if (cursor != null) {
            cursor.close();
        }
    }

    @Test
    public void testQuery_SignSetTable() {
        cursor = brtaSignsProvider.query(SIGN_SET_URI, null, null, null, null);
        assertThat(cursor.getCount(), equalTo(5));
    }

    @Test
    public void testQuery_SignTable() {
        cursor = brtaSignsProvider.query(SIGN_URI, null, null, null, null);
        assertThat(cursor.getCount(), equalTo(71));
    }

    @Test
    public void testQuery_SignTable_SingleRow() {
        cursor = brtaSignsProvider.query(getUriForFirstRowInSignTable(), null, null, null, null);
        assertThat(cursor.getCount(), equalTo(1));
    }

    @Test
    public void testInsert() {
        assertThat(brtaSignsProvider.insert(null, null), nullValue());
    }

    @Test
    public void testUpdate() {
        assertThat(brtaSignsProvider.update(null, null, null, null), equalTo(0));
    }

    @Test
    public void testDelete() {
        assertThat(brtaSignsProvider.delete(null, null, null), equalTo(0));
    }

    @Test
    public void testGetType() {
        assertThat(brtaSignsProvider.getType(SIGN_SET_URI), equalTo(BrtaSignsContract.SignSet.CONTENT_TYPE));
        assertThat(brtaSignsProvider.getType(SIGN_URI), equalTo(BrtaSignsContract.Sign.CONTENT_TYPE));
        assertThat(brtaSignsProvider.getType(getUriForFirstRowInSignTable()),
                equalTo(BrtaSignsContract.Sign.CONTENT_TYPE_SINGLE_ROW));
    }

    private Uri getUriForFirstRowInSignTable() {
        return Uri.parse(SIGN_URI.toString() + "/1");
    }
}
