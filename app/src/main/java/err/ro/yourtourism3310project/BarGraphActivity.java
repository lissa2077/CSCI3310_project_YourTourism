package err.ro.yourtourism3310project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class BarGraphActivity extends AppCompatActivity {

    boolean stepTop = true;
    DrawingView d;
    FrameLayout frameLayout;
    Button chartModeButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        chartModeButton = findViewById(R.id.chart_mode_button);
        chartModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });

        d = new DrawingView(getApplicationContext());
        d.setter(DatabaseUtil.getStepLogTop5(),true);
        frameLayout = findViewById(R.id.frameLayout);
        frameLayout.addView(d);


    }

    public void switchMode(){
        stepTop = !stepTop;
        frameLayout.removeViewAt(0);
        if(stepTop){
            chartModeButton.setText("Step Counts Top 5");
            d = new DrawingView(getApplicationContext());
            d.setter(DatabaseUtil.getStepLogTop5(),true);
            frameLayout.addView(d);
        }else{
            chartModeButton.setText("Attractions Visit Top 5");
            d = new DrawingView(getApplicationContext());
            d.setter(DatabaseUtil.getAttractionsCountTop5(),false);
            frameLayout.addView(d);
        }
    }


}
