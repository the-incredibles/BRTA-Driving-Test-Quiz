package net.incredibles.brtaprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.cryptic.dbutil.cursorfactory.QueryLoggingEnabledCursorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sharafat
 * @Created 2/11/12 2:43 PM
 */
public class BrtaSignsProvider extends ContentProvider {
    private static final Logger LOG = LoggerFactory.getLogger(BrtaSignsProvider.class);

    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int SIGN_SET_TABLE = 1;
    private static final int SIGN_TABLE = 2;
    private static final int SIGN_TABLE_SINGLE_ROW = 3;

    private SQLiteOpenHelper db;
    private boolean queryWithLoggingEnabledCursorFactory;

    static {
        matcher.addURI(BrtaSignsContract.AUTHORITY, BrtaSignsContract.SignSet.TABLE_NAME, SIGN_SET_TABLE);
        matcher.addURI(BrtaSignsContract.AUTHORITY, BrtaSignsContract.Sign.TABLE_NAME, SIGN_TABLE);
        matcher.addURI(BrtaSignsContract.AUTHORITY, BrtaSignsContract.Sign.TABLE_NAME + "/#", SIGN_TABLE_SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        db = new DbHelper(getContext());
        queryWithLoggingEnabledCursorFactory = true;
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);

        throwExceptionIfUriIsInvalid(uri, table);

        selection = getSelectionWithIdSelectionAppendedIfSignTableSingleRow(uri, selection);
        try {
            Cursor cursor;
            if (queryWithLoggingEnabledCursorFactory) {
                cursor = db.getReadableDatabase().queryWithFactory(new QueryLoggingEnabledCursorFactory(), false,
                        table, projection, selection, selectionArgs, null, null, sortOrder, null);
            } else {
                cursor = db.getReadableDatabase().query(table, projection, selection, selectionArgs, null, null, sortOrder);
            }

            setNotificationUri(uri, cursor);

            return cursor;
        } catch (SQLiteException e) {
            LOG.debug("Query Exception", e);
            return null;
        }
    }

    private String getTableName(Uri uri) {
        switch (matcher.match(uri)) {
            case SIGN_SET_TABLE:
                return BrtaSignsContract.SignSet.TABLE_NAME;
            case SIGN_TABLE:
                // fall-through
            case SIGN_TABLE_SINGLE_ROW:
                return BrtaSignsContract.Sign.TABLE_NAME;
            default:
                return null;
        }
    }

    private void throwExceptionIfUriIsInvalid(Uri uri, String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("Wrong URI: " + uri.toString());
        }
    }

    private String getSelectionWithIdSelectionAppendedIfSignTableSingleRow(Uri uri, String selection) {
        if (isSignTableSingleRow(uri)) {
            selection = appendIdSelectionToSelection(uri, selection);
        }
        return selection;
    }

    private boolean isSignTableSingleRow(Uri uri) {
        return matcher.match(uri) == SIGN_TABLE_SINGLE_ROW;
    }

    private String appendIdSelectionToSelection(Uri uri, String selection) {
        String idSelection = BrtaSignsContract.Sign._ID + " = " + uri.getLastPathSegment();
        selection = selection == null ? idSelection : selection + " AND " + idSelection;
        return selection;
    }

    private void setNotificationUri(Uri uri, Cursor cursor) {
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case SIGN_SET_TABLE:
                return BrtaSignsContract.SignSet.CONTENT_TYPE;
            case SIGN_TABLE:
                return BrtaSignsContract.Sign.CONTENT_TYPE;
            case SIGN_TABLE_SINGLE_ROW:
                return BrtaSignsContract.Sign.CONTENT_TYPE_SINGLE_ROW;
            default:
                return null;
        }
    }

    void setSQLiteOpenHelper(SQLiteOpenHelper sqLiteOpenHelper) {
        db = sqLiteOpenHelper;
    }

    void setQueryWithLoggingEnabledCursorFactory(boolean queryWithLoggingEnabledCursorFactory) {
        this.queryWithLoggingEnabledCursorFactory = queryWithLoggingEnabledCursorFactory;
    }
}
