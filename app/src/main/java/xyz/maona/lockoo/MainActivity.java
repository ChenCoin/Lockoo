package xyz.maona.lockoo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends Activity {
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        boolean advanced = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
        boolean meizu = android.os.Build.BRAND.toLowerCase().equals("meizu") &&
                android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.P;
        if (advanced && !meizu)
            lockScreenOverP();
        else lockScreen();
        finish();
    }

    private void lockScreen() {
        ComponentName componentName = new ComponentName(this, android.app.admin.DeviceAdminReceiver.class);
        DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (policyManager == null) return;
        if (!policyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getResources().getString(R.string.app_name));
            startActivity(intent);
        } else policyManager.lockNow();
    }

    private void lockScreenOverP() {
        boolean settingOn = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(1000))
            if (service.service.getClassName().equals(MyAccessibilityService.class.getName()))
                settingOn = true;
        if (settingOn) {
            MyAccessibilityService.lock();
        } else startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }
}
