package com.example.pairingserver;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GetStudents extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_students);

       final TextView textViewRes = (TextView) findViewById(R.id.TVRes);

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
                                textViewRes.setText("There is " + ArrayStudentsCount + " Students");

                                /*
                                if (Globals.events != null && Globals.accounts != null) {
                                    for (int d = 0; d < Globals.events.getCalendarEvents().length; d++) {
                                        for (int i = 0; i < 12; i++) {
                                            for (int c = 0; c < 31; c++) {
                                                for (int k = 0; k < 10; k++) {
                                                    if (Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k] != null) {
                                                        int WedID = Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].getWedID();
                                                        Account acc = Globals.accountArray.findItem(0, Globals.accountArray.getArray().length - 1, WedID);
                                                        if (acc != null)
                                                            if (Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].getType().equals("z") || Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].getType().equals("sg") || Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].getType().equals("a"))
                                                                Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].setName(acc.getGroomN() + " " + acc.getGroomFN() + " " + acc.getGroomFam());
                                                            else if (Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].getType().equals("sb"))
                                                                Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].setName(acc.getBrideN() + " " + acc.getBrideFN() + " " + acc.getBrideFam());
                                                            else if (Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].getType().equals("ss"))
                                                                Globals.events.getCalendarEvents()[d].getYearevents()[i].getMonthevents()[c].getEvents()[k].setName(acc.getGroomN() + " " + acc.getGroomFam() + " & " + acc.getBrideN() + " " + acc.getBrideFam());
                                                        //Globals.userEvents[i].setName(brideN + " " + brideFN + " " + brideFam);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                */
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
