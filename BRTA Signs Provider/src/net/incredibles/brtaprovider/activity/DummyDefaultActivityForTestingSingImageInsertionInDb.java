package net.incredibles.brtaprovider.activity;

import android.app.Activity;
import android.os.Bundle;
import net.incredibles.brtaprovider.BrtaSignsContract;

/**
 * @author sharafat
 * @Created 2/15/12 3:34 PM
 */
public class DummyDefaultActivityForTestingSingImageInsertionInDb extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContentResolver().query(BrtaSignsContract.Sign.CONTENT_URI, null, null, null, null);
    }
}
