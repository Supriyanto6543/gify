package app.gify.co.id.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent = new Intent("app.gify.co.id.activity.MyService");
            context.startService(serviceIntent);
        }
    }
}

