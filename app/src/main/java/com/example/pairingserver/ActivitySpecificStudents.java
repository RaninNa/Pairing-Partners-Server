package com.example.pairingserver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySpecificStudents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_students);

        Button btnCont =  (Button) findViewById(R.id.btnCont);
        final Spinner spinnerFaculty =  (Spinner) findViewById(R.id.spinnerFaculty);
        final Spinner spinnerCourses =  (Spinner) findViewById(R.id.spinnerCourses);
        final Spinner spinnerType =  (Spinner) findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.faculty_array, android.R.layout.simple_spinner_item);
        spinnerFaculty.setAdapter(adapter);
        spinnerFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getContext(), "this is " + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.courses_array, android.R.layout.simple_spinner_item);
        spinnerCourses.setAdapter(adapter2);
        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getContext(), "this is " + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        spinnerType.setAdapter(adapter3);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getContext(), "this is " + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.faculty=spinnerFaculty.getSelectedItem().toString();
                Globals.course=spinnerCourses.getSelectedItem().toString();
                Globals.workType=spinnerType.getSelectedItem().toString();
                Intent intent = new Intent(ActivitySpecificStudents.this, GetStudents.class);
                startActivity(intent);
            }
        });
    }
}