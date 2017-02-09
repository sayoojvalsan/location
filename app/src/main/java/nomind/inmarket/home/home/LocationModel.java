package nomind.inmarket.home.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import nomind.inmarket.home.util.Util;

/**
 * Created by sayoojvalsan on 2/7/17.
 */

public class LocationModel implements LocationModelnterface, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private static final String TAG = LocationModel.class.getSimpleName();
    private final Context mContext;
    private nomind.inmarket.home.listeners.LocationListener mLocationListener;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private boolean mRequestingLocationUpdates;
    private Handler mHandler;
    private TimerRunnable mRunnable;


    public LocationModel(final Context context) {
        mContext = context;
        buildGoogleApiClient(context);

    }

    synchronized void buildGoogleApiClient(final Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }


    @Override
    public void onDestroy() {

        stopLocationUpdates();
        mHandler.removeCallbacks(mRunnable);
        mLocationListener = null;
        mGoogleApiClient.disconnect();
    }


    protected void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates");

        if (mRequestingLocationUpdates) {
            Log.d(TAG, "Location services are already enabled. Skipping");
            return;
        }
        mRequestingLocationUpdates = true;
        if (!Util.doWeHavePermission(mContext)){
            Log.w(TAG, "Permissions are missing. Skipping");
            return;
        }

        if(!mGoogleApiClient.isConnected()) {
            Log.w(TAG, "Google client is not connected. Skipping");

            return;
        }

        if (!Util.isGooglePlayServicesAvailable(mContext)) return;

        //We are checking for permission on Top
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    private void stopLocationUpdates() {
        Log.d(TAG, "stopLocationUpdates");
        mRequestingLocationUpdates = false;
        if(!mGoogleApiClient.isConnected()) return;
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }


    @Override
    public void fetch(nomind.inmarket.home.listeners.LocationListener locationListener) {
        mLocationListener = locationListener;
        if(!Util.isGooglePlayServicesAvailable(mContext)) return;

        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        startLocationUpdates();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "onConnected");
        mRunnable = new TimerRunnable();
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 20 * 1000);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setInterval(10000); // Update location every second

        startLocationUpdates();


        if (!Util.doWeHavePermission(mContext)) return;

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mLastLocation != null){
            mLocationListener.onLastLocationFound(mLastLocation);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Got new location..");

        if(mLocationListener != null){
            mLocationListener.onLocationFound(location);
            mHandler.removeCallbacks(mRunnable); //Remove our Timer
            stopLocationUpdates();
        }
    }


    public class TimerRunnable implements Runnable{

        @Override
        public void run() {
            try{
                Log.d(TAG, "Waited too long. Stopping location updates");
                stopLocationUpdates();
            }
            catch (Exception e){
                Log.w(TAG, "Timer Interrupted");
            }
        }
    }
}
