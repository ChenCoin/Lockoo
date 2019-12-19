package xyz.maona.lockoo;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

class Lockoo {

    private Context context;

    Lockoo(Context context) {
        this.context = context;
    }

    void lock() {
        boolean advanced = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
        boolean meizu = android.os.Build.BRAND.toLowerCase().equals("meizu") &&
                android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.P;
        if (advanced && !meizu)
            lockScreenOverP();
        else lockScreen();
    }

    private void lockScreen() {
        ComponentName component = new ComponentName(context, android.app.admin.DeviceAdminReceiver.class);
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (policyManager == null) return;
        if (!policyManager.isAdminActive(component)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    context.getResources().getString(R.string.app_name));
            context.startActivity(intent);
        } else policyManager.lockNow();
    }

    private void lockScreenOverP() {
        boolean settingOn = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null)
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(1000))
                if (service.service.getClassName().equals(MyAccessibilityService.class.getName()))
                    settingOn = true;
        if (settingOn) {
            MyAccessibilityService.lock();
        } else {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
