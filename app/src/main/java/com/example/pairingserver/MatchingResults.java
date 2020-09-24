package com.example.pairingserver;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MatchingResults extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        final Button btnDeleteRes = (Button) findViewById(R.id.btnDeleteRes);
        final TableLayout tableRes = (TableLayout) findViewById(R.id.tableRes);

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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MatchingResults.this);
                builder.setMessage("האם למחוק את הנתונים?").setPositiveButton("כן", dialogClickListener)
                        .setNegativeButton("לא", dialogClickListener).show();



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
                        Globals.partners = new Partner[jsonData.length()];
                        CheckBox checkBox;
                        TextView No, name1, name2, title, status;
                        final Typeface tvFont = ResourcesCompat.getFont(MatchingResults.this, R.font.newfont);
                        Globals.pairs = new PairsData[30];
                        if (success) {
                            String[] faculties = {"חוג מדעי מחשב", "חוג למתמטיקה"};
                            String[] coursescs = {"מבוא למדעי המחשב", "מבוא לחמרה", "מבני נתונים", "תכנון וניתוח אלגוריתמים", "מודילים חישוביים"};
                            String[] coursesmath = {"חדוא 1", "חדוא 2", "אלגברה לינארית א", "אלגברה ב", "מתמטיקה דיסקריטית"};
                            String[] worktype = {"תרגילי בית", "פרויקט", "התכוננות למבחן"};
                            int[] lengths = new int[15 * 2];

                            for (int i = 0; i < Globals.partners.length; i++) {
                                int id = jsonData.getJSONObject(i).getInt("id");
                                String user_name = jsonData.getJSONObject(i).getString("user_name");
                                String Name = jsonData.getJSONObject(i).getString("name");
                                String email = jsonData.getJSONObject(i).getString("email");
                                String phone = jsonData.getJSONObject(i).getString("phone");
                                int agreed1 = jsonData.getJSONObject(i).getInt("agreed1");
                                String faculty = jsonData.getJSONObject(i).getString("faculty");
                                String course = jsonData.getJSONObject(i).getString("course");
                                String workType = jsonData.getJSONObject(i).getString("workType");
                                String pairUserName = jsonData.getJSONObject(i).getString("pairUserName");
                                String nameOfPair = jsonData.getJSONObject(i).getString("nameOfPair");
                                String emailOfPair = jsonData.getJSONObject(i).getString("emailOfPair");
                                String phoneOfPair = jsonData.getJSONObject(i).getString("phoneOfPair");
                                int agreed2 = jsonData.getJSONObject(i).getInt("agreed2");
                                Globals.partners[i] = new Partner(id, user_name, Name, email, phone, agreed1, faculty, course, workType, pairUserName, nameOfPair, emailOfPair, phoneOfPair, agreed2);

                            }
                            for (int i = 0; i < Globals.partners.length; i++) {
                                for (int j = 0; j < 2; j++) {
                                    if (faculties[j].equals(Globals.partners[i].getFaculty())) {
                                        for (int k = 0; k < 5; k++) {
                                            if (coursescs[k].equals(Globals.partners[i].getCourse())) {
                                                for (int m = 0; m < 3; m++) {
                                                    if (worktype[m].equals(Globals.partners[i].getWorkType())) {
                                                        lengths[j + 5 * k + m]++;
                                                        k = 5;
                                                        j = 2;
                                                        break;
                                                    }
                                                }

                                            } else if (coursesmath[k].equals(Globals.partners[i].getCourse())) {
                                                for (int m = 0; m < 3; m++) {
                                                    if (worktype[m].equals(Globals.partners[i].getWorkType())) {
                                                        lengths[j * 15 + 5 * k + m]++;
                                                        k = 5;
                                                        j = 2;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            int cs = lengths[0];
                            int stams = lengths[1];
                            for(int i=0;i<Globals.pairs.length;i++) {
                                Globals.pairs[i] = new PairsData();
                                Globals.pairs[i].setPartners(new Partner[lengths[i]]);
                            }


                            for (int i = 0; i < Globals.partners.length; i++) {
                                for (int j = 0; j < 2; j++) {
                                    if (faculties[j].equals(Globals.partners[i].getFaculty())) {
                                        for (int k = 0; k < 5; k++) {
                                            if (coursescs[k].equals(Globals.partners[i].getCourse())) {
                                                for (int m = 0; m < 3; m++) {
                                                    if (worktype[m].equals(Globals.partners[i].getWorkType())) {
                                                        Globals.pairs[j + 5 * k + m].AddPartner(Globals.partners[i]);
                                                        k = 5;
                                                        j = 2;
                                                        break;
                                                    }
                                                }

                                            } else if (coursesmath[k].equals(Globals.partners[i].getCourse())) {
                                                for (int m = 0; m < 3; m++) {
                                                    if (worktype[m].equals(Globals.partners[i].getWorkType())) {
                                                        Globals.pairs[j * 15 + 5 * k + m].AddPartner(Globals.partners[i]);
                                                        k = 5;
                                                        j = 2;
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }






                            for (int j = 0; j < 30; j++) {
                                if (lengths[j] > 0) {
                                    TableRow rowTitle = new TableRow(MatchingResults.this);
                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                    TableRow.LayoutParams lptitle = new TableRow.LayoutParams((int) (10), (int) (50 * Globals.DP));
                                    lp.gravity = Gravity.CENTER_HORIZONTAL;
                                    lptitle.gravity = Gravity.CENTER_HORIZONTAL;
                                    lptitle.weight = 1;
                                    title = new TextView(MatchingResults.this);
                                    title.setText(Globals.pairs[j].getPartners()[0].getCourse() + "");
                                    title.setTextColor(getResources().getColor(R.color.BorderColor));
                                    title.setTypeface(tvFont);
                                    title.setTextSize(Globals.DP * 8);
                                    title.setLayoutParams(lptitle);
                                    rowTitle.addView(title);
                                    rowTitle.setLayoutParams(lp);
                                    tableRes.addView(rowTitle,0);
                                    for(int i = 0; i < lengths[j]; i++)
                                    {
                                        TableRow row = new TableRow(MatchingResults.this);
                                        TableRow.LayoutParams lpname1 = new TableRow.LayoutParams((int) (120 * Globals.DP), (int) (50 * Globals.DP));
                                        TableRow.LayoutParams lpname2 = new TableRow.LayoutParams((int) (120 * Globals.DP), (int) (50 * Globals.DP));
                                        TableRow.LayoutParams lpstatus = new TableRow.LayoutParams((int) (20 * Globals.DP), (int) (50 * Globals.DP));
                                        TableRow.LayoutParams lpNo = new TableRow.LayoutParams((int) (20 * Globals.DP), (int) (50 * Globals.DP));
                                        lpname1.gravity = Gravity.CENTER_HORIZONTAL;
                                        lpname2.gravity = Gravity.CENTER_HORIZONTAL;
                                        lpNo.gravity = Gravity.CENTER_HORIZONTAL;
                                        No = new TextView(MatchingResults.this);
                                        No.setText("" + (i + 1));
                                        No.setTextColor(getResources().getColor(R.color.BorderColor));
                                        No.setTextSize(Globals.DP * 10);
                                        No.setLayoutParams(lpNo);
                                        //minusBtn.setImageResource(R.drawable.minus);
                                        name1 = new TextView(MatchingResults.this);
                                        name1.setTypeface(tvFont);
                                        name1.setText(Globals.pairs[j].getPartners()[i].getName());
                                        name1.setTextColor(getResources().getColor(R.color.BorderColor));
                                        name1.setTextSize(Globals.DP * 10);
                                        name1.setLayoutParams(lpname1);


                                        name2 = new TextView(MatchingResults.this);
                                        name2.setTypeface(tvFont);
                                        name2.setText(Globals.pairs[j].getPartners()[i].getPairName());
                                        name2.setTextColor(getResources().getColor(R.color.BorderColor));
                                        name2.setTextSize(Globals.DP * 10);
                                        name2.setLayoutParams(lpname2);

                                        status = new TextView(MatchingResults.this);
                                        status.setTypeface(tvFont);
                                        status.setTextColor(getResources().getColor(R.color.BorderColor));
                                        status.setTextSize(Globals.DP * 10);
                                        status.setLayoutParams(lpstatus);
                                        if(Globals.pairs[j].getPartners()[i].getAgreed1() == 1 &&Globals.pairs[j].getPartners()[i].getAgreed2() == 1 )
                                            status.setText("ש");
                                        else
                                            status.setText("ע");

                                        row.addView(No);
                                        row.addView(name1);
                                        row.addView(name2);
                                        row.addView(status);
                                        row.setLayoutParams(lp);
                                        tableRes.addView(row, (i+1));

                                    }

                                }

                            }

                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(MatchingResults.this);
                            builder.setMessage("Check Failed")
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

        GetAllPairsReq getAllPairsReq = new GetAllPairsReq("u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
        RequestQueue queue = Volley.newRequestQueue(MatchingResults.this);
        queue.add(getAllPairsReq);


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

                            AlertDialog.Builder builder = new AlertDialog.Builder(MatchingResults.this);
                            builder.setMessage("Check Failed")
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

        RemoveGlobalPairsReq removeGlobalPairsReq = new RemoveGlobalPairsReq("u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
        RequestQueue queue = Volley.newRequestQueue(MatchingResults.this);
        queue.add(removeGlobalPairsReq);
    }
}
