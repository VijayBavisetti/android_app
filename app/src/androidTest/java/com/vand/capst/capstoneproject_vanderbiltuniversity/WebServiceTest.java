package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Sudeep.Pandey on 1/22/2018.
 * The below is integration test that verifies that bound service can operate well.
 */
@RunWith(AndroidJUnit4.class)
public class WebServiceTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    //bound service should operate properly. It should return IBinder during onBind(), return its instance
    //when getService() is called and execute() method should run.
    @Test
    public void testWithBoundService() throws Exception{

        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(), WebService.class);

        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call public methods on the binder directly.
        WebService service = ((WebService.LocalBinder) binder).getService();

        // Verify that the service is working correctly.
        service.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.40,-70.32&radius=1000&type=hospital&key=AIzaSyDo7-hsZ6-c5YaxfB8R906UFjkOE20K3yA","hospital");
    }
}
