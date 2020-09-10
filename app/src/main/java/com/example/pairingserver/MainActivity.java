package com.example.pairingserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSpecific = (Button) findViewById(R.id.btnSpecificMatching);
        Button btnGlobalMatch = (Button) findViewById(R.id.btnGlobalMatching);

        btnSpecific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.MatchType = 0;
                Intent intent = new Intent(MainActivity.this, ActivitySpecificStudents.class);
                startActivity(intent);
            }
        });
        btnGlobalMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.MatchType = 1;
                Intent intent = new Intent(MainActivity.this, GetAllStudents.class);
                startActivity(intent);
            }
        });
     }
}