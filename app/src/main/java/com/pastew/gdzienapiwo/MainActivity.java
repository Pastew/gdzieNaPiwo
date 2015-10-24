package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ArrayAdapter<String> adapter;
    ArrayList<String> items;
    Button getBeersButton;
    Button pubButton;
    Button pubsButton;
    Button addBeerButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.listv);
        items=new ArrayList<String>();
        adapter=new ArrayAdapter(this, R.layout.item_layout,R.id.txt,items);
        listView.setAdapter(adapter);

        initUI();
    }

    private void initUI() {
        getBeersButton = (Button) findViewById(R.id.get_beers_button);
        pubButton = (Button) findViewById(R.id.pub_button);
        pubsButton = (Button) findViewById(R.id.pubs_button);
        addBeerButton = (Button) findViewById(R.id.add_beer_test_button);

        getBeersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        pubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PubActivity.class);
                i.putExtra("id", "1");
                startActivity(i);
            }
        });

        pubsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PubsActivity.class);
                startActivity(i);
            }
        });

        addBeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddBeerActivity.class);
                startActivity(i);
            }
        });
    }

    public void onStart(){
        super.onStart();
    }

    private void makeRequest() {
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest("http://www.gtwebsolutions.net/kent/getoutlet.php",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                // Successfully download json
                // So parse it and populate the listview
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        items.add(jsonObject.getString("owner_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
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

    public void gotoMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);

    }
}
