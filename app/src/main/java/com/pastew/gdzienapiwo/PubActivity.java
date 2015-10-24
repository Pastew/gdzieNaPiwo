package com.pastew.gdzienapiwo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class PubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("id");

        Pub pub = getPub(id);
        ((TextView)findViewById(R.id.pub_name)).setText(pub.getName());
    }

    private Pub getPub(int id) {
        return new Pub();
    }


}
