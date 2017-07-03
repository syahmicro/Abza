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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zarulizham on 06/08/2016.
 */
public class Tips extends AppCompatActivity {

    public ListView lv;
    private Context context;
    private Toolbar toolbar;
    private ArrayList<ObjTips> list;

    public void init() {
        lv = (ListView) findViewById(R.id.lvTips);
        context = this;
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.tips);
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
        getTips(getIntent().getStringExtra("juz_no"));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, TipsDetails.class);
                intent.putExtra("title", list.get(i).getTitle());
                intent.putExtra("info", list.get(i).getInfo());
                startActivity(intent);
            }
        });
    }

    private ProgressDialog pd;
    public void getTips(final String juz_no) {
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
//        String url = "http://www.tetunpai.net/abzaa/get-tips.php";
        String url = "http://abza.000webhostapp.com/get-tips.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            int success = json.getInt("status");
                            if (success == 0) {

                                Toast.makeText(context, "There are no tips & extras for this Juz", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                JSONArray jsonArray = json.getJSONArray("tips");
                                int size = jsonArray.length();

                                list = new ArrayList<>();
                                ArrayList listTitle = new ArrayList();
                                for (int a=0; a<size; a++) {
                                    JSONObject j = jsonArray.getJSONObject(a);
                                    list.add(new ObjTips(
                                            j.getString("id"),
                                            j.getString("juz_no"),
                                            j.getString("title"),
                                            j.getString("info"),
                                            j.getString("status")
                                    ));

                                    listTitle.add(j.getString("title"));

                                }

                                ArrayAdapter adapter = new ArrayAdapter(context,
                                        android.R.layout.simple_list_item_1, android.R.id.text1, listTitle);
                                lv.setAdapter(adapter);
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
                params.put("juz_no", juz_no);
                Log.e("Params", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}
