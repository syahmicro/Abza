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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zarul on 14/04/16.
 */
public class Login extends AppCompatActivity {

    private EditText txtPhone, txtPwd;
    private Button btnLogin;
    Context context;
    DBHelper dbHelper;
    private Toolbar toolbar;

    public void init() {
        dbHelper = new DBHelper(this);
        context = Login.this;
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtPwd = (EditText) findViewById(R.id.txtPwd);

        btnLogin = (Button) findViewById(R.id.btnLogin);

    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.login);
        init();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, splash.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private ProgressDialog pd;
    public void login() {
        pd = new ProgressDialog(context);
        pd.setMessage("Logging in...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
        String url = "http://abza.000webhostapp.com/login.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            int success = json.getInt("status");
                            if (success == 0) {

                                Toast.makeText(context, "Login failed. Please check phone number and password.", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Succesfully Logged in", Toast.LENGTH_SHORT).show();
                                String fname = json.getString("fname");
                                String lname = json.getString("lname");
                                String phone = json.getString("phone");
                                String pwd = json.getString("pwd");
                                String id = json.getString("id");

                                SharedPreferences sp = getSharedPreferences("Profile", 0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("id", id);
                                editor.putString("fname", fname);
                                editor.putString("lname", lname);
                                editor.putString("phone", phone);
                                editor.putString("pwd", pwd);
                                editor.putBoolean("isLogin", true);
                                editor.apply();

                                JSONArray jsonArray = json.getJSONArray("groups");
                                int size = jsonArray.length();

                                dbHelper.clearGroupUser();

                                for (int a=0; a<size; a++) {
                                    JSONObject j = jsonArray.getJSONObject(a);
                                    dbHelper.insertGroup(
                                            j.getString("id"),
                                            j.getString("user_id"),
                                            j.getString("group_id"),
                                            j.getString("juz_no"),
                                            //j.getString("date_joined"),
                                            j.getString("group_name"),
                                            j.getString("group_picture"),
                                            j.getString("status"),
                                            j.getString("gstatus"),
                                            j.getString("created_by")
                                    );
                                }

                                Intent i = new Intent(context, MainActivity.class);
                                startActivity(i);
                                dbHelper.closeConnection();

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
                params.put("phone", txtPhone.getText().toString());
                params.put("password", txtPwd.getText().toString());
                Log.e("Params", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}
