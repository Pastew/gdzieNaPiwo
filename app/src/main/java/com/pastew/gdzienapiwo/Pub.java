package com.pastew.gdzienapiwo;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Pastew on 2015-10-24.
 */
public class Pub {
    private int id;
    private String name;
    private String address;
    private ArrayList<BeerPrice> beerPrices;

    public Pub(){

    }

    public Pub(int id, String name, String address, ArrayList<BeerPrice> beerPrices) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.beerPrices = beerPrices;
    }

    public String getName() {
        return name;
    }

    public ArrayList<BeerPrice> getBeerPrices() {
        return beerPrices;
    }
}
