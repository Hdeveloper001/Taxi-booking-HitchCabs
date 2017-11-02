package com.example.preet.hitchcabs;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import com.example.preet.hitchcabs.Data.Locations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.PolylineOptions;

public class RequestCabActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    DrawerLayout mDrawerLayout;
    Button btnMenu;
    ListView lv_locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_cab);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnMenu = (Button)findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });

        setNavigationViewListner();

        Button btn_back = (Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lv_locationList = (ListView)findViewById(R.id.lv_requestlocationlist);
        RequestListAdapter requestListAdapter = new RequestListAdapter();
        lv_locationList.setAdapter(requestListAdapter);

        lv_locationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SharescoreContent.SharescoreItem sse = filteredList.get(position);
//                Allmacros.Current_SSE = sse;
//                txtLocation.setText(Allmacros.Current_SSE.title);
            }
        });
    }

    private void setNavigationViewListner() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (!Locations.startLocation.lat.equals("")) {
            builder.include(new LatLng(Double.valueOf(Locations.startLocation.lat), Double.valueOf(Locations.startLocation.lng)));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(Locations.startLocation.lat), Double.valueOf(Locations.startLocation.lng))));
        }
        if (!Locations.endLocation.lat.equals("")) {
            builder.include(new LatLng(Double.valueOf(Locations.endLocation.lat), Double.valueOf(Locations.endLocation.lng)));
            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(Locations.endLocation.lat), Double.valueOf(Locations.endLocation.lng))));
        }
        LatLngBounds bounds = builder.build();

        int padding = 400; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        if (Locations.startLocation.lat.equals("") || Locations.endLocation.lat.equals(""))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        else {
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(Common.pathline.getPoints());
            lineOptions.width(20);
            lineOptions.color(Color.RED);
            mMap.addPolyline(lineOptions);

            mMap.animateCamera(cu);
        }
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
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    class RequestListAdapter extends BaseAdapter {

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
            ImageView img_pin = (ImageView) convertView.findViewById(R.id.img_pin);
            ImageView img_process = (ImageView) convertView.findViewById(R.id.img_process);
//            CF.setImage(convertView.getContext(), filteredList.get(position).imgurl, img_cell, null, convertView.getContext().getResources().getDrawable(R.drawable.user_pic));
            TextView lb_title = (TextView) convertView.findViewById(R.id.lb_locationtitle);
            if (position == 0) {
//                String uri = "@drawable/pickuppin";  // where myresource (without the extension) is the file
//                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//                Drawable res = getResources().getDrawable(imageResource);
//                img_pin.setImageDrawable(res);
                img_pin.setBackgroundResource(R.drawable.pickuppin);
                img_process.setBackgroundResource(R.drawable.rect_end);

                lb_title.setText(R.string.str_pickuplocation);
            }
            return convertView;
        }
    }
}
