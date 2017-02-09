package nomind.inmarket.home.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import nomind.inmarket.home.listeners.LocationListener;
import nomind.inmarket.home.util.Util;

/**
 * Created by sayoojvalsan on 2/7/17.
 */

public class HomePresenter implements HomePresenterInterface {
    private static final String TAG = HomePresenter.class.getSimpleName();

    private static final String LOCATION_KEY = "LOCATION_KEY";
    private HomeViewInterface mViewInterface;
    private LocationModel mLocationModel;
    private Context mContext;
    private Location mCurrentLocation;
    private HomePresenterListener mHomePresenterListener;
    private boolean mTestMode;

    /**
     * Presenter which helps communication between view and location service
     * @param viewInterface
     * @param locationModel
     * @param context
     */
    public HomePresenter(HomeViewInterface viewInterface, LocationModel locationModel,  Context context) {
        mViewInterface = viewInterface;
        mLocationModel = locationModel;
        mContext = context;
    }

    public void setListener(HomePresenterListener homePresenterListener){
        mHomePresenterListener = homePresenterListener;
    }



    private void showLocation(Location location) {
        mViewInterface.showLocation(location.getLatitude(), location.getLongitude());
    }


    @Override
    public void onViewLoaded() {
        //Check if we have correct permissions
        if(!doWeHavePermission(mContext)){

            if(mHomePresenterListener != null){
                mHomePresenterListener.onException(new IllegalStateException("Need location permission"));
            }
            return;
        }
        //Check if we have play services
        if(!isGooglePlayServicesAvailable(mContext)){
            if(mHomePresenterListener != null){
                mHomePresenterListener.onException(new IllegalStateException("Require Google play services"));
            }
            return;
        }
        //Let the view know we are showing progress
        mViewInterface.onShowProgress();

        //Fetch the latest location
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
        mLocationModel.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Save the current location. Helpful when device rotates
        if(mCurrentLocation != null) {
            outState.putParcelable(LOCATION_KEY, mCurrentLocation);
        }

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        //restore the current location. Helpful when device rotates
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

    /**
     * Check if we have Location permissions
     * @param context
     * @return boolean
     */
    private boolean doWeHavePermission(Context context){
        if(mTestMode) return true;
        return Util.doWeHavePermission(context);
    }

    private boolean isGooglePlayServicesAvailable(Context context){
        if(mTestMode) return true;
        return Util.isGooglePlayServicesAvailable(context);
    }

    /**
     * User for testing
     */
    public void setTestMode(){
        mTestMode = true;
    }
}
