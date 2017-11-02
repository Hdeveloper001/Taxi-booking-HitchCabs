package com.example.preet.hitchcabs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.preet.hitchcabs.Data.Common;
import com.example.preet.hitchcabs.Data.DataParser;
import com.example.preet.hitchcabs.Data.Locations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.preet.hitchcabs.Data.Common.pathline;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
//    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    DrawerLayout mDrawerLayout;
    Button btnMenu;
    ListView lv_locationList;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    boolean isPickupSelected = false;
    private boolean isStartLocationManuallSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btn_hitchCabs = findViewById(R.id.btn_hitchcabs);

        btn_hitchCabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RequestCabActivity.class));
            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btnMenu = findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });

        setNavigationViewListner();

        lv_locationList = findViewById(R.id.lv_homelocationlist);
        LocationListAdapter locationListAdapter = new LocationListAdapter();
        lv_locationList.setAdapter(locationListAdapter);

        lv_locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SharescoreContent.SharescoreItem sse = filteredList.get(position);
//                Allmacros.Current_SSE = sse;
//                txtLocation.setText(Allmacros.Current_SSE.title);

                isPickupSelected = position == 0;

                @SuppressLint("RestrictedApi") ContextThemeWrapper cw = new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogTheme);
                AlertDialog.Builder d = new AlertDialog.Builder(cw);
//                        d.setTitle("Create New")
//                        .setNegativeButton("Cancel", null)
                d.setItems(new String[]{"Enter Location", "Choose from Favorites", "Cancel"}, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dlg, int position)
                    {
                        if ( position == 0 )
                        {
                            try {
                                Intent intent =
                                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                                .build(MainActivity.this);
                                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                            } catch (GooglePlayServicesRepairableException e) {
                                // TODO: Handle the error.
                            } catch (GooglePlayServicesNotAvailableException e) {
                                // TODO: Handle the error.
                            }
                        }
                        else if(position == 1){
                            startActivity(new Intent(MainActivity.this, RequestCabActivity.class));

                        }
                        else if(position == 2){

                        }
                    }
                })
                        .create();
                d.show();

            }
        });

//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i("Selected", "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i("Not selected", "An error occurred: " + status);
//            }
//        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("Selected", "Place: " + place.getName());
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if (isPickupSelected){
                    isStartLocationManuallSelected = true;
                    Locations.startLocation.location = place.getAddress().toString();
                    Locations.startLocation.lat = Double.toString(place.getLatLng().latitude);
                    Locations.startLocation.lng = Double.toString(place.getLatLng().longitude);

//                    LatLng sydney = new LatLng(-34, 151);
                    if (Common.startMarker != null)
                        Common.startMarker.setPosition(place.getLatLng());
                    else
                        Common.startMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                }
                else{
                    Locations.endLocation.location = place.getAddress().toString();
                    Locations.endLocation.lat = Double.toString(place.getLatLng().latitude);
                    Locations.endLocation.lng = Double.toString(place.getLatLng().longitude);

                    if (Common.endMarker != null)
                        Common.endMarker.setPosition(place.getLatLng());
                    else
                        Common.endMarker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()));

//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
//                    builder.include(place.getLatLng());
                }
                if (!Locations.startLocation.lat.equals(""))
                    builder.include(new LatLng(Double.valueOf(Locations.startLocation.lat), Double.valueOf(Locations.startLocation.lng)));
                if (!Locations.endLocation.lat.equals(""))
                    builder.include(new LatLng(Double.valueOf(Locations.endLocation.lat), Double.valueOf(Locations.endLocation.lng)));

                LatLngBounds bounds = builder.build();

                int padding = 400; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.moveCamera(cu);
                if (Locations.startLocation.lat.equals("") || Locations.endLocation.lat.equals(""))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                else {

                    LatLng origin = new LatLng(Double.valueOf(Locations.startLocation.lat), Double.valueOf(Locations.startLocation.lng));
                    LatLng dest = new LatLng(Double.valueOf(Locations.endLocation.lat), Double.valueOf(Locations.endLocation.lng));

                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl fetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    fetchUrl.execute(url);

                    mMap.animateCamera(cu);
                }
                ((LocationListAdapter)lv_locationList.getAdapter()).notifyDataSetChanged();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Not Selected", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private void setNavigationViewListner() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void openDrawer(){

        if(mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_favorite: {
                //do somthing
                break;
            }
            case R.id.nav_contactus: {
                //do somthing
                break;
            }
            case R.id.nav_privacy: {
                //do somthing
                break;
            }
            case R.id.nav_terms: {
                //do somthing
                break;
            }
            case R.id.nav_logout: {
                //do somthing
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        Common.startMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }

        //Place current location marker
        if (!isStartLocationManuallSelected)
        {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            if (Common.startMarker != null)
                Common.startMarker.setPosition(latLng);
            else
                Common.startMarker = mMap.addMarker(markerOptions);

            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    class LocationListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.lv_locationcell,null);
            ImageView img_pin = convertView.findViewById(R.id.img_pin);
            ImageView img_process = convertView.findViewById(R.id.img_process);
//            CF.setImage(convertView.getContext(), filteredList.get(position).imgurl, img_cell, null, convertView.getContext().getResources().getDrawable(R.drawable.user_pic));
            TextView lb_title = convertView.findViewById(R.id.lb_locationtitle);
            TextView lb_location = convertView.findViewById(R.id.lb_location);
            if (position == 0) {
//                String uri = "@drawable/pickuppin";  // where myresource (without the extension) is the file
//                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                Drawable res = getResources().getDrawable(imageResource);
//                img_pin.setImageDrawable(res);
                img_pin.setBackgroundResource(R.drawable.pickuppin);
                img_process.setBackgroundResource(R.drawable.rect_end);
                lb_title.setText(R.string.str_pickuplocation);

                if (Locations.startLocation != null)
                    lb_location.setText(Locations.startLocation.location);
            }
            else{
                if (Locations.endLocation != null)
                lb_location.setText(Locations.endLocation.location);
            }
            return convertView;
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if (pathline != null)
                    pathline.setColor(Color.argb(0,255,255,255));
//                pathline.getPoints().clear();
                pathline = mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
}
