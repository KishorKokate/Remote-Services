package com.example.servicesideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button start, stop;
    Intent serviceintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=findViewById(R.id.start_btn);
        stop=findViewById(R.id.stop_btn);

        serviceintent=new Intent(getApplicationContext(),MyService.class);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(serviceintent);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(serviceintent);
                Toast.makeText(MainActivity.this, "You need TO unbind Service first", Toast.LENGTH_SHORT).show();
            }
        });
    }
}