package xyz.maona.lockoo;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {

    private static MyAccessibilityService service;

    public static void lock() {
        if (service != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            service.performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service = null;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
