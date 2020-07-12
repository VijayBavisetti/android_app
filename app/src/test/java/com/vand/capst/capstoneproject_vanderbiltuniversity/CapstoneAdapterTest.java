package com.vand.capst.capstoneproject_vanderbiltuniversity;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.RoboLayoutInflater;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.TextView;


/**
 * Created by Vijay Babu Bavisetti on 28/06/2020.
 *
 * The following class includes test cases for CapstoneAdapter. This adapter was used to generate the RecyclerView that holds all
 * the card views with data retrieved from Google Web Service.
 */
@RunWith(RobolectricTestRunner.class)
public class CapstoneAdapterTest {

    CapstoneAdapter adapter;
    Webresponse[] responses = new Webresponse[]{new Webresponse("one"),new Webresponse("two")};
    CardView cardView;
    RecyclerView rView;

    @Before
    public void setUp(){
        adapter = new CapstoneAdapter(responses);
        RoboLayoutInflater inflater = new RoboLayoutInflater(Robolectric.buildActivity(ResultView.class).get());
        cardView =  (CardView)inflater.from(Robolectric.buildActivity(ResultView.class).get()).inflate(R.layout.card_v,null);
        rView = (RecyclerView)inflater.from(Robolectric.buildActivity(ResultView.class).get()).inflate(R.layout.activity_result_view,null).findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3,1);
        rView.setLayoutManager(manager);
    }

    //checks whether number of items(cardviews) is equal to size of array used as constructor parameter.
    @Test
    public void checkIfgetItemCountReturnsCorrectNumber() throws Exception{
        int itemCount = adapter.getItemCount();
        assertEquals(itemCount,2);
    }

    //checks if ViewHolder of the recyclerview got successfully instantiated and it is associated with the correct cardview.
    @Test
    public void onCreateViewHolderShouldCreateCorrectViewHolder() throws Exception{

        CapstoneAdapter.ViewHolder holder = adapter.onCreateViewHolder(rView,0);
        assertNotNull(holder);
        assertTrue(holder.getClass()==(new CapstoneAdapter.ViewHolder(cardView).getClass()));
    }

    //checks if the cardviews in recyclerview display the correct data.
    @Test
    public void checkIfOnBindViewHolderGeneratesCorrectCardView() throws Exception{

        TextView textView = cardView.findViewById(R.id.web_response);

        adapter.onBindViewHolder(new CapstoneAdapter.ViewHolder(cardView),1);
        assertEquals(textView.getText(),"two");

        adapter.onBindViewHolder(new CapstoneAdapter.ViewHolder(cardView),0);
        assertEquals(textView.getText(),"one");

    }
}
