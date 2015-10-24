package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");

        Pub pub = getPub(id);
        ((TextView)findViewById(R.id.pub_name)).setText(pub.getName());

        Collections.sort(pub.getBeerPrices(), new CustomComparator());

        LinearLayout beerPricesList = (LinearLayout) findViewById(R.id.beer_prices_list);
        for(BeerPrice bp : pub.getBeerPrices()){
            TextView priceLine = new TextView(this);
            priceLine.setText(bp.getPrice() + " | " + bp.getVotes());

            beerPricesList.addView(priceLine);
        }
    }

    private Pub getPub(int id) {
        ArrayList<BeerPrice> prices = new ArrayList<>();
        prices.add(new BeerPrice(3.5f, 3));
        prices.add(new BeerPrice(4, 5));
        prices.add(new BeerPrice(6, 6));
        return new Pub(1, "Karlik", "Czarnowiejska 12", prices);
    }

    public class CustomComparator implements Comparator<BeerPrice> {
        @Override
        public int compare(BeerPrice o1, BeerPrice o2) {
            return o1.getPrice() > o2.getPrice() ? 1 : 0;
        }
    }
}
