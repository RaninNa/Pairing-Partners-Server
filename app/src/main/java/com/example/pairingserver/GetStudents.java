package com.example.pairingserver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
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
                HungarianAlgorithm ha = new HungarianAlgorithm(Globals.pairs_scores);
                int[][] assignment = ha.findOptimalAssignment();

                String res_string = " ";
                JSONArray jsonArray = new JSONArray();
                if (assignment.length > 0) {
                    // print assignment
                    for (int i = 0; i < assignment.length; i++) {
                        res_string +=  Globals.students[assignment[i][0]].getName() + " => " + Globals.students[assignment[i][1]].getName() + "\n";
                        JSONObject jsonObj = new JSONObject();
                        try {
                            jsonObj.put("user_name",Globals.students[assignment[i][0]].getUser_name());
                            jsonObj.put("course",Globals.students[assignment[i][0]].getCourse());
                            jsonObj.put("nameOfPair",Globals.students[assignment[i][1]].getName());
                            jsonObj.put("emailOfPair",Globals.students[assignment[i][1]].getEmail());
                            jsonObj.put("phoneOfPair",Globals.students[assignment[i][1]].getPhone());

                            jsonArray.put(jsonObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                String id = jsonResponse.getString("id");
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "שליחה התבצעה", Toast.LENGTH_LONG).show();
                                    //Intent intent = new Intent();
                                    //getActivity().startActivity(intent);
                                    //Intent intent = new Intent(AuthenticateUser.this, RegisterEventActivity.class);
                                    //AuthenticateUser.this.startActivity(intent);

                                    try {
                                        //if (AuthenticateUser.this != null)
                                        //hideSoftKeyboard(AuthenticateUser.this);
                                    } catch (Exception e) {

                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "שליחה נכשלה", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };


                    UpdatePairsReq registerRequest = new UpdatePairsReq(jsonArray, "id14702484_clients", "id14702484_pairingapp", "Pairing2020YR!", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(GetStudents.this);
                    queue.add(registerRequest);



                } else {
                    Toast.makeText(getApplicationContext(), "no assignment found!", Toast.LENGTH_LONG).show();
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
                                    String user_name = jsonData.getJSONObject(i).getString("user_name");
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


                                    Student student = new Student(user_name, Name, Location, Email, Phone, Gender, Age, Year, GradeAverage, PrefGen, Meeting, WorkPlan, WorkHours, ILocation, IGrade,
                                            Globals.faculty, Globals.course, Globals.workType);
                                    Globals.students[i] = student;


                                }
                                //textViewRes.setText("There is " + ArrayStudentsCount + " Students");
                                Globals.pairs_scores = new int[Globals.students.length][Globals.students.length];

                                for (int i = 0; i < Globals.students.length; i++) {
                                    for (int c = i + 1; c < Globals.students.length && c > i; c++) {
                                        Globals.pairs_scores[c][i] = Globals.pairs_scores[i][c] = -Helpers.GetScore(Globals.students[i], Globals.students[c]);
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

}
