package com.example.pairingserver;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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

public class MatchingResults extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity);
        final Button btnDeleteRes = (Button) findViewById(R.id.btnDeleteRes);
        final TableLayout tableRes = (TableLayout) findViewById(R.id.tableRes);
        CheckBox checkBox;
        TextView tv,qty;
        ImageButton addBtn,minusBtn;

        for (int i = 0; i <10; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            checkBox = new CheckBox(this);
            checkBox.setTextColor(getResources().getColor(R.color.BorderColor));
            tv = new TextView(this);
            tv.setText("tv " + i);
            tv.setTextColor(getResources().getColor(R.color.BorderColor));
            tv.setTextSize(Globals.DP * 10);
            //minusBtn.setImageResource(R.drawable.minus);
            qty = new TextView(this);
            qty.setText("qty " + i);
            qty.setTextColor(getResources().getColor(R.color.BorderColor));
            qty.setTextSize(Globals.DP * 10);
            checkBox.setText("hello");

            row.addView(checkBox);
            row.addView(tv);
            row.addView(qty);
            row.setLayoutParams(lp);
            tableRes.addView(row, i);
        }

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

        RemoveGlobalPairsReq removeGlobalPairsReq = new RemoveGlobalPairsReq("id14702484_clients", "id14702484_pairingapp", "Pairing2020YR!", responseListener);
        RequestQueue queue = Volley.newRequestQueue(MatchingResults.this);
        queue.add(removeGlobalPairsReq);
    }
}
