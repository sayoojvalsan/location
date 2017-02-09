package nomind.inmarket.home.home;

/**
 * Created by sayoojvalsan on 2/7/17.
 */

public interface HomeViewInterface {


    void showLocation(double lat, double lon);

    void onLastLocationFound(double lat, double lon);

    void onShowProgress();

}
