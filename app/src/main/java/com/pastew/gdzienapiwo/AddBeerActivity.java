package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class AddBeerActivity extends Activity {

    Button addBeerButton;
    AutoCompleteTextView pubNameTextView;
    TextView priceTextView;

    int[] PUBS_IDS;
    String[] PUBS_NAMES;
    ArrayList<Pub> pubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        initUI();

        downloadPubs();
    }

    private void initUI() {
        addBeerButton = (Button) findViewById(R.id.add_beer_button);
        priceTextView = (TextView)findViewById(R.id.price_edit_text);

        addBeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBeer();
            }
        });
    }

    private void addBeer(){
        int pubId = getPubId(pubNameTextView.getText().toString());
        if(pubId == -1)
            return;

        String priceStr = priceTextView.getText().toString();
        if(priceStr.length()==0) {
            Toast.makeText(getApplicationContext(), "Halo, halo. Brakuje ceny.", Toast.LENGTH_LONG).show();
            return;
        }

        float price = Float.parseFloat(priceStr);

        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        JSONObject params = new JSONObject();
        try {
            params.put("pub_id", pubId);
            params.put("price", price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject param = new JSONObject();
        try{
            param.put("beer_price", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://mysterious-shelf-1380.herokuapp.com/beers/add", param, new Response.Listener<JSONObject>(){
            public void onResponse(JSONObject response){
                Toast.makeText(getApplicationContext(), "Dodalem piwko", Toast.LENGTH_SHORT).show();
                //finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Wystapil blad :(", Toast.LENGTH_LONG).show();
                Log.e("Error", "Unable to parse json array");
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonObjectRequest);
    }

    private int getPubId(String text) {
        if(!Arrays.asList(PUBS_NAMES).contains(text)){
            Toast.makeText(getApplicationContext(), "Nie ma takiego pubu", Toast.LENGTH_LONG).show();
            return -1;
        }

        for(int i = 0 ; i < PUBS_IDS.length ; ++i){
            if(PUBS_NAMES[i].equalsIgnoreCase(text)){
                return PUBS_IDS[i];
            }
        }
        return -1;
    }

    private void downloadPubs(){
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest("https://mysterious-shelf-1380.herokuapp.com/pubs",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                pubs = new ArrayList<>();
                // Successfully download json
                // So parse it and populate the listview
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        Pub pub = new Pub();

                        //name, address
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        pub.setId(jsonObject.getInt("id"));
                        pub.setName(jsonObject.getString("name"));
                        pub.setAddress(jsonObject.getString("address"));

                        pubs.add(pub);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                PUBS_IDS = new int[pubs.size()];
                PUBS_NAMES = new String[pubs.size()];

                for(int i = 0 ; i < PUBS_IDS.length ; ++i){
                    PUBS_IDS[i] = pubs.get(i).getId();
                    PUBS_NAMES[i] = pubs.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.pub_dropdown, PUBS_NAMES);
                pubNameTextView = (AutoCompleteTextView) findViewById(R.id.pub_autocomplete);
                pubNameTextView.setAdapter(adapter);
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
}
