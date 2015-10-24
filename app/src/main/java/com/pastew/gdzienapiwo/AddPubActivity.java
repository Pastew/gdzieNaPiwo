package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class AddPubActivity extends Activity {

    private LinearLayout addPubPerksLL;
    private Button addPubButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pub);

        addPubButton = (Button) findViewById(R.id.add_pub_button);
        addPubPerksLL = (LinearLayout)findViewById(R.id.add_pub_perks_ll);

        for(int i = 0 ; i < Global.PERKS.length ; ++i) {
            CheckBox perk = new CheckBox(this);
            perk.setText(Global.PERKS[i]);
            perk.setChecked(false);
            addPubPerksLL.addView(perk);
        }

        addPubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPub();
            }
        });
    }

    private void addPub(){

        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        JSONObject params = new JSONObject();
        try {
            params.put("name", ((EditText)findViewById(R.id.add_pub_name_et)).getText().toString());
            params.put("address", ((EditText)findViewById(R.id.add_pub_address_et)).getText().toString());

            for(int i = 0 ; i < addPubPerksLL.getChildCount() ; ++i){
                CheckBox cb = (CheckBox)addPubPerksLL.getChildAt(i);
                params.put(cb.getText().toString(), cb.isChecked() );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject param = new JSONObject();
        try{
            param.put("pub", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://mysterious-shelf-1380.herokuapp.com/pubs", param, new Response.Listener<JSONObject>(){
            public void onResponse(JSONObject response){
                Toast.makeText(getApplicationContext(), "Dodalem pub", Toast.LENGTH_LONG).show();
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


}
