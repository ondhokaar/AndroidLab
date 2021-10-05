package com.example.s13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.s13.emergency.doctor;

public class EmergencyActivity extends AppCompatActivity {
    private LottieAnimationView lottiePolice, lottieDoctor, lottieAmbulance, lottiePharmacy, lottie999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        lottieAmbulance = (LottieAnimationView) findViewById(R.id.ambulance);
        lottieDoctor = (LottieAnimationView) findViewById(R.id.doctor);
        lottiePharmacy = (LottieAnimationView) findViewById(R.id.pharmacy);
        lottiePolice = (LottieAnimationView) findViewById(R.id.police);
        lottie999 = (LottieAnimationView) findViewById(R.id.lottie_999);

        lottie999.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:999"));
                startActivity(intent);
            }
        });

        lottiePolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open Police contact activity
                Bundle bundle = new Bundle();
                bundle.putString("service", "Police");
                startActivity(new Intent(getApplicationContext(), doctor.class).putExtras(bundle));
            }
        });
        lottiePharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("service", "Pharmacy");
                startActivity(new Intent(getApplicationContext(), doctor.class).putExtras(bundle));
            }
        });

        lottieAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("service", "Ambulance");
                startActivity(new Intent(getApplicationContext(), doctor.class).putExtras(bundle));
            }
        });

        lottieDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("service", "Doctors");
                startActivity(new Intent(getApplicationContext(), doctor.class).putExtras(bundle));
            }
        });

    }
}