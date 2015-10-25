package com.pastew.gdzienapiwo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pastew on 2015-10-24.
 */
public class Pub {
    private int id;
    private String name;
    private String address;
    private ArrayList<BeerPrice> beerPrices;


    HashMap<String, Boolean> perks;

    public Pub(int id, String name, String address, ArrayList<BeerPrice> beerPrices, HashMap<String, Boolean> perks) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.beerPrices = beerPrices;
        this.perks = perks;
    }

    public Pub() {}

    public String getName() {
        return name;
    }

    public ArrayList<BeerPrice> getBeerPrices() {
        return beerPrices;
    }

    public float getBeerPrice() {
        if(beerPrices == null)
            return 0;

        float result = 0;
        int votesMax = 0;
        for(BeerPrice bp : beerPrices){
            if(bp.getVotes() > votesMax){
                votesMax = bp.getVotes();
                result = bp.getPrice();
            }
        }

        return result;
    }

    public HashMap<String, Boolean> getPerks(){
        return perks;
    }

    public void setPerks(HashMap<String, Boolean> perks) {
        this.perks = perks;
    }

    public String getDistance() {
        return " ";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setBeerPrices(ArrayList<BeerPrice> prices) {
        this.beerPrices = prices;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }
}
