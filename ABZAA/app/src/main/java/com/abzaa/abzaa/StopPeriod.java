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
import android.widget.TextView;
import android.widget.Toast;

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

import static com.abzaa.abzaa.R.id.btnStop_period;

/**
 * Created by Syahmicro on 5/5/2017.
 */

public class StopPeriod extends AppCompatActivity {

    private TextView txtStopMsg;
    private Button btnStopperiod;
    private Context context;
    private Toolbar toolbar;
    public String formattedDatefull,fullDateTime;

    public void init() {
        context = StopPeriod.this;
        txtStopMsg = (TextView) findViewById(R.id.textView_stop_period);
        btnStopperiod = (Button) findViewById(R.id.btnStop_period);

        getDateTime();
    }


    public void getDateTime()
    {
        Calendar c = Calendar.getInstance();

        String replaceneeded = "By clicking button Stop below, the group weekly reading will end by today on *";

        SimpleDateFormat fulldatetime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();

        formattedDatefull = sdf_.format(date);
        fullDateTime = fulldatetime.format(date);

        String replaceDate = replaceneeded.replace("*",formattedDatefull);
        txtStopMsg.setText(replaceDate);
    }


    @Override
    public void onCreate(Bundle sp) {
        super.onCreate(sp);
        setContentView(R.layout.stop_period);
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

        btnStopperiod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                stopPeriod(i.getStringExtra("group_id"),fullDateTime);
            }
        });
    }

    private ProgressDialog pd;
    public void stopPeriod(final String groupId,final String dateTimeString) {
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();

        String url = "http://abza.000webhostapp.com/stop-period.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {
                                Toast.makeText(context, "Period not able to stop.", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Period stopped and notified all members.", Toast.LENGTH_SHORT).show();
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
