package com.abzaa.abzaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
 * Created by zarul on 14/04/16.
 */
public class NoGroup extends AppCompatActivity {

    Button btnCreate, btnJoin;
    private Context context;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private DBHelper dbHelper;

    public void init() {
        dbHelper = new DBHelper(this);
        context = this;
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnJoin = (Button) findViewById(R.id.btnJoin);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.no_group);
        init();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoGroup.this, CreateNewGroup.class);
                startActivity(i);
                finish();
            }
        });

        LayoutInflater li = LayoutInflater.from(context);
        final View join = li.inflate(R.layout.group_join, null);
        final EditText txtName = (EditText) join.findViewById(R.id.txtName);
        final EditText txtPasscode = (EditText) join.findViewById(R.id.txtPasscode);

        adb = new AlertDialog.Builder(NoGroup.this);
        adb.setView(join)
                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sp = getSharedPreferences("Profile", 0);
                        joinGroup(txtName.getText().toString(), txtPasscode.getText().toString(), sp.getString("id", "0"));
                    }
                })
                .setNegativeButton("Cancel", null);
        ad = adb.create();

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();

            }
        });

    }

    private ProgressDialog pd;
    public void joinGroup(final String name, final String passcode, final String user_id) {
        pd = new ProgressDialog(context);
        pd.setMessage("Searching group...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
//        String url = "http://www.tetunpai.net/abzaa/join-group.php";
        String url = "http://abza.000webhostapp.com/join-group.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        // pd2.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {

                                Toast.makeText(context, "Group not found", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "You have join group", Toast.LENGTH_SHORT).show();
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
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
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
