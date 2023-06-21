package err.ro.yourtourism3310project;

import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StepCountFragment extends Fragment implements SensorEventListener {

    SensorManager sensorManager = null;
    Boolean firstrun = false;
    SharedPreferences settings;

    Cursor cursor;
    int totalSteps = 0;
    int previousTotalSteps = 0;
    TextView steps_number;
    ImageButton instruction;
    RelativeLayout background;
    public StepCountFragment(){
    }

    Button add_1_step_btn,add_1000_step_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_count, container, false);
        Log.d("meD","onCreateView()");
        settings = getActivity().getSharedPreferences("Profile", 0);

        //debug tools
        /*add_1_step_btn = view.findViewById(R.id.add_1_step_btn);
        add_1_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalSteps += 1;
                steps_number.setText(""+(totalSteps - previousTotalSteps));
                updateBackground();
            }
        });
        add_1000_step_btn = view.findViewById(R.id.add_1000_step_btn);
        add_1000_step_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalSteps += 1000;
                steps_number.setText(""+(totalSteps - previousTotalSteps));
                updateBackground();
            }
        });*/

        background = view.findViewById(R.id.steps_background);
        steps_number = view.findViewById(R.id.steps_number);
        instruction = view.findViewById(R.id.instruction);
        instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogShow cdd = new DialogShow(getContext(), "Instruction",getContext().getResources().getString(R.string.steps_instruction));
                cdd.setCanceledOnTouchOutside(true);
                cdd.show();
            }
        });


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepSensor == null){
            Toast.makeText(getContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        }else{
            //sensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this,stepSensor,SENSOR_DELAY_FASTEST);
        }
        loadData();

        return view;
    }

    public void loadData() {

        // In this function we will retrieve data
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("stepMain", Context.MODE_PRIVATE);
        String currentDate = sharedPreferences.getString("currentDate", null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //step: today step, lastSteps: last step, currentDate: the record date of 'step'
        if(currentDate != null){
            if(!currentDate.equals(getToday())){
                //one day ago
                Log.d("meD", "one day ago");
                totalSteps = 0;
                previousTotalSteps = sharedPreferences.getInt("steps", 0);


                editor.putInt("steps", 0);
                editor.putInt("lastSteps", (int)previousTotalSteps);
                editor.putString("currentDate", getToday());
                editor.commit();
            }else{
                //same day
                Log.d("meD", "same day");
                totalSteps = sharedPreferences.getInt("steps", 0);
                previousTotalSteps = sharedPreferences.getInt("lastSteps", 0);
            }

        }else{
            //first run
            Log.d("meD", "first run");
            totalSteps = 0;
            previousTotalSteps = 0;
            firstrun = true;

            editor.putInt("steps", 0);
            editor.putInt("lastSteps", 0);
            //editor.putString("lastDate", currentDate);
            editor.putString("currentDate", getToday());
            editor.commit();
            Log.d("meD", "firstrun: "+firstrun);
        }
        Log.d("meD", "currentDate: "+currentDate);
        Log.d("meD", "totalSteps: "+totalSteps);
        Log.d("meD", "previousTotalSteps: "+previousTotalSteps);
        //DatabaseUtil.updateStepLog((totalSteps - previousTotalSteps));
        steps_number.setText(""+(totalSteps - previousTotalSteps));
        updateBackground();
    }
    public void saveData(boolean firstrun) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("stepMain", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("steps", totalSteps);
        editor.putString("currentDate", getToday());
        if(firstrun){
            editor.putInt("lastSteps", previousTotalSteps);
            Log.d("meD", "saveData = lastSteps: "+previousTotalSteps);
        }
        //editor.putInt("lastSteps", (int)totalSteps);
        //editor.putString("lastDate", getToday());
        editor.commit();
        Log.d("meD", "saveData = steps: "+totalSteps+", currentDate: "+getToday());

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        totalSteps = (int)event.values[0];
        if(firstrun){
            Log.d("meD", "firstrun is detected");
            if(totalSteps > 0){
                previousTotalSteps = totalSteps;
                firstrun = false;
                saveData(true);
                Log.d("meD", "firstrun is done");
            }

        }else{
            saveData(false);
        }

        Log.d("meD","totalSteps: "+totalSteps);
        /*Log.d("meD","previousTotalSteps: "+previousTotalSteps);
        // Current steps are calculated by taking the difference of total steps
        // and previous steps
        int currentSteps = (int)(totalSteps - previousTotalSteps);
        Log.d("meD","currentSteps: "+currentSteps);*/

        // It will show the current steps to the user
        DatabaseUtil.updateStepLog((totalSteps - previousTotalSteps));
        steps_number.setText(""+(totalSteps - previousTotalSteps));
        updateBackground();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




    
    public String getToday(){
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(today);
    }

    public void updateBackground(){
        int current = totalSteps - previousTotalSteps;
        if(current < 2000){
            background.setBackgroundColor(Color.parseColor("#fee2e2"));
        }else if(current < 4000){
            background.setBackgroundColor(Color.parseColor("#ffedd5"));
        }else if(current < 6000){
            background.setBackgroundColor(Color.parseColor("#fef3c7"));
        }else if(current < 8000){
            background.setBackgroundColor(Color.parseColor("#f7efc6"));
        }else{
            background.setBackgroundColor(Color.parseColor("#ecfccb"));
        }

    }




}
