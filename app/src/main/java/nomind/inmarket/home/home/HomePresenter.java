package nomind.inmarket.home.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import nomind.inmarket.home.managers.LocationAlarmManager;
import nomind.inmarket.home.listeners.LocationListener;
import nomind.inmarket.home.util.Util;

/**
 * Created by sayoojvalsan on 2/7/17.
 */

public class HomePresenter implements HomePresenterInterface {
    private static final String TAG = HomePresenter.class.getSimpleName();

    private static final String LOCATION_KEY = "LOCATION_KEY";
    HomeViewInterface mViewInterface;
    LocationModel mLocationModel;
    Context mContext;
    private Location mCurrentLocation;
    private HomePresenterListener mHomePresenterListener;

    public HomePresenter(HomeViewInterface viewInterface, LocationModel locationModel, Context context) {
        mViewInterface = viewInterface;
        mLocationModel = locationModel;
        mContext = context;
    }

    public void setListener(HomePresenterListener homePresenterListener){
        mHomePresenterListener = homePresenterListener;
    }

    private void setUpAlarmManager() {

        LocationAlarmManager alarmManager = new LocationAlarmManager();
        alarmManager.init(mContext);
    }

    private void showLocation(Location location) {
        mViewInterface.showLocation(location.getLatitude(), location.getLongitude());
    }


    @Override
    public void onViewLoaded() {
        if(!Util.doWeHavePermission(mContext)){

            if(mHomePresenterListener != null){
                mHomePresenterListener.onException(new IllegalStateException("Need location permission"));
            }
            return;
        }

        if(!Util.isGooglePlayServicesAvailable(mContext)){
            if(mHomePresenterListener != null){
                mHomePresenterListener.onException(new IllegalStateException("Require Google play services"));
            }
            return;
        }

        Log.d(TAG, "Permission are all good");
        setUpAlarmManager();


        mViewInterface.onShowProgress();
        mLocationModel.fetch(new LocationListener() {
            @Override
            public void onLocationFound(Location location) {
                mCurrentLocation = location;
                showLocation(location);
            }

            @Override
            public void onLastLocationFound(Location location) {
                mCurrentLocation = location;
                showLocation(location);

            }
        });

    }


    @Override
    public void onDestroy() {
        mLocationModel.onDestroy();
    }

    @Override
    public void onPause() {
        mLocationModel.onPause();
    }

    @Override
    public void onResume() {
        mLocationModel.resume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mCurrentLocation != null) {
            outState.putParcelable(LOCATION_KEY, mCurrentLocation);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
            // Since LOCATION_KEY was found in the Bundle, we can be sure that
            // mCurrentLocationis not null.
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            showLocation(mCurrentLocation);

        }
    }

    public interface HomePresenterListener{

         void onException(Exception e);
    }
}
