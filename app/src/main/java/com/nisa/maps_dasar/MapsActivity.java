package com.nisa.maps_dasar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    // ini cara memanggil global

    Button btnNormal, btnHybird, btnTerrain, btnSatellit, btnSearch, btnMy, btnDirection;
    String nama_lokasi;
    TextView txt;

    //mengimport class
    GPSTracker gps ;
    DirectionMapsV2 directionMapsV2;
    AQuery aq;

    int PLACE_AUTO = 1 ;


    Double latitude , longtitude; // kenapa pake double karena pake desimal /pake angka-angka dan koma-koman
    Double lat , lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // buat method

        setView();
    }

    private void setView() {
        //inisialisasi
        btnNormal = (Button) findViewById(R.id.btnNormal);
        btnHybird = (Button) findViewById(R.id.btnHybird);
        btnTerrain = (Button) findViewById(R.id.btnTerrain);
        btnSatellit = (Button) findViewById(R.id.btnSatelit);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnMy = (Button) findViewById(R.id.btnMy);
        btnDirection = (Button) findViewById(R.id.btnDirection);


        txt = (TextView) findViewById(R.id.text);

        btnNormal.setOnClickListener(this);
        btnHybird.setOnClickListener(this);
        btnTerrain.setOnClickListener(this);
        btnSatellit.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnMy.setOnClickListener(this);
        btnDirection.setOnClickListener(this);

        directionMapsV2 = new DirectionMapsV2(MapsActivity.this);
        aq = new AQuery(MapsActivity.this);
        gps = new GPSTracker(MapsActivity.this);

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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(1.649049, 101.405385);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Dumai"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {



                LatLng posisi  = cameraPosition.target;
                lat = posisi.latitude;
                lon = posisi.longitude;

                txt.setText(String.valueOf(lat)+ "," + String.valueOf(lon));


                }
        }); // pas kamera di gerak-gerakin ada yang terjadi

    }

    @Override
    public void onClick(View view) {

        //kalau else itu pilihanetrakhir , kalau ada if nya berarti masih ada condisinya
        if (view == btnNormal){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (view == btnHybird){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (view == btnSatellit){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else if (view == btnTerrain){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }else if (view == btnSearch){
            ActionSearch();
        } else if ( view == btnSearch){
            ActionSearch();
        } else if (view == btnMy) {
            ActionMyLoc();
        }

    }

    private void ActionMyLoc() {

        // permission untuk versi marshmellow keatas
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION} , 100);

        }
        //buat nemui lokasi kita
        if (gps.canGetLocation()) {
            //get koordinat gps
            latitude = gps.getLatitude();
            longtitude = gps.getLongitude();

            //set location use LatLang
            LatLng posisiku = new LatLng(latitude, longtitude);

            //detect name location based coordinat
            Geocoder geocoder = new Geocoder(this, Locale.getDefault()); // apa itu geocorder ini buat ngediteksi nama lokasi
            //apa itu locale mencangkup language , country (region) dll

            //include  koordinat use list<adress>
            try{
                List<Address> addresses = geocoder.getFromLocation(latitude , longtitude , 1);
                 nama_lokasi = addresses.get(0).getFeatureName(); // kalau array index nya dimulai dari 0
            } catch (IOException e) {
                    e.printStackTrace();
            }

            //menghapu marker sebelummnya
            mMap.clear();

            // set marker posisi kita (my location )
            mMap.addMarker(new MarkerOptions().position(posisiku).title(nama_lokasi)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)); // hue ini untuk warna panah lokasi

            //set fokus kamera , jadi kameranya di fokusin di tempat kita

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiku, 15));// yang angka 15 nge set jaraknya

        }
        else {

            // kalau gps gak aktif
            Helper.showSettingGps(MapsActivity.this);
        }

    }

    private void ActionSearch() {

        Intent i = null;
        try {
            //(placeautocomplete) kalau waktu ngeseacrh akan keluar list temapt yang diprediksi akan dipilih atau bisa juga  yang berhubungan atau yang mau dicari
            // kalau intent builder pindahnya itu langsung buat ngesearch tapi bukan buat class baru

            i = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MapsActivity.this);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        startActivityForResult(i, PLACE_AUTO);

    }

    //method ini digunakan untuk menangkap data pengembalian
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check data pengembalian
        if (requestCode == PLACE_AUTO) {
            if (resultCode == RESULT_OK) {

                //get namelocation , coordinate from use into auto complate search google
                Place place = PlaceAutocomplete.getPlace(this, data); // data ini dari intent data

                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;

                //get name location
                //jadi cahrquence adalah sebuah interface dan didalamnya ada string
                // jadi string itu mengimplement charsquen jadi bisa disebut charsquence itu ibu
                // kenapa pake charsquence karna getadress ini type datanya charsquence
                CharSequence name  = place.getAddress();

                //convert char ke string
                String name2 = name.toString();

                LatLng posisi = new LatLng(lat,lon);

                //menghapus marker sebelumnya
                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(posisi).title(name2)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi,15));

                CreateDirection(lat,lon);

            }

            else {
                Helper.showSettingGps(MapsActivity.this);
            }
        }
    }

    private void CreateDirection(final Double lat, final Double lon) {
        //convert to string
        String var_lat = String.valueOf(latitude);
        String var_lon = String.valueOf(longtitude);
        String var_la1 = String.valueOf(lat);
        String var_lon1 = String.valueOf(lon);

        String origin = var_lat + "," + var_lon; //origin itu berarti asal nya dari mana artinya
        String tujuan = var_la1 + "," + var_lon1;

        String url = "https://maps.googleapis.com/maps/api/" + "directions/json?origin=" + origin + "&destination=" + tujuan + "&sensor=true";

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        aq.progress(progressDialog).ajax(url , String.class, new AjaxCallback<String>(){
            @Override
            public void callback(String url, String object, AjaxStatus status) {

                if (object != null){

                    //get json
                    try {
                        JSONObject json = new JSONObject(object);
                        Helper.pre("respon route" + object); // salah satu method di helper untuk menampilkan pesan

                        JSONArray var_array = json.getJSONArray("routes");
                        JSONObject obj = var_array.getJSONObject(0);
                        JSONObject poly = obj.getJSONObject("overview_polyline");

                        LatLng posisia = new LatLng(latitude, longtitude);
                        LatLng posisib = new LatLng(lat, lon);

                        mMap.addMarker(new MarkerOptions().position(posisia));
                        mMap.addMarker(new MarkerOptions().position(posisib));

                        //set fokus
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisib, 10));

                        //untuk buat garis di map nya kayak dari titik 1 ke -  titik 2
                        directionMapsV2.gambarRoute(mMap, poly.getString("points"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
