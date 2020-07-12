package com.vand.capst.capstoneproject_vanderbiltuniversity;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This class represent Adapter for RecyclerView that displays results retrieved from Web Service call.
 * Constructor of this class takes a Java ARRAY of type Webresponse. Each individual Webresponse object
 * is then displayed in a single Card View within the RecycleView.
 * This adapter class binds the appropriate cardview to its corresponding RecyclerView.
 */

class CapstoneAdapter extends RecyclerView.Adapter<CapstoneAdapter.ViewHolder> {

    private Webresponse[] responses;

    public CapstoneAdapter(Webresponse[] responsecollection){
        this.responses=responsecollection;
    }

    //a ViewHolder object is mandatory for RecyclerView. It takes CardView as constructor parameter.
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;

        public ViewHolder(CardView cv){
            super(cv);
            this.cardView=cv;
        }
    }

    //total number of items represents total number of cardviews in the recyclerview.
    @Override
    public int getItemCount(){
        return responses.length;
    }

    //this method associates specific cardview via inflation and instantiates the ViewHolder object.
    @Override
    public CapstoneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        CardView cdvw = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_v,parent,false);
        return new ViewHolder(cdvw);

    }

    //the data from the supplied JAVA Array is then spread throughout cardviews in RecyclerView.
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){

        final CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.web_response);
        textView.setText(responses[position].getName());
    }
}
