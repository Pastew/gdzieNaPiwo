package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class PubsActivity extends Activity {

    TableLayout pubsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubs);

        pubsTable = (TableLayout) findViewById(R.id.pubs_table);

        for(Pub pub: getPubs()){
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView pubName = new TextView(this);
            pubName.setText(pub.getName());
            tr.addView(pubName);

            TextView beerPrice = new TextView(this);
            beerPrice.setText(Float.toString(pub.getBeerPrice()));
            tr.addView(beerPrice);

            TextView distance = new TextView(this);
            distance.setText(Integer.toString(pub.getDistance()));
            tr.addView(distance);

            pubsTable.addView(tr);
        }
    }

    private ArrayList<Pub> getPubs(){
        ArrayList<Pub> pubs =new ArrayList<>();

        ArrayList<BeerPrice> prices = new ArrayList<>();
        prices.add(new BeerPrice(3, 5));
        prices.add(new BeerPrice(4, 5));

        pubs.add(new Pub(1, "Karlik", "Boleslawa 12", prices));

        prices.add(new BeerPrice(2, 5));
        prices.add(new BeerPrice(3, 7));

        pubs.add(new Pub(1, "Disco", "Dyskotekowa 12", prices));

        prices.add(new BeerPrice(5, 5));
        prices.add(new BeerPrice(6, 7));

        pubs.add(new Pub(1, "Melina", "Zulowska 12", prices));

        return pubs;
    }
}
