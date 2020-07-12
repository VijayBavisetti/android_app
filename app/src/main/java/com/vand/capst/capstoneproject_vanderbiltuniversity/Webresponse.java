package com.vand.capst.capstoneproject_vanderbiltuniversity;

/**
 * This is a POJO class that represents JAVA objects carrying data returned from Web Service call.
 * Every single response will be carried as a single POJO object and later packed in JAVA array
 * and passed to RecyclerView Adapter for diplay.
 */
public class Webresponse {

    private String placeName;

    public Webresponse(String _name){
        this.placeName=_name;
    }

    public String getName(){
        return placeName;
    }
}
