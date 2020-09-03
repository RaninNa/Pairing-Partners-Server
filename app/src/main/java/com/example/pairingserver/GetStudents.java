package com.example.pairingserver;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class GetStudents extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_students);

        final TextView textViewRes = (TextView) findViewById(R.id.TVRes);
        final TextView pairing_results = (TextView) findViewById(R.id.pairingResults);
        final Button btnMatch = (Button) findViewById(R.id.btnMatch);
        final int[][] scores_;
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // HERE YOU CAN RUN THE ALGORITHM
                HungarianAlgorithm ha = new HungarianAlgorithm(Globals.pairs_scores);
                int[][] assignment = ha.findOptimalAssignment();

                String res_string = " ";
                if (assignment.length > 0) {
                    // print assignment
                    for (int i = 0; i < assignment.length; i++) {
                        res_string +=  Globals.students[assignment[i][0]].getName() + " => " + Globals.students[assignment[i][1]].getName() + "\n";
                    }
                } else {
                    System.out.println("no assignment found!");
                }

                pairing_results.setText(res_string);
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //JSONObject jsonResponse = new JSONObject(response);
                    if (response.indexOf("success") >= 0) {


                        JSONArray jsonResponse = new JSONArray(response);
                        boolean success = jsonResponse.getJSONObject(0).getBoolean("success");
                        response = "[" + response.substring(19);
                        JSONArray jsonData = new JSONArray(response);
                        //jsonData.length()
                        int ArrayStudentsCount = jsonData.length();
                        Globals.students = new Student[ArrayStudentsCount];
                       /*
                        for (int i = 0; i < Globals.events.getCalendarEvents().length; i++)
                            Globals.events.getCalendarEvents()[i] = new YearEvents(12);
                        for (int d = 0; d < Globals.events.getCalendarEvents().length; d++) {
                            for (int i = 0; i < 12; i++) {
                                Globals.events.getCalendarEvents()[d].getYearevents()[i] = new MonthEvents(31);
                                for (int c = 0; c < 31; c++) {
                                    Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c] = new DayEvents(10);
                                }
                            }
                        }*/
                        //String Resu = jsonData.toString();
                        if (success) {


                            //Globals.ArrayEvents = new Event[ArrayEventsCount];
                            int index = 0;
                            if (!jsonData.toString().equals("[null]")) {
                                for (int i = 0; i < jsonData.length(); i++) {
                                    int id = jsonData.getJSONObject(i).getInt("id");
                                    String Name = jsonData.getJSONObject(i).getString("Name");
                                    String Gender = jsonData.getJSONObject(i).getString("Gender");
                                    String Location = jsonData.getJSONObject(i).getString("Location");
                                    int Age = jsonData.getJSONObject(i).getInt("Age");
                                    String Phone = jsonData.getJSONObject(i).getString("Phone");
                                    String Year = jsonData.getJSONObject(i).getString("Year");
                                    String Email = jsonData.getJSONObject(i).getString("Email");
                                    int GradeAverage = jsonData.getJSONObject(i).getInt("GradeAverage");
                                    String WorkPlan = jsonData.getJSONObject(i).getString("WorkPlan");
                                    String Meeting = jsonData.getJSONObject(i).getString("Meeting");
                                    String PrefGen = jsonData.getJSONObject(i).getString("PrefGen");
                                    String WorkHours = jsonData.getJSONObject(i).getString("WorkHours");
                                    boolean ILocation = (jsonData.getJSONObject(i).getString("ILocation")).equals("1");
                                    boolean IGrade = (jsonData.getJSONObject(i).getString("IGrade")).equals("1");

                                    //String Type = jsonData.getJSONObject(i).getString("Type");


                                    Student student = new Student(Name, Location, Email, Phone, Gender, Age, Year, GradeAverage, PrefGen, Meeting, WorkPlan, WorkHours, ILocation, IGrade,
                                            Globals.faculty, Globals.course, Globals.workType);
                                    Globals.students[i] = student;


                                }
                                //textViewRes.setText("There is " + ArrayStudentsCount + " Students");
                                Globals.pairs_scores = new int[Globals.students.length][Globals.students.length];

                                for (int i = 0; i < Globals.students.length; i++) {
                                    for (int c = i + 1; c < Globals.students.length && c > i; c++) {
                                        Globals.pairs_scores[c][i] = Globals.pairs_scores[i][c] = -GetScore(Globals.students[i], Globals.students[c]);
                                    }

                                }


                                textViewRes.setText("The Scores have been calculated \n");


                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(GetStudents.this);
                            builder.setMessage("GetStudents Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        GetStudentsReq getStudents = new GetStudentsReq(Globals.faculty, Globals.course, Globals.workType, "id14702484_clients", "id14702484_pairingapp", "Pairing2020YR!", responseListener);
        RequestQueue queue = Volley.newRequestQueue(GetStudents.this);
        queue.add(getStudents);

    }

    public int GetScore(Student student1, Student student2) {

        int ScoreSt1 = GetScoreSide(student1, student2);
        int ScoreSt2 = GetScoreSide(student2, student1);
        int Score = (ScoreSt1 + ScoreSt2) / 2;
        return Score;
    }

    public int GetScoreSide(Student student1, Student student2) {
        float Portions = 0;
        int ScoreSt = 0;
        float[] Criterions = new float[6];//Location-Grade-Workplan-meeting-prefgen-hours
        if (student1.isLocation_flag())
            Criterions[0] = 1;
        else
            Criterions[0] = 0.5f;

        if (student1.isGPA_flag())
            Criterions[1] = 1;
        else
            Criterions[1] = 0.5f;

        if (student1.getPreferred_work_plan().equals("לא משנה"))
            Criterions[2] = 0.5f;
        else
            Criterions[2] = 1;

        if (student1.getPreferred_meetings().equals("לא משנה"))
            Criterions[3] = 0.5f;
        else
            Criterions[3] = 1;


        if (student1.getPreferred_gender().equals("לא משנה"))
            Criterions[4] = 0.5f;
        else
            Criterions[4] = 1;

        if (student1.getPreferred_hours().equals("לא משנה"))
            Criterions[5] = 0.5f;
        else
            Criterions[5] = 1;

        for (int i = 0; i < 6; i++)
            Portions += Criterions[i];
        //Calculate ScoreSt1
        //0
        if (student1.isLocation_flag())
            ScoreSt += (GetLocationScore(student1.getLocation(), student2.getLocation()) / 100.0f) * (100 / Portions * Criterions[0]);
        else
            ScoreSt += (100 / Portions) * Criterions[0];
        //1
        if (student1.isGPA_flag())
            ScoreSt += (student2.getGPA() / 100.0f) * (100 / Portions * Criterions[1]);
        else
            ScoreSt += (100 / Portions) * Criterions[1];
        //2
        if (student1.getPreferred_work_plan().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[2];
        else if (student1.getPreferred_work_plan().equals(student2.getPreferred_work_plan()))
            ScoreSt += (100 / Portions) * Criterions[2];
        else
            ScoreSt += 0;
        //3
        if (student1.getPreferred_meetings().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[3];
        else if (student1.getPreferred_meetings().equals(student2.getPreferred_meetings()))
            ScoreSt += (100 / Portions) * Criterions[3];
        else
            ScoreSt += 0;
        //4
        if (student1.getPreferred_gender().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[4];
        else if (student1.getPreferred_gender().equals(student2.getGender()))
            ScoreSt += (100 / Portions) * Criterions[4];
        else
            ScoreSt += 0;

        //5
        if (student1.getPreferred_hours().equals("לא משנה"))
            ScoreSt += (100 / Portions) * Criterions[5];
        else if (student1.getPreferred_hours().equals(student2.getPreferred_hours()))
            ScoreSt += (100 / Portions) * Criterions[5];
        else
            ScoreSt += 0;

        return ScoreSt;
    }

    public float GetLocationScore(String Loc1, String Loc2) {
        int i1=0, i2=0;

        String[] Locs = getResources().getStringArray(R.array.location_array);
        for (int i = 1; i < Locs.length; i++) {
            if (Locs[i].equals(Loc1))
                i1 = i;
            if (Locs[i].equals(Loc2))
                i2 = i;
        }

        float[][] LocationScores = new float[Locs.length][Locs.length];
        LocationScores[1][1] = 100;
        LocationScores[2][2] = 100;
        LocationScores[3][3] = 100;
        LocationScores[4][4] = 100;
        LocationScores[1][2] = LocationScores[2][1] = 26;
        LocationScores[1][3] = LocationScores[3][1] = 67;
        LocationScores[1][4] = LocationScores[4][1] = 70;

        LocationScores[2][3] = LocationScores[3][2] = 0;
        LocationScores[2][4] = LocationScores[4][2] = 0;

        LocationScores[4][3] = LocationScores[3][4] = 83;
        return LocationScores[i1][i2];
    }
}
