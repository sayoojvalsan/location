package nomind.inmarket.home.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import nomind.inmarket.home.managers.LocationAlarmManager;

/**
 * Created by sayoojvalsan on 2/8/17.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            LocationAlarmManager alarmManager = new LocationAlarmManager();
            alarmManager.init(context);
        }
    }
}
