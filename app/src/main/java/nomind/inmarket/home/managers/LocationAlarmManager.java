package nomind.inmarket.home.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;

import nomind.inmarket.home.receiver.AlarmReceiver;


/**
 * Created by sayoojvalsan on 2/8/17.
 */

/**
 * Responsible for setting Alarm Manafer This will wake the app at midnight and start {@link AlarmReceiver}
 */
public class LocationAlarmManager {


    private static String TAG = LocationAlarmManager.class.getSimpleName();


    public void init(Context context) {



        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 108, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //Wake up at midnight
        //calendar.add(Calendar.DAY_OF_MONTH, 1);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                4000, alarmIntent);



    }


}
