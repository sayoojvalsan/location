package nomind.inmarket.home.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import nomind.inmarket.home.listeners.LocationListener;
import nomind.inmarket.home.managers.LocationAlarmManager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by sayoojvalsan on 2/8/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class HomePresenterTest {


    @Mock
    HomeViewInterface mHomeViewInterface;

    @Mock
    LocationModel mLocationModel;


    @Mock
    Context mContext;

    @Mock
    private Location mLocation;

    private HomePresenter mHomePresenter;



    @Before
    public void setup() {

        mHomePresenter = new HomePresenter(mHomeViewInterface, mLocationModel, mContext);
        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((LocationListener) invocation.getArguments()[0]).onLocationFound(mLocation);
                return null;
            }
        }).when(mLocationModel).fetch(any(LocationListener.class));
        mHomePresenter.setTestMode();
    }


    @Test
    public void onViewLoaded() throws Exception {

        mHomePresenter.onViewLoaded();

        //Verify progress is called
        verify(mHomeViewInterface, times(1)).onShowProgress();

        //Verify show Location is shown
        verify(mHomeViewInterface, times(1)).showLocation(anyFloat(), anyFloat());


    }

    @Test
    public void onPause() throws Exception {

        mHomePresenter.onPause();
        verify(mLocationModel, times(1)).onPause();


    }

    @Test
    public void onResume() throws Exception {
        mHomePresenter.onResume();
        verify(mLocationModel, times(1)).onResume();

    }



}