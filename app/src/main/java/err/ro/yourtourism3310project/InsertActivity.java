package err.ro.yourtourism3310project;

import static java.security.AccessController.getContext;

import static err.ro.yourtourism3310project.DatabaseUtil.getToday;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

public class InsertActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    EditText lat,lng,name,experience;
    RatingBar rating;
    Button submitBtn,cancelBtn;

    boolean init = true;
    Attraction a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setTitle("Attraction");

        name = findViewById(R.id.place_name);
        experience = findViewById(R.id.experience);
        rating = findViewById(R.id.rating);

        lat = findViewById(R.id.lat);
        lng = findViewById(R.id.lng);

        Intent i = getIntent();
        if(i.getExtras() != null){
            a = new Attraction();
            a.name = i.getStringExtra("title");
            name.setText(a.name);
            a.experience = i.getStringExtra("experience");
            experience.setText(a.experience);
            a.rating = i.getFloatExtra("rating",3.0f);
            rating.setRating(a.rating);
            a.lat = i.getDoubleExtra("lat",0.0);
            lat.setText(a.lat+"");
            a.lng = i.getDoubleExtra("lng",0.0);
            lng.setText(a.lng+"");
            a.attid = i.getIntExtra("attrid",-1);
            a.date = i.getStringExtra("date");
            Log.d("meD","a.name: "+a.name);
            Log.d("meD","a.experience: "+a.experience);
            Log.d("meD","a.rating: "+a.rating);
            Log.d("meD","a.lat: "+a.lat);
            Log.d("meD","a.lng: "+a.lng);
            Log.d("meD","a.attid: "+a.attid);
        }

        submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.insert_map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MapUtil.setMyLocationEnabled(this,mMap,true);

        LatLng default_point = new LatLng(22.302711, 114.177216);
        if(a != null) {
            //update
            default_point = new LatLng(a.lat, a.lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(default_point);
            googleMap.addMarker(markerOptions);
        }else{
            GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {

                    if(init) {
                        //create, get once current location
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(latLng);
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,19));
                        googleMap.addMarker(markerOptions);
                        lat.setText(location.getLatitude() + "");
                        lng.setText(location.getLongitude() + "");

                        init = false;
                    }
                }
            };
            mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(default_point, 17));





        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,19));
                googleMap.addMarker(markerOptions);
                lat.setText(latLng.latitude+"");
                lng.setText(latLng.longitude+"");

            }
        });

    }

    public void submit(){
        String name2 = name.getText().toString();
        if(name2.equals("")){
            Toast.makeText(this, "Attraction name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String experience2 = experience.getText().toString();
        if(experience2.equals("")){
            Toast.makeText(this, "Experience cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        float rating2 = rating.getRating();
        double lat2 = Double.parseDouble(lat.getText().toString());
        double lng2 = Double.parseDouble(lng.getText().toString());

        Log.d("meD","name: "+name2);
        Log.d("meD","experience: "+experience2);
        Log.d("meD","rating: "+rating2);
        Log.d("meD","lat: "+lat2);
        Log.d("meD","lng: "+lng2);

        Attraction submit_att = new Attraction(name2,rating2,experience2,lat2,lng2);
        if(a != null && a.attid > -1){
            submit_att.attid = a.attid;
            submit_att.date = a.date;
            Log.d("meD","attid: "+a.attid+", date: "+a.date);

        }
        DatabaseUtil.insertAttraction(submit_att);

        this.finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder ab = new AlertDialog.Builder(InsertActivity.this);
        ab.setMessage("Those information will not be save.\nAre you sure to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        ab.show();


    }

}
