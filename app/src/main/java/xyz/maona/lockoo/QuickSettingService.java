package xyz.maona.lockoo;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.quicksettings.TileService;

@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingService extends TileService {
    @Override
    public void onClick() {
        new Lockoo(this, true).lock();
    }
}
