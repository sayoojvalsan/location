package nomind.inmarket.home.home;

import nomind.inmarket.home.listeners.LocationListener;

/**
 * Created by sayoojvalsan on 2/7/17.
 */

public interface LocationModelnterface {




     void onDestroy();


     void fetch(LocationListener locationListener);

     void onPause();

     void onResume();
}
