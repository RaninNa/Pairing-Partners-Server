package com.example.pairingserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
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
import androidx.core.content.res.ResourcesCompat;

import java.text.DecimalFormat;
import java.util.Arrays;


public class GetAllStudents extends AppCompatActivity {
    private boolean NoMatch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_students);
        FixLayoutAspects();
        final TextView textViewRes = (TextView) findViewById(R.id.TVRes);
        final TextView pairing_results = (TextView) findViewById(R.id.pairingResults);
        final Button btnMatch = (Button) findViewById(R.id.btnMatch);
        final Button btnResults = (Button) findViewById(R.id.btnShowMatchingResults);
        final Button btnDeleteRes = (Button) findViewById(R.id.btnDeleteRes);

        btnDeleteRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                DeleteAllMatchingRes();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GetAllStudents.this);
                builder.setMessage("האם למחוק את כל הנתונים?").setPositiveButton("כן", dialogClickListener)
                        .setNegativeButton("לא", dialogClickListener).show();


            }
        });

        final int[][] scores_;
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int d = 0; d < Globals.GlobalData.length; d++) {
                    if (Globals.GlobalData[d] != null && Globals.GlobalData[d].scores.length > 1) {
                        GetTwoPartsStudent(d);
                        HungarianAlgorithm ha = new HungarianAlgorithm(Globals.pairs_scores_new);
                        int[][] assignment = ha.findOptimalAssignment();
                        String AllStudents = "@";
                        for (int i = 0; i < assignment.length; i++) {
                            AllStudents += Globals.studentsP1[assignment[i][0]].getUser_name() + "@";
                            AllStudents += Globals.studentsP1[assignment[i][0]].getUser_name() + "@";
                        }

                        String res_string = " ";
                        JSONArray jsonArray = new JSONArray();
                        if (assignment.length > 0) {
                            // print assignment
                            for (int i = 0; i < assignment.length; i++) {
                                if (AllStudents.contains("@" + Globals.studentsP1[assignment[i][0]].getUser_name() + "@")) {
                                    res_string += Globals.GlobalData[d].getStudents()[assignment[i][0]].getName() + " <=> " + Globals.GlobalData[d].getStudents()[assignment[i][1]].getName() + "\n";
                                    JSONObject jsonObj = new JSONObject();
                                    try {
                                        jsonObj.put("user_name", Globals.studentsP1[assignment[i][0]].getUser_name());
                                        jsonObj.put("name", Globals.studentsP1[assignment[i][0]].getName());
                                        jsonObj.put("email", Globals.studentsP1[assignment[i][0]].getEmail());
                                        jsonObj.put("phone", Globals.studentsP1[assignment[i][0]].getPhone());
                                        jsonObj.put("agreed1", 0);
                                        jsonObj.put("faculty", Globals.studentsP1[assignment[i][0]].getFaculty());
                                        jsonObj.put("course", Globals.studentsP1[assignment[i][0]].getCourse());
                                        jsonObj.put("workType", Globals.studentsP1[assignment[i][0]].getWork_type());
                                        jsonObj.put("pairUserName", Globals.studentsP2[assignment[i][1]].getUser_name());
                                        jsonObj.put("nameOfPair", Globals.studentsP2[assignment[i][1]].getName());
                                        jsonObj.put("emailOfPair", Globals.studentsP2[assignment[i][1]].getEmail());
                                        jsonObj.put("phoneOfPair", Globals.studentsP2[assignment[i][1]].getPhone());
                                        jsonObj.put("agreed2", 0);
                                        jsonArray.put(jsonObj);
                                        AllStudents = AllStudents.replace("@" + Globals.studentsP1[assignment[i][0]].getUser_name() + "@", "@");
                                        AllStudents = AllStudents.replace("@" + Globals.studentsP2[assignment[i][1]].getUser_name() + "@", "@");
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
        btnResults.setVisibility(View.INVISIBLE);
        btnResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetAllStudents.this, MatchingResults.class);
                startActivity(intent);
            }
        });


        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
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
                        Globals.partners = new Partner[jsonData.length()];
                        CheckBox checkBox;
                        TextView No, name1, name2, title, status, empty;
                        Button deleteData;
                        final Typeface tvFont = ResourcesCompat.getFont(GetAllStudents.this, R.font.newfont);
                        Globals.pairs = new PairsData[30];
                        if (success) {
                            NoMatch = false;
                            btnResults.setVisibility(View.VISIBLE);
                        } else {


                        }


                    }


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
                                            Globals.GlobalData = new DataStructure[15 * 2];
                                            String[] faculty = {"חוג מדעי מחשב", "חוג למתמטיקה"};
                                            String[] courses = {"מבוא למדעי המחשב", "מבוא לחמרה", "מבני נתונים", "תכנון וניתוח אלגוריתמים", "מודילים חישוביים"};
                                            String[] coursesmath = {"חדוא 1", "חדוא 2", "אלגברה לינארית א", "אלגברה ב", "מתמטיקה דיסקריטית"};
                                            String[] worktype = {"תרגילי בית", "פרויקט", "התכוננות למבחן"};
                                            int[] lengths = new int[15 * 2];

                                            for (int j = 0; j < 2; j++) {
                                                for (int i = 0; i < Globals.students.length; i++) {
                                                    if (j == 0) {
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
                                                    } else {
                                                        for (int c = 0; c < coursesmath.length; c++) {
                                                            if (Globals.students[i].getCourse().equals(coursesmath[c])) {
                                                                for (int d = 0; d < worktype.length; d++) {
                                                                    if (Globals.students[i].getWork_type().equals(worktype[d])) {
                                                                        lengths[15 + c * 3 + d]++;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            for (int j = 0; j < 2; j++) {
                                                for (int i = 0; i < 5; i++) {
                                                    for (int c = 0; c < 3; c++) {
                                                        if (lengths[j * 15 + i * 3 + c] > 0) {
                                                            Globals.GlobalData[j * 15 + i * 3 + c] = new DataStructure(null, faculty[0], courses[i], worktype[c]);
                                                            Globals.GlobalData[j * 15 + i * 3 + c].setStudents(new Student[lengths[j * 15 + i * 3 + c]]);
                                                        }
                                                    }
                                                }
                                            }
                                            for (int j = 0; j < 2; j++) {
                                                for (int i = 0; i < Globals.students.length; i++) {
                                                    if (j == 0) {
                                                        for (int c = 0; c < courses.length; c++) {
                                                            if (Globals.students[i].getCourse().equals(courses[c])) {
                                                                for (int d = 0; d < worktype.length; d++) {
                                                                    if (Globals.students[i].getWork_type().equals(worktype[d])) {
                                                                        Globals.GlobalData[c * 3 + d].AddStudent(Globals.students[i]);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        for (int c = 0; c < coursesmath.length; c++) {
                                                            if (Globals.students[i].getCourse().equals(coursesmath[c])) {
                                                                for (int d = 0; d < worktype.length; d++) {
                                                                    if (Globals.students[i].getWork_type().equals(worktype[d])) {
                                                                        Globals.GlobalData[15 + c * 3 + d].AddStudent(Globals.students[i]);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }


                                            for (int d = 0; d < Globals.GlobalData.length; d++) {

                                                if (Globals.GlobalData[d] != null) {
                                                    Globals.students = Globals.GlobalData[d].getStudents();
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

                                            }


                                            //Globals.pairs_scores = new int[Globals.students.length][Globals.students.length];


                                            textViewRes.setText("הנתונים מוכנים! \n");
                                            btnMatch.setEnabled(true);

                                        } else {
                                            textViewRes.setText("אין נתונים! \n");
                                            if (!NoMatch)
                                                textViewRes.setText("אין נתונים חדשים, יש כבר שידוך! \n");
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
                                textViewRes.setText("אין נתונים! \n");
                                if (!NoMatch)
                                    textViewRes.setText("אין נתונים חדשים, יש כבר שידוך! \n");
                            }

                        }
                    };


                    GetAllStudentsReq getAllStudents = new GetAllStudentsReq("u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(GetAllStudents.this);
                    queue.add(getAllStudents);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetAllPairsReq getAllPairsReq = new GetAllPairsReq("u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener2);
        RequestQueue queue3 = Volley.newRequestQueue(GetAllStudents.this);
        queue3.add(getAllPairsReq);


    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void FixLayoutAspects() {

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RLGet);
        int childCount = rl.getChildCount();

        ViewGroup.LayoutParams LPR = (ViewGroup.LayoutParams) rl.getLayoutParams();


        if (LPR.width > 0)
            LPR.width = (int) (LPR.width * Globals.scaleDP);
        if (LPR.height > 0)
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
            if (NewLP.height > 0)
                NewLP.height = (int) (LP.height * Globals.scaleDP);
            if (NewLP.width > 0)
                NewLP.width = (int) (LP.width * Globals.scaleDP);

            if (view instanceof Button) {
                Button button = (Button) view;
                float size = button.getTextSize();
                button.setTextSize((button.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof TextView) {
                TextView textView = (TextView) view;
                float size = textView.getTextSize();
                textView.setTextSize((textView.getTextSize() * Globals.scaleDP * Globals.scaleS) / Globals.DP);
            } else if (view instanceof EditText) {
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
                if (Globals.ActualWidth / (float) (Globals.ActualHeight) > 9.0f / 16.0f)
                    NewLP.topMargin = (int) (((NewLP.topMargin / Globals.DP) - 15) * Globals.DP);
                //view.setLayoutParams(NewLP);
            } else if (view instanceof CardView) {
                if(Globals.Ratio > (float)(17f / 9f) ) {
                    NewLP.height = (int) (NewLP.height * 1.1f);
                }
                if (Globals.Ratio > (float) (19f / 9f)) {
                    NewLP.height = (int) (NewLP.height * 1.12f);
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
            NewLP.gravity = LP.gravity;
            NewLP.topMargin = LP.topMargin;
            NewLP.leftMargin = LP.leftMargin;
            NewLP.bottomMargin = LP.bottomMargin;
            NewLP.rightMargin = LP.rightMargin;


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

            } else if (view instanceof CardView) {
                if(Globals.Ratio > (float)(17f / 9f) ) {
                    NewLP.height = (int) (NewLP.height * 1.1f);
                }
                if (Globals.Ratio > (float) (19f / 9f)) {
                    NewLP.height = (int) (NewLP.height * 1.12f);
                }
            }
            view.setLayoutParams(NewLP);


        }
    }

    void DeleteAllMatchingRes()
    {
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
                        if (success) {
                            finish();


                        } else {


                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RemoveGlobalPairsReq removeGlobalPairsReq = new RemoveGlobalPairsReq("u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
        RequestQueue queue = Volley.newRequestQueue(GetAllStudents.this);
        queue.add(removeGlobalPairsReq);
    }


    void GetTwoPartsStudent(int K) {
        int[] ScoresOfStudents = new int[Globals.GlobalData[K].getStudents().length];
        for (int i = 0; i < Globals.GlobalData[K].getScores().length; i++) {

            int sum = 0;
            for (int c = 0; c < Globals.GlobalData[K].getScores().length; c++) {
                sum += Globals.GlobalData[K].getScores()[i][c];
            }
            ScoresOfStudents[i] = sum;
            Globals.GlobalData[K].getStudents()[i].setTotalScore(sum);
        }
        //Arrays.sort(ScoresOfStudents);
        Student[] NewStudents = Globals.GlobalData[K].getStudents();
        quickSort(NewStudents, 0, NewStudents.length - 1);

        int count1 = 0, count2 = 0;
        Globals.studentsP1 = new Student[NewStudents.length / 2];
        Globals.studentsP2 = new Student[NewStudents.length / 2];
        for (int i = 0; i < NewStudents.length; i++) {

            if (i % 2 == 0) {
                Globals.studentsP1[count1] = NewStudents[i];
                count1++;
            } else {
                Globals.studentsP2[count2] = NewStudents[i];
                count2++;
            }
        }
        Globals.pairs_scores_new = new int[Globals.studentsP1.length][Globals.studentsP2.length];
        for (int i = 0; i < Globals.studentsP1.length; i++) {
            for (int c = 0; c < Globals.studentsP2.length; c++) {
                Globals.pairs_scores_new[i][c] = Globals.GlobalData[K].getScores()[Globals.studentsP1[i].getNo()][Globals.studentsP2[c].getNo()];
            }

        }


        return;
    }

    static int partition(Student[] array, int begin, int end) {
        int pivot = end;

        int counter = begin;
        for (int i = begin; i < end; i++) {
            if (array[i].getTotalScore() < array[pivot].getTotalScore()) {
                Student temp = array[counter];
                array[counter] = array[i];
                array[i] = temp;
                counter++;
            }
        }
        Student temp = array[pivot];
        array[pivot] = array[counter];
        array[counter] = temp;

        return counter;
    }

    public static void quickSort(Student[] array, int begin, int end) {
        if (end <= begin) return;
        int pivot = partition(array, begin, end);
        quickSort(array, begin, pivot-1);
        quickSort(array, pivot+1, end);
    }
}
