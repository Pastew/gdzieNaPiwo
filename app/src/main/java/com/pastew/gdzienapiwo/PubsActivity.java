package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;


public class PubsActivity extends Activity {

    private static final String[] PERKS = {"pool", "garden", "dancefloor", "television", "karaoke", "pool"};
    private ArrayList<View> MAINTABLES = new ArrayList<>();
    private ArrayList<View> SUBTABLES = new ArrayList<>();
    TableLayout pubsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubs);

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

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        pub.setName(jsonObject.getString("name"));
                        pub.setAddress(jsonObject.getString("address"));

                        // perks
                        HashMap<String,Boolean> perks = new HashMap<>();
                        for(String perk : PERKS) {
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
        pubsTable = (TableLayout) findViewById(R.id.pubs_table);

        for (Pub pub : pubs) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView pubName = new TextView(this);
            pubName.setTextAppearance(getApplicationContext(), R.style.pub_name );
            pubName.setText(pub.getName());
            tr.addView(pubName);

            TextView beerPrice = new TextView(this);
            beerPrice.setTextAppearance(getApplicationContext(), R.style.beer_price );
            beerPrice.setText(Float.toString(pub.getBeerPrice()));
            tr.addView(beerPrice);

            TextView distance = new TextView(this);
            distance.setTextAppearance(getApplicationContext(), R.style.distance );
            distance.setText(Integer.toString(pub.getDistance()));
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

            for(HashMap.Entry<String, Boolean> entry : pub.getPerks().entrySet()){
                String key = entry.getKey();
                boolean value = entry.getValue();

                //don't add empty perks
                //if(!value)
                  //  continue;

                CheckBox perk = new CheckBox(this);
                perk.setClickable(false);
                perk.setText(key); // TODO dodaj do strings.xml
                perk.setChecked(value);
                ll1.addView(perk);
            }

            trSub.addView(ll1);

            // third col - map link
            LinearLayout ll3 = new LinearLayout(this);
            ll3.setOrientation(LinearLayout.VERTICAL);

            TextView map = new TextView(this);
            map.setText("MAP");
            ll3.addView(map);

            trSub.addView(ll3);

            trSub.setVisibility(View.GONE);
            pubsTable.addView(trSub);
            SUBTABLES.add(trSub);
        }
    }

    private void showSubTable(View pubTextView) {

        View parentTable = ((View) pubTextView.getParent());
        int parentTableIndex = MAINTABLES.indexOf(parentTable);
        View viewToShow = SUBTABLES.get(parentTableIndex);

        for(View sub : SUBTABLES) {
            if(sub == viewToShow)
                viewToShow.setVisibility(SUBTABLES.get(parentTableIndex).getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            else
                sub.setVisibility(View.GONE);
        }
    }
}
