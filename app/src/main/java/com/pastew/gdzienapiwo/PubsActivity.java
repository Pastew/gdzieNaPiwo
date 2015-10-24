package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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


public class PubsActivity extends Activity {

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
                ArrayList<Pub> pubs =new ArrayList<>();
                // Successfully download json
                // So parse it and populate the listview
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        Pub pub = new Pub();

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        pub.setName(jsonObject.getString("name"));
                        pub.setAddress(jsonObject.getString("address"));

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
}
