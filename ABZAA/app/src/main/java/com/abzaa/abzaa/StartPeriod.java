package com.abzaa.abzaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zarul on 15/04/16.
 */
public class StartPeriod extends AppCompatActivity {

    private EditText txtDate, txtTime;
    private TextView textDateTimeReplace;
    private Button btnStart;
    private Context context;
    private Toolbar toolbar;
    public String formattedDate, formattedTime, formattedDatefull,fullDateTime;

    public void init() {
        context = StartPeriod.this;
        textDateTimeReplace = (TextView) findViewById(R.id.textView35);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);
        btnStart = (Button) findViewById(R.id.btnStart);

        getDateTime();
    }

    public void getDateTime()
    {
        Calendar c = Calendar.getInstance();

        String replaceneeded = "By clicking button Start below, the group weekly reading will end on every * of button click at #";

        c.add(Calendar.DATE, -1);

        SimpleDateFormat fulldatetime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();

        formattedDate = df.format(c.getTime());
        formattedTime = timeFormat.format(c.getTime());
        formattedDatefull = sdf_.format(date);
        fullDateTime = fulldatetime.format(date);

        String replaceDate = replaceneeded.replace("*",formattedDatefull);
        String replaceTime = replaceDate.replace("#","11.59 PM");
        textDateTimeReplace.setText(replaceTime);
    }


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.start_period);
        init();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                startPeriod(i.getStringExtra("group_id"),fullDateTime);
            }
        });
    }

    private ProgressDialog pd;
    public void startPeriod(final String groupId,final String dateTimeString) {
        pd = new ProgressDialog(context);
        pd.setMessage("Starting...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();

        String url = "http://abza.000webhostapp.com/start-period.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {
                                Toast.makeText(context, "Period not able to start.", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Period started and notified all members.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "Server error.", Toast.LENGTH_SHORT).show();
                            Log.e("VolleyError", e.toString());
                        }
                        pd.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Toast.makeText(context, "Please connect to internet.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", groupId);
                params.put("spdatetimefull",dateTimeString);
                Log.e("Response", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}