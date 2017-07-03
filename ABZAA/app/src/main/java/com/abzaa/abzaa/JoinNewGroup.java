package com.abzaa.abzaa;

import android.content.Context;
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
 * Created by zarul on 15/04/16.
 */
public class JoinNewGroup extends AppCompatActivity {

    private EditText txtName, txtPasscode;
    private Button btnJoin;
    private Context context;
    private Toolbar toolbar;

    public void init() {
        context = JoinNewGroup.this;
        txtName = (EditText) findViewById(R.id.txtName);
        txtPasscode = (EditText) findViewById(R.id.txtPasscode);

        btnJoin = (Button) findViewById(R.id.btnJoin);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.join_new_group);
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

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("Profile", 0);
                joinGroup(
                        txtName.getText().toString(),
                        txtPasscode.getText().toString(),
                        sp.getString("id", "0")
                );
            }
        });
    }

    public void joinGroup(final String name, final String passcode, final String user_id) {
//        String url = "http://www.tetunpai.net/abzaa/join-group.php";
        String url = "http://abza.000webhostapp.com/join-group.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pd2.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {

                                Toast.makeText(context, "Group not found", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "You have joined the group", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (success == 2) {
                                Toast.makeText(context, "Already join that group", Toast.LENGTH_SHORT).show();
                            } else if (success == 3) {
                                Toast.makeText(context, "Sorry, group full.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "Server error.", Toast.LENGTH_SHORT).show();
                            Log.e("VolleyError", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Please connect to internet.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("groupName", name);
                params.put("passcode", passcode);
                params.put("user_id", user_id);
                Log.e("Response", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}
