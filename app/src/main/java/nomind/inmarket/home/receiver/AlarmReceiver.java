package nomind.inmarket.home.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import nomind.inmarket.home.services.FetchLocationService;



/**
 * Created by sayoojvalsan on 2/8/17.
 */

/**
 * Responsible for starting up {@link FetchLocationService}
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    final static String TAG = AlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "AlarmReceiver woke up. Nice!");

        //Start our location fetch service
        Intent startLocationService = new Intent(context, FetchLocationService.class);
        context.startService(startLocationService);
        Log.d(TAG, "Initiating FetchLocationService...!");

    }
}
