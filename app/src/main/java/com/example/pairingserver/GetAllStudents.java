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


public class GetAllStudents extends AppCompatActivity {
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
                for (int d = 0; d < Globals.GlobalData.length; d++) {
                    if (Globals.GlobalData[d] != null) {
                        HungarianAlgorithm ha = new HungarianAlgorithm(Globals.GlobalData[d].scores);
                        int[][] assignment = ha.findOptimalAssignment();
                        String AllStudents = "@";
                        for (int i = 0; i < assignment.length; i++) {
                            AllStudents += Globals.GlobalData[d].getStudents()[assignment[i][0]].getUser_name() + "@";
                        }

                        String res_string = " ";
                        JSONArray jsonArray = new JSONArray();
                        if (assignment.length > 0) {
                            // print assignment
                            for (int i = 0; i < assignment.length; i++) {
                                if (AllStudents.contains("@" + Globals.GlobalData[d].getStudents()[assignment[i][0]].getUser_name() + "@")) {
                                    res_string += Globals.GlobalData[d].getStudents()[assignment[i][0]].getName() + " <=> " + Globals.GlobalData[d].getStudents()[assignment[i][1]].getName() + "\n";
                                    JSONObject jsonObj = new JSONObject();
                                    try {
                                        jsonObj.put("user_name", Globals.GlobalData[d].getStudents()[assignment[i][0]].getUser_name());
                                        jsonObj.put("name", Globals.GlobalData[d].getStudents()[assignment[i][0]].getName());
                                        jsonObj.put("email", Globals.GlobalData[d].getStudents()[assignment[i][0]].getEmail());
                                        jsonObj.put("phone", Globals.GlobalData[d].getStudents()[assignment[i][0]].getPhone());
                                        jsonObj.put("agreed1", 0);
                                        jsonObj.put("faculty", Globals.GlobalData[d].getStudents()[assignment[i][0]].getFaculty());
                                        jsonObj.put("course", Globals.GlobalData[d].getStudents()[assignment[i][0]].getCourse());
                                        jsonObj.put("workType", Globals.GlobalData[d].getStudents()[assignment[i][0]].getWork_type());
                                        jsonObj.put("pairUserName", Globals.GlobalData[d].getStudents()[assignment[i][1]].getUser_name());
                                        jsonObj.put("nameOfPair", Globals.GlobalData[d].getStudents()[assignment[i][1]].getName());
                                        jsonObj.put("emailOfPair", Globals.GlobalData[d].getStudents()[assignment[i][1]].getEmail());
                                        jsonObj.put("phoneOfPair", Globals.GlobalData[d].getStudents()[assignment[i][1]].getPhone());
                                        jsonObj.put("agreed2", 0);
                                        jsonArray.put(jsonObj);
                                        AllStudents = AllStudents.replace("@" + Globals.GlobalData[d].getStudents()[assignment[i][0]].getUser_name() + "@", "@");
                                        AllStudents = AllStudents.replace("@" + Globals.GlobalData[d].getStudents()[assignment[i][1]].getUser_name() + "@", "@");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

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
                            RequestQueue queue = Volley.newRequestQueue(GetAllStudents.this);
                            queue.add(registerRequest);


                        } else {
                            Toast.makeText(getApplicationContext(), "no assignment found!", Toast.LENGTH_LONG).show();
                        }

                        pairing_results.setText(res_string);
                    }
                }

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

                        if (success) {


                            //Globals.ArrayEvents = new Event[ArrayEventsCount];

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

                                    String Faculty = jsonData.getJSONObject(i).getString("Faculty");
                                    String Course = jsonData.getJSONObject(i).getString("Course");
                                    String WorkType = jsonData.getJSONObject(i).getString("WorkType");
                                    //String Type = jsonData.getJSONObject(i).getString("Type");


                                    Student student = new Student(user_name, Name, Location, Email, Phone, Gender, Age, Year, GradeAverage, PrefGen, Meeting, WorkPlan, WorkHours, ILocation, IGrade,
                                            Faculty, Course, WorkType);
                                    Globals.students[i] = student;


                                }
                                //textViewRes.setText("There is " + ArrayStudentsCount + " Students");
                                Globals.GlobalData = new DataStructure[18];
                                String faculty = "מבוא למדעי המחשב";
                                String[] courses = {"מבוא למדעי המחשב", "מבוא לחמרה", "מתמטיקה דיסקריטית", "מבני נתונים", "תכנון וניתוח אלגוריתמים", "מודילים חישוביים"};
                                String[] worktype = {"תרגילי בית", "פרויקט", "התכוננות למבחן"};
                                int[] lengths = new int[18];
                                for (int i = 0; i < Globals.students.length; i++) {
                                    for (int c = 0; c < courses.length; c++) {
                                        if (Globals.students[i].getCourse().equals(courses[c])) {
                                            for (int d = 0; d < worktype.length; d++) {
                                                if (Globals.students[i].getWork_type().equals(worktype[d])) {
                                                    lengths[c * 3 + d]++;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < 6; i++) {
                                    for (int c = 0; c < 3; c++) {
                                        if (lengths[i * 3 + c] > 0)
                                        {
                                            Globals.GlobalData[i * 3 + c] = new DataStructure(null, faculty, courses[i], worktype[c]);
                                            Globals.GlobalData[i * 3 + c].setStudents(new Student[lengths[i * 3 + c]]);
                                        }
                                    }
                                }
                                for (int i = 0; i < Globals.students.length; i++) {
                                    for (int c = 0; c < courses.length; c++) {
                                        if (Globals.students[i].getCourse().equals(courses[c])) {
                                            for (int d = 0; d < worktype.length; d++) {
                                                if (Globals.students[i].getWork_type().equals(worktype[d])) {
                                                    Globals.GlobalData[c * 3 + d].AddStudent(Globals.students[i]);
                                                }
                                            }
                                        }
                                    }
                                }




                                for (int d = 0; d < Globals.GlobalData.length; d++) {

                                    Globals.students= Globals.GlobalData[d].getStudents();
                                    if (Globals.students.length % 2 == 1) {
                                        double MinScore = 100 * Globals.students.length;
                                        int index = -1;
                                        Globals.pairs_scores = new int[Globals.students.length][Globals.students.length];
                                        for (int i = 0; i < Globals.students.length; i++) {
                                            for (int c = i + 1; c < Globals.students.length && c > i; c++) {
                                                Globals.pairs_scores[c][i] = Globals.pairs_scores[i][c] = Helpers.GetScore(Globals.students[i], Globals.students[c]);
                                            }
                                        }
                                        for (int i = 0; i < Globals.students.length; i++) {
                                            int sum = 0;
                                            for (int c = 0; c < Globals.students.length; c++) {
                                                sum += Globals.pairs_scores[c][i];
                                            }
                                            if (sum <= MinScore) {
                                                MinScore = sum;
                                                index = i;
                                            }
                                        }
                                        Helpers.RemoveRowColScores(index);
                                        Helpers.RemoveRowColStudents(index);
                                        for (int i = 0; i < Globals.students.length; i++) {
                                            for (int c = i + 1; c < Globals.students.length && c > i; c++) {
                                                Globals.pairs_scores[c][i] = Globals.pairs_scores[i][c] *= -1;
                                            }
                                        }
                                        Globals.GlobalData[d].setStudents(Globals.students);
                                        Globals.GlobalData[d].setScores(Globals.pairs_scores);

                                    } else {
                                        if (Globals.GlobalData[d] != null) {
                                            int[][] Score = new int[Globals.GlobalData[d].getStudents().length][Globals.GlobalData[d].getStudents().length];
                                            for (int i = 0; i < Globals.GlobalData[d].getStudents().length; i++) {
                                                for (int c = i + 1; c < Globals.GlobalData[d].getStudents().length && c > i; c++) {
                                                    Score[c][i] = Score[i][c] = -Helpers.GetScore(Globals.GlobalData[d].getStudents()[i], Globals.GlobalData[d].getStudents()[c]);
                                                }
                                            }
                                            Globals.GlobalData[d].setScores(Score);
                                        }

                                    }
                                }


                                //Globals.pairs_scores = new int[Globals.students.length][Globals.students.length];



                                textViewRes.setText("The Scores have been calculated \n");


                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(GetAllStudents.this);
                            builder.setMessage("GetAllStudents Failed")
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


        GetAllStudentsReq getAllStudents = new GetAllStudentsReq("id14702484_clients", "id14702484_pairingapp", "Pairing2020YR!", responseListener);
        RequestQueue queue = Volley.newRequestQueue(GetAllStudents.this);
        queue.add(getAllStudents);

    }


}
