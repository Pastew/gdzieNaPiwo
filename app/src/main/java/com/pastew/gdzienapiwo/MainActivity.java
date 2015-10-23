package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends Activity {

    Button getButton;
    TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        resultTV = (TextView) findViewById(R.id.result_tv);
        getButton = (Button) findViewById(R.id.get_button);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePostRequest();
            }
        });
    }

    private void makePostRequest() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://mysterious-shelf-1380.herokuapp.com/beers";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        resultTV.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTV.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
