package jett_apps.grouvie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainLandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_landing_page);

        Button planBut = (Button) findViewById(R.id.PlanBut);
        planBut.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }



}
