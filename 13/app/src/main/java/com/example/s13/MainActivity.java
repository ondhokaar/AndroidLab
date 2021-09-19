package com.example.s13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button dashboardbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //findview
        dashboardbtn = (Button) findViewById(R.id.dashboardbtn);


        //intents
        Intent intent_dashboard = new Intent(this, dashboard.class);

        //setonclick
        dashboardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_dashboard);
            }
        });
    }




}