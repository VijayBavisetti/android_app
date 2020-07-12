package com.vand.capst.capstoneproject_vanderbiltuniversity;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ResultView extends Activity {

    /**
     * This activity will be instantiated via Intent from the MainActivity. The intent will
     * carry JAVA Array which will then be passed as parameter to the RecyclerView adapter.
     * Once the adapter is set to the recyclerview, cardviews are then displayed in the
     * recyclerview.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);

        String[] myResponses = getIntent().getStringArrayExtra("responseArray");
        Webresponse[] responseObjects = new Webresponse[myResponses.length];
        for(int i=0;i<myResponses.length;i++){
            responseObjects[i]=(new Webresponse(myResponses[i]));
        }
        //get Webresponse[] from intent.
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        CapstoneAdapter adapter = new CapstoneAdapter(responseObjects);

        recyclerView.setAdapter(adapter);

        //Layout Manager is mandatory for recyclerview.
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }
}
