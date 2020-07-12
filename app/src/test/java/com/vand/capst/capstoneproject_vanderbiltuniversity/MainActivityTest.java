package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Button;
import android.widget.Spinner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Sudeep.Pandey on 1/22/2018.
 *
 * The following tests cover MainActivity lifecycle phases and events that take place during various stages of
 * the lifecycle.
 * The test cases also verify the state of the activity utilizing the User Interface checks.
 * The test also creates mock WebService and verifies the interaction of MainActivity with WebService.
 *
 */

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    private Activity activity;
    BroadcastReceiver mReceiver;

    //onCreate() phase of the activity lifecycle - UI initialization test
    @Test
    public void onCreateLifeCyclePhaseTest() throws Exception{

        activity= Robolectric.buildActivity(MainActivity.class).create().get();
        assertNotNull(activity);

        Spinner spinner = activity.findViewById(R.id.spinner);

        assertFalse(spinner.getAdapter().isEmpty());
        assertEquals(spinner.getCount(),8);
        assertEquals(spinner.getItemAtPosition(0),"Statue of Liberty");

        Spinner spinner2 = activity.findViewById(R.id.spinner2);

        assertFalse(spinner2.getAdapter().isEmpty());
        assertEquals(spinner2.getCount(),5);
        assertEquals(spinner2.getItemAtPosition(0),"hospital");

        assertFalse(activity.findViewById(R.id.button2).isEnabled());

    }

    //onResume() phase of the activity lifecycle - registering a receiver, sending broadcase and verifying the consequence.
    @Test
    public void onResumeLifeCyclePhaseTest() throws Exception{


        activity=Robolectric.buildActivity(MainActivity.class).create().resume().visible().get();
        boolean buttonEnabled = activity.findViewById(R.id.button).isEnabled();

        IntentFilter intentFilter = new IntentFilter(
                "capstone.project.action.PERSIST_COMPLETE");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                Button mybutton = activity.findViewById(R.id.button2);
                mybutton.setEnabled(true);

            }
        };

        activity.registerReceiver(this.mReceiver, intentFilter);
        assertTrue(mReceiver!=null);
        activity.sendBroadcast(new Intent("capstone.project.action.PERSIST_COMPLETE"));
        assertTrue(activity.findViewById(R.id.button2).isEnabled());
    }

    //mocking the bound service and verifying onStart() lifecycle phase.
    @Test
    public void onStartStateTest() throws Exception{
       mockBoundLocalService();
       activity=Robolectric.buildActivity(MainActivity.class).start().get();
        assertTrue(activity!=null);
    }

    //mocking the bound service and performing UI operations
    @Test
    public void button_Find_PerformClickWorks() throws Exception{

        mockBoundLocalService();

        activity=Robolectric.buildActivity(MainActivity.class).create().start().resume().visible().get();

        Spinner mySpinner=(Spinner) activity.findViewById(R.id.spinner);

        assertTrue(mySpinner.getCount()!=0);
        assertTrue(activity.findViewById(R.id.button).isEnabled());
        assertFalse(activity.findViewById(R.id.button2).isEnabled());
        assertTrue(activity.findViewById(R.id.button).isClickable());

        Mockito.doCallRealMethod().when(mock(WebService.class)).execute(anyString(), anyString());
        activity.findViewById(R.id.button).performClick();

        assertFalse(activity.findViewById(R.id.button2).isEnabled());
    }

    //unregistering the broadcast receiver during onPause() lifecycle phase.
   @Test
   public void onPauseLifecycleMethodUnregistersReceiver() throws Exception{

       activity=Robolectric.buildActivity(MainActivity.class).create().resume().visible().pause().get();
       activity.sendBroadcast(new Intent("capstone.project.action.PERSIST_COMPLETE"));
       assertFalse(activity.findViewById(R.id.button2).isEnabled());
   }

   //onStop() - activity persists.
    @Test
    public void onStopMethodRetainsActivity() throws Exception{

        mockBoundLocalService();
        activity=Robolectric.buildActivity(MainActivity.class).create().start().resume().visible().pause().stop().get();

        assertTrue(activity!=null);
    }

   private void mockBoundLocalService(){
       WebService.LocalBinder stubBinder = mock(WebService.LocalBinder.class);
       when(stubBinder.getService()).thenReturn(mock(WebService.class));
       shadowOf(RuntimeEnvironment.application).setComponentNameAndServiceForBindService(new ComponentName("com.vand.capst.capstoneproject_vanderbiltuniversity","WebService"), stubBinder);
   }
}
