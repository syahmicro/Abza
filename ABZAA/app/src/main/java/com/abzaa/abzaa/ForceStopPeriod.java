package com.abzaa.abzaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syahmicro on 6/5/2017.
 */

public class ForceStopPeriod extends AppCompatActivity{

    private TextView txtFSP,txtgetday;
    private Button btnsaveSP;
    private Toolbar toolbar;
    private Context context;
    private Spinner dropdown;
    public String getday;

    public void init() {
        context  = ForceStopPeriod.this;
        btnsaveSP = (Button) findViewById(R.id.btnsavefsp);
        txtFSP = (TextView) findViewById(R.id.txtfsp);
        txtgetday = (TextView) findViewById(R.id.txtgetSaveday);

        dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Sunday", "Monday","Tuesday", "Wednesday", "Thursday","Friday","Saturday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {
                getday=dropdown.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
}

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.force_stop_period);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           String mainURL= extras.getString("group_id");
            getSavedDay(mainURL);
        }

        SimpleDateFormat dateStartedValidate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        final String fspDateformat = dateStartedValidate.format(date);

        btnsaveSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                forceStopPeriod(i.getStringExtra("group_id"),getday,fspDateformat);
            }
        });

    }

    private ProgressDialog pd;
    public void forceStopPeriod(final String groupId, final String day,final String fspdate){

        pd = new ProgressDialog(context);
        pd.setMessage("Starting...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();

        String url = "http://abza.000webhostapp.com/force-stop-period.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);

                            int success = json.getInt("status");
                            if (success == 0) {
                                Toast.makeText(context, "Force Stop Period not able to start.", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Set Force Stop Period success....", Toast.LENGTH_SHORT).show();
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
                params.put("day", day);
                params.put("fspdate",fspdate);
                Log.e("Response", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

    }

    public void getSavedDay(final String groupId){

        pd = new ProgressDialog(context);
        pd.setMessage("Starting...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();

        String url = "http://abza.000webhostapp.com/get-force-stop-period.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            int daystatus = json.getInt("status");
                            if (daystatus == 1){
                                String savedday = json.getString("FSP");
                                String replaceneeded = "Your Force Stop Period was set on *";
                                String replaceDate = replaceneeded.replace("*",savedday);
                                txtgetday.setText(replaceDate);
                                txtgetday.setVisibility(View.VISIBLE);

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
                Log.e("Response", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}
