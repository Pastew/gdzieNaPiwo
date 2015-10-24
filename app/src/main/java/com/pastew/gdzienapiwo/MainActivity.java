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
    Button pubsButton;
    Button addBeerButton;
    Button addPubButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        addBeerButton = (Button) findViewById(R.id.add_beer_test_button);
        addPubButton = (Button) findViewById(R.id.add_pub_test_button);
        pubsButton = (Button) findViewById(R.id.pubs_button);
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

        addPubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddPubActivity.class);
                startActivity(i);
            }
        });
    }

    public void onStart(){
        super.onStart();
    }

    public void gotoMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        startActivity(intent);

    }
}
