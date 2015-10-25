package com.pastew.gdzienapiwo;


import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    double closestDistance=10000000;
    double a,b;
    Bitmap bitmap;

    public static final String TAG = MapsActivity.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds







    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    public void onSearch()
    {

        getPubs();

        //closestMark(markerOptionsList, closestMarker);
    }



    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
         Bitmap  bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
     //   mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        Log.d("Lokacja", location.toString());

        // conversion to simple coordinate
        String goodFormatLocation=locationStringFromLocation(location);
        Log.d("Lokacja22", goodFormatLocation);


        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        a=currentLatitude;
        b=currentLongitude;

        this.onSearch();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);


        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));


    }

    @Override
    public void onConnected(Bundle bundle) {
         Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    // conversion to simple coordinate
    public  String locationStringFromLocation(final Location location) {
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " " + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }

    public void closestMark(List<MarkerOptions> markerOptionsList,  View closestMarker) {



        int R;
        double x,y, distance;
//        double currentLatitude = tempLocation.getLatitude();
//        double currentLongitude = tempLocation.getLongitude();
//
//        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        LatLng latLngList;

        int closestNumber=0;

        Log.i("closestNumber", "wbijam do obliczania najblizszego");

        for(int i=0; i<markerOptionsList.size(); i++){

            latLngList=markerOptionsList.get(i).getPosition();

            R = 6371; // km
            x = (latLngList.longitude - b) * Math.cos((a + latLngList.latitude) / 2);
            y = (latLngList.latitude - a);
            distance = Math.sqrt(x * x + y * y) * R;

            Log.i("distance", ""+distance);

            if(distance<closestDistance){
                closestNumber=i;

                closestDistance=distance;
                Log.i("closestNumber", ""+i);
            }

        }


        markerOptionsList.get(closestNumber).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, closestMarker)));

        mMap.addMarker(markerOptionsList.get(closestNumber));


        Log.i("ab"," "+a);
        Log.i("ab", " " + b);
    }

    ArrayList<Pub> pubs;
    public void getPubs(){

         pubs = new ArrayList<>();

        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        // todo
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest("https://mysterious-shelf-1380.herokuapp.com/pubs/nearby/?x="+a+"&y="+b,new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){

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
                populateMarks();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array");
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void populateMarks() {

        //******************************************* snippet several line settings ****************
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context mContext = getApplicationContext();

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);


                return info;
            }
        });

        //********************************************* Ustawienia MARKERA.png **************************
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        TextView markerText=(TextView) marker.findViewById(R.id.marker_text);


       // int height_in_pixels = markerText.getLineCount() * markerText.getLineHeight(); //approx height text
        //   markerText.setHeight(30);

        //**********************************************************************************************************


        int priceParser;

        List<MarkerOptions> markerOptionsList=new ArrayList<>();
        //List<String> location=new ArrayList<>();

        if(!pubs.isEmpty())
        {
            List<Address> addressList;
            Geocoder geocoder = new Geocoder(this);
            try {
                int j = 0;
                for(int i=0; i<pubs.size(); i++) {

                    priceParser=(int)pubs.get(i).getBeerPrice();

                    if(pubs.get(i).getBeerPrice()!=0.0) {

                        if(priceParser==pubs.get(i).getBeerPrice()){
                            markerText.setText(priceParser+"");
                        }
                        else
                        markerText.setText(pubs.get(i).getBeerPrice() + "");
                    }
                    else
                        markerText.setText("-");

                    bitmap=createDrawableFromView(this,marker);
                    BitmapDescriptor bitmapDescriptor=BitmapDescriptorFactory.fromBitmap(bitmap);

                    addressList = geocoder.getFromLocationName(pubs.get(i).getAddress(), 1);
                    if(addressList.size()==0)
                        continue;

                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());


                    if(pubs.get(i).getBeerPrice()!=0.0) {


                        markerOptionsList.add(j, new MarkerOptions().position(latLng).title("" + pubs.get(i).getBeerPrice()).snippet("" + pubs.get(i).getName() + "\n" +
                                pubs.get(i).getAddress() + "\n")
                                .icon(bitmapDescriptor));

                    }

                    else {

                        markerOptionsList.add(j, new MarkerOptions().position(latLng).title("Badz pierwszy i dodaj cene piwka!").snippet("" + pubs.get(i).getName() + "\n" +
                                pubs.get(i).getAddress() + "\n")
                                .icon(bitmapDescriptor));

                    }


                    mMap.addMarker(markerOptionsList.get(j));



                    // markerOptionsList.get(i).icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, closestMarker)));

                    // mMap.addMarker(markerOptionsList.get(i));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    j++;


                    if(j==10) return;

                   // mMap.clear();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
