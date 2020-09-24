package com.example.pairingserver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.DecimalFormat;
import java.util.Arrays;

public class GetStudents extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_students);
        FixLayoutAspects();
        final TextView textViewRes = (TextView) findViewById(R.id.TVRes);
        final TextView pairing_results = (TextView) findViewById(R.id.pairingResults);
        final Button btnMatch = (Button) findViewById(R.id.btnMatch);
        final Button btnResults = (Button) findViewById(R.id.btnShowMatchingResults);
        btnResults.setVisibility(View.INVISIBLE);
        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetStudents.this, MatchingResults.class);
                startActivity(intent);
            }
        });
        final int[][] scores_;
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HungarianAlgorithm ha = new HungarianAlgorithm(Globals.pairs_scores);
                int[][] assignment = ha.findOptimalAssignment();
                String AllStudents = "@";
                for (int i = 0; i < assignment.length; i++) {
                    AllStudents += Globals.students[assignment[i][0]].getUser_name() + "@";
                }

                String res_string = " ";
                JSONArray jsonArray = new JSONArray();
                if (assignment.length > 0) {
                    // print assignment
                    for (int i = 0; i < assignment.length; i++) {
                        if (AllStudents.contains("@" + Globals.students[assignment[i][0]].getUser_name() + "@")) {
                            res_string += Globals.students[assignment[i][0]].getName() + " <=> " + Globals.students[assignment[i][1]].getName() + "\n";
                            JSONObject jsonObj = new JSONObject();
                            try {
                                jsonObj.put("user_name", Globals.students[assignment[i][0]].getUser_name());
                                jsonObj.put("name", Globals.students[assignment[i][0]].getName());
                                jsonObj.put("email", Globals.students[assignment[i][0]].getEmail());
                                jsonObj.put("phone", Globals.students[assignment[i][0]].getPhone());
                                jsonObj.put("agreed1", 0);
                                jsonObj.put("faculty", Globals.students[assignment[i][0]].getFaculty());
                                jsonObj.put("course", Globals.students[assignment[i][0]].getCourse());
                                jsonObj.put("workType", Globals.students[assignment[i][0]].getWork_type());
                                jsonObj.put("pairUserName", Globals.students[assignment[i][1]].getUser_name());
                                jsonObj.put("nameOfPair", Globals.students[assignment[i][1]].getName());
                                jsonObj.put("emailOfPair", Globals.students[assignment[i][1]].getEmail());
                                jsonObj.put("phoneOfPair", Globals.students[assignment[i][1]].getPhone());
                                jsonObj.put("agreed2", 0);
                                jsonArray.put(jsonObj);
                                AllStudents = AllStudents.replace("@" + Globals.students[assignment[i][0]].getUser_name() + "@", "@");
                                AllStudents = AllStudents.replace("@" + Globals.students[assignment[i][1]].getUser_name() + "@", "@");

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
                                if (success) {
                                    Toast.makeText(getApplicationContext(), "שליחה התבצעה", Toast.LENGTH_LONG).show();
                                    btnResults.setVisibility(View.VISIBLE);
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


                    UpdatePairsReq registerRequest = new UpdatePairsReq(jsonArray, "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
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
                                if (Globals.students.length % 2 == 1) {
                                    double MinScore = 100*Globals.students.length;
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

                                }
                                else
                                {
                                    Globals.pairs_scores = new int[Globals.students.length][Globals.students.length];

                                    for (int i = 0; i < Globals.students.length; i++) {
                                        for (int c = i + 1; c < Globals.students.length && c > i; c++) {
                                            Globals.pairs_scores[c][i] = Globals.pairs_scores[i][c] = -Helpers.GetScore(Globals.students[i], Globals.students[c]);
                                        }

                                    }
                                }



                                textViewRes.setText("הנתונים מוכנים! \n");
                                btnMatch.setEnabled(true);


                            }
                            else {
                                textViewRes.setText("אין נתונים! \n");
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(GetStudents.this);
                            builder.setMessage("GetStudents Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };


        GetStudentsReq getStudents = new GetStudentsReq(Globals.faculty, Globals.course, Globals.workType, "u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
        RequestQueue queue = Volley.newRequestQueue(GetStudents.this);
        queue.add(getStudents);

    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void FixLayoutAspects()
    {

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RLGet);
        int childCount = rl.getChildCount();

        ViewGroup.LayoutParams LPR = (ViewGroup.LayoutParams) rl.getLayoutParams();


        if(LPR.width>0)
            LPR.width = (int) (LPR.width * Globals.scaleDP);
        if(LPR.height>0)
            LPR.height = (int) (LPR.height * Globals.scaleDP);
        rl.setLayoutParams(LPR);
        for (int i = 0; i < childCount; i++) {
            View view = rl.getChildAt(i);
            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (view.getLayoutParams().width * Globals.scaleDP),
            //        (int) (view.getLayoutParams().height * Globals.scaleDP));
            RelativeLayout.LayoutParams LP = (RelativeLayout.LayoutParams) view.getLayoutParams();
            //layoutParams.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
            //        (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));
            RelativeLayout.LayoutParams NewLP = new RelativeLayout.LayoutParams(LP);
            int[] rules = LP.getRules();
            for (int verb = 0; verb < rules.length; verb++) {
                int subject = rules[verb];
                NewLP.addRule(verb, subject);
            }
            NewLP.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
                    (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));
            if (NewLP.height > 0 )
                NewLP.height = (int) (LP.height * Globals.scaleDP);
            if(NewLP.width > 0)
                NewLP.width = (int) (LP.width * Globals.scaleDP);

            if (view instanceof Button) {
                Button button = (Button) view;
                float size = button.getTextSize();
                button.setTextSize((button.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            }
            else if (view instanceof TextView) {
                TextView textView = (TextView) view;
                float size = textView.getTextSize();
                textView.setTextSize((textView.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            }  else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                float size = editText.getTextSize();
                editText.setTextSize((editText.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                float height = imageView.getHeight();
                float width = imageView.getWidth();
                //view.setLayoutParams(NewLP);

                //imageView.setTextSize((imageView.getTextSize() * Globals.scaleDP)/ Globals.DP);
            } else if (view instanceof Spinner) {
                Spinner spinner = (Spinner) view;
                if(Globals.ActualWidth / (float)(Globals.ActualHeight) > 9.0f /16.0f)
                    NewLP.topMargin = (int)(((NewLP.topMargin / Globals.DP)-15)*Globals.DP) ;
                //view.setLayoutParams(NewLP);
            }
            else if (view instanceof CardView)
            {
                if(Globals.Ratio >17f / 9f ) {
                    NewLP.height = (int) (NewLP.height * 1.1f);
                }
            }
            view.setLayoutParams(NewLP);

            //view.setX(location[0]);
            //view.setY(location[1]);

            // Do something with v.
            // …


        }

        CardView c1 = (CardView) findViewById(R.id.CardViewGetStudents);


        childCount = c1.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = c1.getChildAt(i);
            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (view.getLayoutParams().width * Globals.scaleDP),
            //        (int) (view.getLayoutParams().height * Globals.scaleDP));
            FrameLayout.LayoutParams LP = (FrameLayout.LayoutParams) view.getLayoutParams();
            //layoutParams.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
            //        (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));
            FrameLayout.LayoutParams NewLP = new FrameLayout.LayoutParams(LP);
            NewLP.gravity=LP.gravity;
            NewLP.topMargin=LP.topMargin;
            NewLP.leftMargin=LP.leftMargin;
            NewLP.bottomMargin=LP.bottomMargin;
            NewLP.rightMargin=LP.rightMargin;



            NewLP.setMargins((int) (LP.leftMargin * Globals.scaleDP), (int) (LP.topMargin * Globals.scaleDP),
                    (int) (LP.rightMargin * Globals.scaleDP), (int) (LP.bottomMargin * Globals.scaleDP));

            if (NewLP.height > 0)
                NewLP.height = (int) (LP.height * Globals.scaleDP);
            if (NewLP.width > 0)
                NewLP.width = (int) (LP.width * Globals.scaleDP);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                float size = textView.getTextSize();
                textView.setTextSize((textView.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof Button) {
                Button button = (Button) view;
                float size = button.getTextSize();
                button.setTextSize((button.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof EditText) {
                EditText editText = (EditText) view;
                float size = editText.getTextSize();
                editText.setTextSize((editText.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                float height = imageView.getHeight();
                float width = imageView.getWidth();

                //imageView.setTextSize((imageView.getTextSize() * Globals.scaleDP)/ Globals.DP);
            } else if (view instanceof Spinner) {
                Spinner spinner = (Spinner) view;

            }
            else if (view instanceof CardView)
            {
                if(Globals.Ratio >17f / 9f ) {
                    NewLP.height = (int) (NewLP.height * 1.1f);
                }
            }
            view.setLayoutParams(NewLP);


        }
    }


}
