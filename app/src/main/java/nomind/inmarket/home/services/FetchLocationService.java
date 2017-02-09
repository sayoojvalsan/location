package nomind.inmarket.home.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import nomind.inmarket.home.listeners.LocationListener;
import nomind.inmarket.home.home.LocationModel;
import nomind.inmarket.home.util.Util;


public class FetchLocationService extends Service implements LocationListener {

    private String TAG = FetchLocationService.class.getSimpleName();
    private Intent mIntent;
    private LocationModel mLocationModel;
    private Thread mTimerThread;

    public FetchLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "FetchLocationService started");


        if (!Util.doWeHavePermission(getApplicationContext()) || !Util.isGooglePlayServicesAvailable(getApplicationContext())){
            Log.w(TAG, "No permission or Google play services are missing. Stoping the service");
            stopSelf();
            return START_NOT_STICKY;
        }


        //Start our timer thread to stop location updates after sometime.
        //We dont need our service running all day long if no location is received in 20 Secs
        mTimerThread = new Thread(new TimerRunnable());
        mTimerThread.start();

        mIntent = intent;



        mLocationModel  = new LocationModel(getApplicationContext());
        mLocationModel.fetch(this);


        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        try {
            WakefulBroadcastReceiver.completeWakefulIntent(mIntent);
            Log.d(TAG, "FetchLocationService completed");

        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public void onLocationFound(Location location) {
        Log.d(TAG, "Location found!");
        //Dont have to do anything.
        stopSelf();
        mTimerThread.interrupt();
    }

    @Override
    public void onLastLocationFound(Location location) {
        //Dont have to do anything.
        Log.d(TAG, "Location found!");
        stopSelf();
        mTimerThread.interrupt();
    }

    class TimerRunnable implements Runnable{

        @Override
        public void run() {

            try {
                Thread.currentThread().sleep(20 * 1000);
                Log.d(TAG, "Giving up after 20 secs of wait..");
                mLocationModel.onDestroy();
                stopSelf();
            } catch (InterruptedException e) {
                Log.w(TAG, "Got Interrupted. But all for good..");

            }
        }
    }
}
