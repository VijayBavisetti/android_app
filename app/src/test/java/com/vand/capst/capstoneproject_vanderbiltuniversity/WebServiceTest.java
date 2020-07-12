package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.content.Intent;
import android.os.IBinder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Sudeep.Pandey on 1/24/2018.
 *
 * The below test verifies successful instantiation of the WebService.
 * Also, when onBind() is called, appropriate Binder object should return.
 */
@RunWith(RobolectricTestRunner.class)
public class WebServiceTest {

    private WebService service;

    @Before
    public void setUp(){
        service = Robolectric.setupService(WebService.class);
        }

    //onBind() method should return IBinder.
   @Test
   public void onBindReturnsIBinder() throws Exception{

       IBinder b = service.onBind(new Intent());
       assertNotNull(b);
       assertTrue(b instanceof IBinder);
       assertTrue(b instanceof WebService.LocalBinder);
   }

   //service object instantiated..
   @Test
    public void setUpServiceTest() throws Exception{

       assertTrue(service !=null);
   }
}
