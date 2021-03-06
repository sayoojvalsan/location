package nomind.inmarket.home;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import butterknife.BindView;
import butterknife.ButterKnife;
import nomind.inmarket.R;
import nomind.inmarket.home.home.HomePresenter;
import nomind.inmarket.home.home.HomeViewInterface;
import nomind.inmarket.home.home.LocationModel;
import nomind.inmarket.home.managers.LocationAlarmManager;

public class MainActivity extends AppCompatActivity implements HomeViewInterface, HomePresenter.HomePresenterListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 108;
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.location)
    TextView mLocationTextView;

    HomePresenter mHomePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mHomePresenter = new HomePresenter(this, new LocationModel(getApplicationContext()), getApplicationContext());
        mHomePresenter.setListener(this);
        checkPermissions();

        mLocationTextView.setText(getString(R.string.no_location_yet));
    }


    @Override
    protected void onPause() {
        mHomePresenter.onPause();
        super.onPause();
    }


    @Override
    protected void onResume() {
        mHomePresenter.onResume();
        super.onResume();
    }

    private void checkPermissions() {

        if(!isGooglePlayServicesAvailable(this)){
            return;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.d(TAG, "shouldShowRequestPermissionRationale");

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

            } else {

                // No explanation needed, we can request the permission.
                Log.d(TAG, "No explanation needed");

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // ACCESS_COARSE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
        else{
            init();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "We have the location permission");

                    init();
                } else {
                    Log.d(TAG, "Permission denied");
                    mLocationTextView.setText(getString(R.string.error_permission));
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }

    private void init() {
        new LocationAlarmManager().init(getApplicationContext());
        mHomePresenter.onViewLoaded();
    }

    @Override
    public void showLocation(double lat, double lon) {

        mLocationTextView.setText(lat + "  " + lon);
    }

    @Override
    public void onLastLocationFound(double lat, double lon) {
        mLocationTextView.setText(lat + "  " + lon);
    }

    @Override
    public void onShowProgress() {
        //TODO show progress if we need to
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mHomePresenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mHomePresenter.onRestoreInstanceState(savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onException(Exception e) {
        Log.d(TAG, "Exception " + e.getMessage());
    }


    /**
     * Check if Google play services is availble. If not, do the resolution if any
     * @param activity
     * @return boolean
     */
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

}
