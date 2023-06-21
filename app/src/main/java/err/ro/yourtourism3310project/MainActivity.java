package err.ro.yourtourism3310project;

import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    StepCountFragment stepCountFragment;
    static MapFragment mapFragment;
    AttractionCardListFragment attractionCardListFragment;

    RecordFragment recordFragment;

    static BottomNavigationView bottomNavigationView;

    static FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(getApplicationContext().getResources().getString(R.string.app_name));

        fm = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        stepCountFragment = new StepCountFragment();
        mapFragment = new MapFragment();
        attractionCardListFragment = new AttractionCardListFragment();
        recordFragment = new RecordFragment();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);





        if (SDK_INT >= Build.VERSION_CODES.M) {
            boolean cameraHasGone = checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)
                    == PackageManager.PERMISSION_GRANTED;
            boolean externalHasGone = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
            boolean externalHasGone2 = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;


            String[] permissions;

            Log.d("meD","permission: "+cameraHasGone+","+externalHasGone+","+externalHasGone2);
            if(!cameraHasGone || !externalHasGone || !externalHasGone2){
                if (!cameraHasGone && !externalHasGone && !externalHasGone2) {
                    permissions = new String[3];
                    permissions[0] = Manifest.permission.ACTIVITY_RECOGNITION;
                    permissions[1] = Manifest.permission.ACCESS_FINE_LOCATION;
                    permissions[2] = Manifest.permission.ACCESS_COARSE_LOCATION;
                    Log.d("meD","permission: 1");
                } else if (!cameraHasGone) {
                    if(!externalHasGone){
                        permissions = new String[2];
                        permissions[0] = Manifest.permission.ACTIVITY_RECOGNITION;
                        permissions[1] = Manifest.permission.ACCESS_FINE_LOCATION;
                        //Log.d("meD","permission: no ACTIVITY_RECOGNITION");
                        Log.d("meD","permission: 2");
                    } else if (!externalHasGone2) {
                        permissions = new String[2];
                        permissions[0] = Manifest.permission.ACTIVITY_RECOGNITION;
                        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
                        Log.d("meD","permission: 3");
                    }else{
                        permissions = new String[1];
                        permissions[0] = Manifest.permission.ACTIVITY_RECOGNITION;
                        Log.d("meD","permission: 4");
                        //Log.d("meD","permission: no ACTIVITY_RECOGNITION");
                    }

                } else if (!externalHasGone) {
                    if(!externalHasGone2) {
                        permissions = new String[2];
                        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
                        permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
                        //Log.d("meD","permission: no location");
                        Log.d("meD","permission: 5");
                    }else{
                        permissions = new String[1];
                        permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
                        Log.d("meD","permission: 6");
                    }
                }else{
                    permissions = new String[1];
                    permissions[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
                    Log.d("meD","permission: 7");
                }
                requestPermissions(permissions, 100);
            }
        }

        DatabaseUtil.path = getFilesDir()+"/db";
        SharedPreferences settings = getSharedPreferences("Profile", 0);
        boolean ProfileExist = settings.getBoolean("ProfileExist", false);
        if(!ProfileExist) {
            Toast.makeText(this, "Welcome first use our app", Toast.LENGTH_SHORT).show();
            DatabaseUtil.init();

            settings.edit().putBoolean("ProfileExist", true).commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                fm.beginTransaction()
                        .replace(R.id.flFragment, stepCountFragment)
                        .commit();
                return true;

            case R.id.map:
                fm.beginTransaction()
                        .replace(R.id.flFragment, mapFragment)
                        .commit();
                return true;
            case R.id.insert:
                Intent i = new Intent(MainActivity.this,InsertActivity.class);
                startActivity(i);
                return false;
            case R.id.attractions:
                fm.beginTransaction()
                        .replace(R.id.flFragment, attractionCardListFragment)
                        .commit();
                return true;
            case R.id.record:
                fm.beginTransaction()
                        .replace(R.id.flFragment, recordFragment)
                        .commit();
                return true;

        }
        return false;
    }

    public static void refreshFragment(){
        Log.d("meD","refreshFragment");
        Fragment curr = fm.findFragmentById(R.id.flFragment);
        if (SDK_INT >= Build.VERSION_CODES.N) {
            fm.beginTransaction().detach(curr).commitNow();
            fm.beginTransaction().attach(curr).commitNow();
        } else {
            fm.beginTransaction().detach(curr).attach(curr).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFragment();
    }


}