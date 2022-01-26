package com.vthree.rentbaseapplication.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.Adapter.EquipmentListAdapter;
import com.vthree.rentbaseapplication.Adapter.MyListAdapter;
import com.vthree.rentbaseapplication.MapsActivity;
import com.vthree.rentbaseapplication.ModelClass.EquipmentModel;
import com.vthree.rentbaseapplication.ModelClass.MyListData;
import com.vthree.rentbaseapplication.ModelClass.UserModel;
import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.preferences.PrefManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {


    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    List<EquipmentModel> list = new ArrayList<>();

    RecyclerView recyclerView;
    int flag = 0;
    EquipmentListAdapter adapter;
    SearchView editsearch;
    Location mLastLocation;
    LocationManager mLocationManager;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;
    FloatingActionButton show_map;
    SupportMapFragment mapFrag;
    Marker mCurrLocationMarker;
    Button btn_show;
    PrefManager prefManager;
    long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager=new PrefManager(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("EquipmentDetail").child("image");
        adapter = new EquipmentListAdapter(list, MainActivity.this);
        MyListData[] myListData = new MyListData[]{
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Dialer", android.R.drawable.ic_dialog_dialer),
                new MyListData("Alert", android.R.drawable.ic_dialog_alert),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
        };

        btn_show=(Button)findViewById(R.id.btn_showmap);
        show_map=findViewById(R.id.show_map);

     //   mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.serachmap);
     //   mapFrag.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }


        editsearch=(SearchView)findViewById(R.id.searchView);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_person_white);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        progressDialog = new ProgressDialog(MainActivity.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();



        adapter.OnItemClickArrayElement(new OnItemClickListenerArray());

        recyclerView.setAdapter(adapter);

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        buildGoogleApiClient();

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, MapNearByActivity.class);
                 startActivity(intent);
                Log.d("click","click");
            }
        });
        show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, MapNearByActivity.class);
                startActivity(intent);
                Log.d("click","click");
            }
        });

    }
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    boolean checkRadius(
            int radius,
            double centerLatitude ,
            double centerLongitud,
            double testLatitude,
            double testLongitude
    ){
        float[] results =new  float[1];
        Location.distanceBetween(centerLatitude, centerLongitud, testLatitude, testLongitude, results);
        float distanceInMeters = results[0];
        return distanceInMeters < radius;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mLastLocation=location;
            Log.d("location",mLastLocation.getLatitude()+"  :   "+mLastLocation.getLongitude());
        }
    };
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            this.mLocationRequest = new LocationRequest();
            this.mLocationRequest.setInterval(1000);
            this.mLocationRequest.setFastestInterval(1000);
            this.mLocationRequest.setPriority(102);
            if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
                LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
            }
        }catch (Exception e)
        {

        }

    }

    public void mapDialoge()
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_map);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.setTitle("Title...");
    }

    @Override
    public void onConnectionSuspended(int i) {

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();

            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);


                alertDialog.setTitle(R.string.app_name);


                alertDialog.setMessage("Do you want to exit?");


                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);
                        finishAffinity();

                    }
                });


                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_equipment) {

            Intent intent=new Intent(MainActivity.this,EquipmentRegisterActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_order) {
            Intent intent=new Intent(MainActivity.this,MyOrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_booked_order) {
            Intent intent=new Intent(MainActivity.this,BookedOrderActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_slideshow) {
            Intent intent=new Intent(MainActivity.this,AboutUs.class);
            startActivity(intent);
        } else  if(id==R.id.nav_logout){
            final AlertDialog builder=new AlertDialog.Builder(this).create();
            View view= LayoutInflater.from(this).inflate(R.layout.row_alert_logout,null);
            Button logout_btn_cancel=(Button)view.findViewById(R.id.logout_btn_cancel);
            Button logout_btn_ok=(Button)view.findViewById(R.id.logout_btn_ok);
            logout_btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
            logout_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    prefManager.setLogin(false);
                    startActivity(intent);

                    builder.dismiss();

                }
            });
            builder.setView(view);
            builder.setCanceledOnTouchOutside(true);
            builder.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!= null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            mLastLocation = location;
            Log.d("location88",mLastLocation.getLatitude()+"  :   "+mLastLocation.getLongitude());
        }


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
       // mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));

       /* CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(5000)
                .strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(Color.parseColor("#500084d3"));
        mGoogleMap.addCircle(circleOptions);*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }


    private class OnItemClickListenerArray implements EquipmentListAdapter.OnArrayItemClick {
        @Override
        public void setOnArrayItemClickListener(final int position, final String sellerID) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query key = ref.child("EquipmentDetail").child("image").orderByChild("equipment_id").equalTo(sellerID);
            key.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getEquipment_id() == sellerID) {
                                list.remove(i);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Log.d("position", "" + appleSnapshot.getKey());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("position", "onCancelled", databaseError.toException());
                }
            });
            Log.d("position11", "" + position + "   " + key + "  " + sellerID);
            //databaseReference.child("seller").child(key).removeValue();

        }
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        //mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            EquipmentModel model = snapshot.getValue(EquipmentModel.class);
                          //  latLng=getLocationFromAddress(model.getAddress());
                         //   Log.d("data2",mLastLocation.getLatitude()+" : "+mLastLocation.getLongitude()+" : "+latLng.latitude+" : "+latLng.longitude);
                           // boolean fenc= checkRadius(10000,mLastLocation.getLatitude(),mLastLocation.getLongitude(),latLng.latitude,latLng.longitude);
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (model.getEquipment_id().equals(list.get(i).getEquipment_id())) {
                                        flag = 1;
                                        Log.d("data2", model.getEquipment_name().toString());
                                    }
                                }
                                if (flag == 1) {
                                } else {
                                    flag = 0;
                                    //if (fenc==true) {
                                        list.add(model);
                                        Log.d("data", model.getEquipment_name().toString());
                                        Log.d("data", model.getContact().toString());
                                       /* MarkerOptions markerOptions = new MarkerOptions();
                                        markerOptions.position(latLng);
                                        markerOptions.title(model.getEquipment_name());

                                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);*/
                                   // }
                                }
                            } else {
                               // if (fenc==true) {
                                    list.add(model);
                                    Log.d("data11", model.getEquipment_name().toString());
                                    Log.d("data11", model.getContact().toString());
                                  //  progressDialog.dismiss();
                                 /*   MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title(model.getEquipment_name());

                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);*/
                               // }
                            }
                            //  Log.d("datas", model.getAddress().toString()+"  lat: "+mLastLocation.getLatitude()+"long: "+mLastLocation.getLongitude()+"  val: "+fenc);
                        }

                    } catch (Exception e) {
                        Log.d("dataqq", e.getMessage());
                    }

                    HashSet<EquipmentModel> hashSet = new HashSet<EquipmentModel>();
                    hashSet.addAll(list);
                    list.clear();
                    list.addAll(hashSet);
                    Log.d("size1", "" + list.size());

                    adapter.notifyDataSetChanged();
                }
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("data", databaseError.getMessage());
            }
        });


    }

}
