package com.abzaa.abzaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zarulizham on 06/05/2016.
 */
public class CreateNewGroupLoggedIn extends AppCompatActivity {

    private EditText txtName, txtPasscode;
    private Button btnCreate;
    private Context context;
    private Toolbar toolbar;
    private DBHelper dbHelper;

    public void init() {
        dbHelper = new DBHelper(this);
        context = this;
        txtName = (EditText) findViewById(R.id.txtName);
        txtPasscode = (EditText) findViewById(R.id.txtPasscode);
        btnCreate = (Button) findViewById(R.id.btnCreate);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.create_new_group);
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

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("Profile", 0);
                createNewGroup(sp.getString("id", "1"));
            }
        });

    }

    private ProgressDialog pd;
    public void createNewGroup(final String id) {
        pd = new ProgressDialog(context);
        pd.setMessage("Creating...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
//        String url = "http://www.tetunpai.net/abzaa/create-new-group.php";
        String url = "http://abza.000webhostapp.com/create-new-group.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        // pd2.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 2) {

                                Toast.makeText(context, "Please choose another passcode.", Toast.LENGTH_LONG).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Group successfully created.", Toast.LENGTH_SHORT).show();
                                dbHelper.insertGroup(
                                        json.getString("id"),
                                        json.getString("user_id"),
                                        json.getString("group_id"),
                                        json.getString("juz_no"),
                                        //json.getString("date_joined"),
                                        json.getString("group_name"),
                                        json.getString("group_picture"),
                                        json.getString("status1"),
                                        json.getString("gstatus"),
                                        json.getString("created_by")
                                );
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
                        Toast.makeText(context, "Please connect to internet.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        pd.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("groupName", txtName.getText().toString());
                params.put("passcode", txtPasscode.getText().toString());
                params.put("user_id", id);
                Log.e("params", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}
