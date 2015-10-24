package com.pastew.gdzienapiwo;

/**
 * Created by Pastew on 2015-10-24.
 */
public class BeerPrice {

    public float price;
    public int votes;


    public BeerPrice(float price, int votes) {
        this.price = price;
        this.votes = votes;
    }

    public float getPrice() {
        return price;
    }

    public int getVotes() {
        return votes;
    }
}
