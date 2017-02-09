package nomind.inmarket.home.listeners;

import android.location.Location;

/**
 * Created by sayoojvalsan on 2/7/17.
 */

public interface LocationListener {

    void onLocationFound(Location location);
    void onLastLocationFound(Location location);
}
