package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


public class AddBeerActivity extends Activity {

    Button addBeerButton;
    TextView pubNameTextView;
    TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        initUI();
    }

    private void initUI() {
        addBeerButton = (Button) findViewById(R.id.add_beer_button);
        pubNameTextView = (TextView)findViewById(R.id.pub_edit_text);
        priceTextView = (TextView)findViewById(R.id.price_edit_text);

        addBeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pubId = getPubId(pubNameTextView.getText().toString());
                float price = Float.parseFloat(priceTextView.getText().toString());
                addBeer(pubId, price);
            }
        });
    }

    private void addBeer(int pubId, float price){
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
                Toast.makeText(getApplicationContext(), "Dodalem piwko", Toast.LENGTH_LONG).show();
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
        return 39;
    }
}
