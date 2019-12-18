package xyz.maona.lockoo;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        new Lockoo(this).lock();
        finish();
    }
}
