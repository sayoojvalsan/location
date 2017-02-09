package nomind.inmarket.home.home;

import android.os.Bundle;

/**
 * Created by sayoojvalsan on 2/7/17.
 */

public interface HomePresenterInterface {

     void onViewLoaded();

     void onDestroy();


     void onPause();

     void onResume();

     void onSaveInstanceState(Bundle outState);

     void onRestoreInstanceState(Bundle savedInstanceState);
}
