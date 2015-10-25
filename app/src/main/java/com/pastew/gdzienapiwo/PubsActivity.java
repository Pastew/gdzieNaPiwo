package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class PubsActivity extends Activity {

    private ArrayList<View> MAINTABLES = new ArrayList<>();
    private ArrayList<View> SUBTABLES = new ArrayList<>();
    private TableLayout pubsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubs);
        pubsTable = (TableLayout) findViewById(R.id.pubs_table);

        showPubs();
    }

    private void showPubs(){
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest("https://mysterious-shelf-1380.herokuapp.com/pubs",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                ArrayList<Pub> pubs = new ArrayList<>();
                // Successfully download json
                // So parse it and populate the listview
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        Pub pub = new Pub();

                        //name, address
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        pub.setName(jsonObject.getString("name"));
                        pub.setAddress(jsonObject.getString("address"));

                        //prices
                        ArrayList<BeerPrice> beerPrices = new ArrayList<>();
                        JSONArray beerPricesJson = jsonObject.getJSONArray("beer_prices");
                        for(int j = 0 ; j < beerPricesJson.length() ; ++j){
                            JSONObject beer_price = beerPricesJson.getJSONObject(j);
                            float price = (float) beer_price.getDouble("price");
                            int votes = beer_price.getInt("votes");
                            beerPrices.add(new BeerPrice(price, votes));
                        }

                        pub.setBeerPrices(beerPrices);
                        // perks
                        HashMap<String,Boolean> perks = new HashMap<>();
                        for(String perk : Global.PERKS) {
                            perks.put(perk, jsonObject.getBoolean(perk));
                        }
                        pub.setPerks(perks);

                        pubs.add(pub);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                populateTable(pubs);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array");
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);
    }

    private void populateTable(ArrayList<Pub> pubs) {

        Collections.sort(pubs, new Comparator<Pub>() {
            public int compare(Pub b1, Pub b2) {
                if (b1.getBeerPrice() > b2.getBeerPrice())
                    return 1;
                if (b1.getBeerPrice() < b2.getBeerPrice())
                    return -1;
                return 0;
            }
        });

        for (Pub pub : pubs) {
            if(pub.getBeerPrice()>0.1)
                addPub(pub);
        }

        for (Pub pub : pubs) {
            if(pub.getBeerPrice()<0.1)
                addPub(pub);
        }
    }

    private void addPub(Pub pub) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView pubName = new TextView(this);
        pubName.setTextAppearance(getApplicationContext(), R.style.pub_name);
        pubName.setText(pub.getName());
        tr.addView(pubName);

        TextView beerPrice = new TextView(this);
        beerPrice.setTextAppearance(getApplicationContext(), R.style.beer_price);
        float price = pub.getBeerPrice();
        if(price < 0.1)
            beerPrice.setText("-");
        else
            beerPrice.setText(Float.toString(price));
        tr.addView(beerPrice);

        TextView distance = new TextView(this);
        distance.setTextAppearance(getApplicationContext(), R.style.distance );
        distance.setText(pub.getDistance());
        tr.addView(distance);

        pubsTable.addView(tr);
        MAINTABLES.add(tr);

        pubName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubTable(v);
            }
        });

        // ====== dropping down table -  additional info =======
        // first column of sub table
        TableRow trSub = new TableRow(this);
        LinearLayout ll1 = new LinearLayout(this);
        ll1.setOrientation(LinearLayout.VERTICAL);

        // address
        TextView address = new TextView(this);
        address.setTextAppearance(getApplicationContext(), R.style.pub_address);
        address.setText(pub.getAddress());
        ll1.addView(address);


        LinearLayout perksAndVotes = new LinearLayout(this);
        perksAndVotes.setOrientation(LinearLayout.HORIZONTAL);
        ll1.addView(perksAndVotes);

        LinearLayout perks = new LinearLayout(this);
        perks.setOrientation(LinearLayout.VERTICAL);
        perksAndVotes.addView(perks);

        LinearLayout votes = new LinearLayout(this);
        votes.setOrientation(LinearLayout.VERTICAL);
        perksAndVotes.addView(votes);

        for(HashMap.Entry<String, Boolean> entry : pub.getPerks().entrySet()){
            String key = entry.getKey();
            boolean value = entry.getValue();

            //don't add empty perks
            //if(!value)
            //  continue;

            CheckBox perk = new CheckBox(this);
            perk.setClickable(false);
            perk.setText(keyToString(key));
            perk.setChecked(value);
            perks.addView(perk);
        }
        trSub.addView(ll1);

        // second col - prices/votes
        LinearLayout ll2 = new LinearLayout(this);
        ll2.setOrientation(LinearLayout.VERTICAL);

        ArrayList<BeerPrice> beerPrices = pub.getBeerPrices();
        Collections.sort(beerPrices, new Comparator<BeerPrice>() {
            public int compare(BeerPrice b1, BeerPrice b2) {
                if (b1.getVotes() > b2.getVotes())
                    return -1;
                if (b1.getVotes() < b2.getVotes())
                    return 1;
                return 0;
            }
        });

        for(int i = 0 ; i < 3 && i < beerPrices.size(); ++i) {
            BeerPrice bp = beerPrices.get(i);

            TextView v = new TextView(getApplicationContext());
            v.setText(bp.getPrice() + getString(R.string.pln) + bp.getVotes() + getString(R.string.votes));
            v.setTextAppearance(getApplicationContext(), R.style.votes);
            votes.addView(v);
        }
        /*
        TextView map = new TextView(this);
        map.setText("MAP");
        ll3.addView(map);
        */
        trSub.addView(ll2);

        trSub.setVisibility(View.GONE);
        pubsTable.addView(trSub);
        SUBTABLES.add(trSub);
    }

    private void showSubTable(View pubTextView) {

        View parentTable = ((View) pubTextView.getParent());
        int parentTableIndex = MAINTABLES.indexOf(parentTable);
        View viewToShow = SUBTABLES.get(parentTableIndex);

        for(View sub : SUBTABLES) {
            if(sub == viewToShow) {
                viewToShow.setVisibility(SUBTABLES.get(parentTableIndex).getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
            else
                sub.setVisibility(View.GONE);
        }
    }

    private String keyToString(String key) {
        for(int i = 0 ; i < Global.PERKS.length ; ++i){
            if ( key.equals(Global.PERKS[i]))
                return Global.PERKS_STRING[i];
        }
        return "?";
    }
}
