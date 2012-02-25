package net.incredibles.brtaprovider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author sharafat
 * @Created 2/15/12 12:05 PM
 */
public final class BrtaSignsContract {
    public static final String AUTHORITY = "net.incredibles.brtaprovider";

    private static final String SCHEME = "content://";

    public static final class SignSet implements BaseColumns {
        public static final String TABLE_NAME = "sign_set";
        private static final String PATH = "/" + TABLE_NAME;

        public static final String COL_NAME = "name";

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;

        private SignSet() {
        }
    }

    public static final class Sign implements BaseColumns {
        public static final String TABLE_NAME = "sign";
        private static final String PATH = "/" + TABLE_NAME;

        public static final String COL_SIGN_SET = "sign_set";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE = "image";

        public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
        public static final String CONTENT_TYPE_SINGLE_ROW = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

        private Sign() {
        }
    }

    private BrtaSignsContract() {
    }

}
