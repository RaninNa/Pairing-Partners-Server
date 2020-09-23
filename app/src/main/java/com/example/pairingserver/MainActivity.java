package com.example.pairingserver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetScreenSizeScaleParameters();
        FixLayoutAspects();
        Button btnSpecific = (Button) findViewById(R.id.btnSpecificMatching);
        Button btnGlobalMatch = (Button) findViewById(R.id.btnGlobalMatching);
        final TextView TVNoteResults = (TextView) findViewById(R.id.TVNoteResults);
        final Button btnMatchingResults = (Button) findViewById(R.id.btnShowMatchingResults);
        //btnMatchingResults.setVisibility(View.INVISIBLE);
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

        btnMatchingResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MatchingResults.class);
                startActivity(intent);
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

                        if (success) {
                            if(ArrayStudentsCount>0)
                            {
                                TVNoteResults.setText("יש כבר שידוך לתוצאות:");
                                btnMatchingResults.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                TVNoteResults.setText("אין שידוך עדיין");
                                btnMatchingResults.setVisibility(View.INVISIBLE);
                            }

                            //Globals.ArrayEvents = new Event[ArrayEventsCount];


                        } else {

                            TVNoteResults.setText("אין שידוך עדיין");

                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        CheckGlobalPairsReq checkGlobalPairsReq = new CheckGlobalPairsReq("u747931869_FindPair", "u747931869_yuosifhanna", "V!5:Eg0H~", responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(checkGlobalPairsReq);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void GetScreenSizeScaleParameters()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics metrics2 = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics2);
        Globals.ScreenHeight = metrics2.heightPixels;//+ (int)(metrics.density*48);
        Display d = getWindowManager().getDefaultDisplay();


        //int navigBottomHeight= hasNavBar(getResources());
        //Globals.ScreenHeight+=(int)(metrics.density*48);


        Globals.ActualWidth = metrics2.widthPixels;
        Globals.ActualHeight = metrics2.heightPixels;
        Globals.ScreenWidth = metrics.widthPixels;
        Globals.Ratio = (float) metrics2.heightPixels/ Globals.ScreenWidth;
        int densityDpi = (int)(metrics.density * 160f);
        Globals.metrics=metrics;
        Globals.metrics2=metrics2;

        /*if(Globals.Ratio==18f / 9f )
        {
            if(metrics.density<3.5f)
            {
                Globals.scaleDP = 3.5f / metrics.density * Globals.ScreenWidth / 1440f;
                //Globals.scaleDP = 1;
            }
        }*/

        if(Globals.Ratio==18.5f / 9f ||  Globals.Ratio==19f / 9f || Globals.Ratio==19.5f / 9f || Globals.Ratio==18f / 9f || Globals.Ratio == 20f/9f) {
            Globals.scaleDP = 3.5f / metrics.density * Globals.ScreenWidth / 1440f;
        }
        if(Globals.Ratio==16f / 9f )
        {
            if(metrics.heightPixels!=metrics2.heightPixels)
            {
                Globals.scaleDP=0.95f*3.5f /metrics.density * Globals.ScreenWidth / 1440f;
            }
            else
            {
                Globals.scaleDP=3.5f /metrics.density * Globals.ScreenWidth / 1440f;
            }
        }

        Globals.DP=metrics.density;
    }



    void FixLayoutAspects()
    {

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.RLMainActivity);
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

            view.setLayoutParams(NewLP);

            //view.setX(location[0]);
            //view.setY(location[1]);

            // Do something with v.
            // …


        }



        rl = findViewById(R.id.RLCardView);

        LPR = (ViewGroup.LayoutParams) rl.getLayoutParams();
        if (LPR.width > 0)
            LPR.width = (int) (LPR.width * Globals.scaleDP);
        if (LPR.height > 0)
            LPR.height = (int) (LPR.height * Globals.scaleDP);
        if(Globals.Ratio >17f / 9f ) {
            LPR.height = (int) (LPR.height * 1.3f);
        }
        rl.setLayoutParams(LPR);

        CardView c1 = (CardView) findViewById(R.id.CardViewMain);


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

            view.setLayoutParams(NewLP);


        }
    }

}