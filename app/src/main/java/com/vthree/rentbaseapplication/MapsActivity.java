package com.vthree.rentbaseapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vthree.rentbaseapplication.Activity.EquipmentRegisterActivity;
import com.vthree.rentbaseapplication.Adapter.Constants;
import com.vthree.rentbaseapplication.Adapter.DirectionsJSONParser;

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
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    double destlang;
    double destlat;
    double lat;
    double lng;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
   //GoogleMap mMap;
    MarkerOptions markerOptions1;
    TextView MarkerAddress;
    Button button;
    double x, y;
    StringBuilder strReturnedAddress;

    private class DownloadTask extends AsyncTask<String, Void, String> {
        private DownloadTask() {
        }

        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = MapsActivity.this.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new MapsActivity.ParserTask().execute(new String[]{result});
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        private ParserTask() {
        }

        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            List<List<HashMap<String, String>>> routes = null;
            try {
                routes = new DirectionsJSONParser().parse(new JSONObject(jsonData[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            for (int i = 0; i < result.size(); i++) {
                ArrayList points = new ArrayList();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = (List) result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = (HashMap) path.get(j);
                    points.add(new LatLng(Double.parseDouble((String) point.get("lat")), Double.parseDouble((String) point.get("lng"))));
                }
                lineOptions.addAll(points);
                lineOptions.width(12.0f);
                lineOptions.color(SupportMenu.CATEGORY_MASK);
                lineOptions.geodesic(true);
            }
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        MarkerAddress = (TextView) findViewById(R.id.txt_addresss);
        button = (Button) findViewById(R.id.dashboardButton);
        this.destlat = 19.950950;
        this.destlang = 73.824690;
        if (Build.VERSION.SDK_INT >= 23) {
            checkLocationPermission();
        }
        //((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 99);
            return false;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 99);
        return false;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(strUrl).openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String str = "";
            while (true) {
                str = br.readLine();
                if (str == null) {
                    break;
                }
                sb.append(str);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();
        for (int i = 0; i < result.size(); i++) {
            ArrayList points = new ArrayList();
            lineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = (List) result.get(i);
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = (HashMap) path.get(j);
                points.add(new LatLng(Double.parseDouble((String) point.get("lat")), Double.parseDouble((String) point.get("lng"))));
            }
            lineOptions.addAll(points);
            lineOptions.width(12.0f);
            lineOptions.color(SupportMenu.CATEGORY_MASK);
            lineOptions.geodesic(true);
        }
        if (lineOptions != null) {
            mMap.addPolyline(lineOptions);
        }
    }




        /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMapType(1);
        if (Build.VERSION.SDK_INT < 23) {
            buildGoogleApiClient();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            this.mMap.setMyLocationEnabled(true);
        } else if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            buildGoogleApiClient();
            this.mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        this.mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setInterval(1000);
        this.mLocationRequest.setFastestInterval(1000);
        this.mLocationRequest.setPriority(102);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            LocationServices.FusedLocationApi.requestLocationUpdates(this.mGoogleApiClient, this.mLocationRequest, (LocationListener) this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLastLocation = location;
        if (this.mCurrLocationMarker != null) {
            this.mCurrLocationMarker.remove();
        }
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.markerfour));

        // Toast.makeText(MapCurrentActivity.this,"LatLong"+latLng,Toast.LENGTH_SHORT).show();
        // String provider = ((LocationManager) getSystemService("location")).getBestProvider(new Criteria(), true);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(300.0f));
            this.mCurrLocationMarker = this.mMap.addMarker(markerOptions);
            this.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            this.mMap.animateCamera(CameraUpdateFactory.zoomTo(100.0f));
            if (this.mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, (LocationListener) this);
            }
            String url = getDirectionsUrl();
            new DownloadTask().execute(new String[]{url});
            drawMarker(new LatLng(this.lat, this.lng), new LatLng(this.destlat, this.destlang));
        }
    }

    private String getDirectionsUrl() {

        String str_origin = "origin=" + this.lat + "," + this.lng;
        return "https://maps.googleapis.com/maps/api/directions/" + "json" + "?" + (str_origin + "&" + ("destination=" + this.destlat + "," + this.destlang) + "&" + "sensor=false" + "&" + "mode=driving") + "&key=AIzaSyAolmn5QHW4BY2Et-0Qz5DhAgiOOjzFE3o";

    }

    public void drawMarker(final LatLng source_point, LatLng destination_point) {
        markerOptions1 = new MarkerOptions();
        markerOptions1.title("Source");
        markerOptions1.snippet("Current Location" + source_point);
        markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.markerfour));
        markerOptions1.position(source_point);
        markerOptions1.draggable(true);
        this.mMap.addMarker(markerOptions1);
        this.mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(final Marker marker) {
                Log.d("", "latitude : " + marker.getPosition().latitude + " " + marker.getPosition().longitude);
                marker.setSnippet("latitude" + marker.getPosition().latitude + " " + marker.getPosition().longitude);
                MarkerAddress.setText("latitude" + marker.getPosition().latitude + " " + marker.getPosition().longitude);
                      /*  x=marker.getPosition().latitude;
                        y=marker.getPosition().longitude;*/
                Constants.Lat=marker.getPosition().latitude;
                Constants.Long= marker.getPosition().longitude;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                getCompleteAddressString(marker.getPosition().latitude, marker.getPosition().longitude);

                // cancelOrder(String.valueOf(marker.getPosition().latitude),String.valueOf(marker.getPosition().longitude));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapsActivity.this, EquipmentRegisterActivity.class);
                        String value = strReturnedAddress.toString();
                        intent.putExtra("value", value);
                        intent.putExtra("x", x);
                        intent.putExtra("y", y);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }
        });
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction ", strReturnedAddress.toString());
                MarkerAddress.setText(strReturnedAddress.toString());

            } else {
                Log.w("My Current loction ", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction ", "Canont get Address!");
        }
        return strAdd;
    }


}
