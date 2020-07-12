package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Sudeep.Pandey on 1/25/2018.
 *
 * The below test verifies that RecycleView object gets instantiated when ResultView activity
 * gets created via appropriate Intent object.
 */
@RunWith(RobolectricTestRunner.class)
public class ResultViewTest {

    private Activity activity;

    //starting second activity with an intent - RecyclerView gets instantiated.
    @Test
    public void checkOnCreateLifeCycleStage() throws Exception{

        Intent intent = new Intent(RuntimeEnvironment.application,ResultView.class);
        intent.putExtra("responseArray",new String[]{"one","two","three","four","five"});

        activity = Robolectric.buildActivity(ResultView.class,intent).create().resume().visible().get();
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        assertNotNull(recyclerView);
    }
}
