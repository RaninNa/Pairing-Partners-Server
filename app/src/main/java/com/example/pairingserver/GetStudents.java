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

        float ScoreSt1 = GetScoreSide(student1, student2);
        float ScoreSt2 = GetScoreSide(student2, student1);
        int Score = (int) ((ScoreSt1 + ScoreSt2) / 2);
        return Score;
    }

    public float GetScoreSide(Student student1, Student student2) {
        float Portions = 0;
        float ScoreSt = 0;
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
        else
            ScoreSt += (GetDayHourWorkingScore(student1.getPreferred_hours(), student2.getPreferred_hours()) / Portions) * Criterions[5];

        return ScoreSt;
    }

    public double GetDayHourWorkingScore(String DH1, String DH2) {
        try {
            String[] St1 = DH1.split("-");
            String[] St2 = DH2.split("-");
            String Student1Days = St1[0];
            String Student2Days = St2[0];
            String Student1Hours = St1[1];
            String Student2Hours = St2[1];
            String[] S1Days = Student1Days.split("@");
            String[] S2Days = Student2Days.split("@");
            String[] S1Hours = Student1Hours.split("@");
            String[] S2Hours = Student2Hours.split("@");
            int st1days = S1Days.length;
            int st2days = S2Days.length;
            int st1hours = S1Hours.length;
            int st2hours = S2Hours.length;
            int countCommonDays = 0;
            for (int i = 0; i < st1days; i++) {
                if (Student2Days.contains(S1Days[i])) {
                    countCommonDays++;
                }
            }
            int totalDays = st1days + st2days - countCommonDays; // תורת הקבוצות :)
            int countCommonHours = 0;
            for (int i = 0; i < st1hours; i++) {
                if (Student2Hours.contains(S1Hours[i])) {
                    countCommonHours++;
                }
            }
            int totalHours = st1hours + st2hours - countCommonHours;
            double scoreDays = 0;
            if (countCommonDays > 3)
                scoreDays = 1;
            else if (countCommonDays >= 2)
                scoreDays = 0.8f;
            else
                scoreDays = countCommonDays / totalDays;
            double scoreHours = 0;
            if (countCommonHours > 2)
                scoreHours = 1;
            else
                scoreHours = countCommonHours / totalHours;
            double TotalScore = scoreDays * scoreHours;

            return TotalScore;

        } catch (Exception ex) {
            return 10;
        }

    }
    public double GetLocationScore(String Loc1, String Loc2) {

        if(Loc1.equals("") || Loc2.equals(""))
        {
            return 0;
        }
        try {

            String[] L1 = Loc1.split("@");
            String[] L2 = Loc2.split("@");
            LatLng latLng1 = new LatLng(Double.parseDouble(L1[0]), Double.parseDouble(L1[0]));
            LatLng latLng2 = new LatLng(Double.parseDouble(L2[0]), Double.parseDouble(L2[1]));
            double Dis = CalculationByDistance(latLng1, latLng2);
            double Res =  (((60 - Dis) / 60) * 100);
            if (Res < 10)
                return 10;
            return Res;

        }
        catch (Exception ex) {
            return 10;
        }
        /*
        int i1 = 0, i2 = 0;

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
        */


    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return valueResult;//Radius * c;
        /*
        LatLng latLng;
        Double l1=latlng.latitude;
        Double l2=latlng.longitude;
        String coordl1 = l1.toString();
        String coordl2 = l2.toString();
        l1 = Double.parseDouble(coordl1);
        l2 = Double.parseDouble(coordl2);


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(l1, l2))
                .title(title)
                .snippet(info));
                */

    }
}
