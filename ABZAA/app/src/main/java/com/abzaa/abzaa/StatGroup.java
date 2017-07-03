package com.abzaa.abzaa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
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
 * Created by zarul on 15/04/16.
 */
public class StatGroup extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    private Activity act;
    private TextView lblChamp, lblAvg, lblComplete;
    private String groupId;
    private ListView lv;

    public void init(View v) {
        lblChamp = (TextView) v.findViewById(R.id.lblChamp);
        lblAvg = (TextView) v.findViewById(R.id.lblAvg);
        lblComplete = (TextView) v.findViewById(R.id.lblComplete);
        lv = (ListView) v.findViewById(R.id.listView3);

    }

    public StatGroup() {
    }

    public static StatGroup newInstance(int sectionNumber, String groupId) {
        StatGroup fragment = new StatGroup();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("groupId", groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stat_group, container, false);
        init(rootView);
        groupId = this.getArguments().getString("groupId", "0");

        getStat();

        return rootView;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        act = (Activity) c;
        context = c;
    }

    private ProgressDialog pd;
    public void getStat() {
        pd = new ProgressDialog(context);
        pd.setMessage("Refreshing...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
//        String url = "http://www.tetunpai.net/abzaa/stat-group.php";
        String url = "http://abza.000webhostapp.com/stat-group.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {
                            } else if (success == 1) {
                                lblChamp.setText(json.getString("no_champ"));
                                lblAvg.setText(json.getString("avg_time"));
                                lblComplete.setText(json.getString("complete"));

                                JSONArray arr = json.getJSONArray("champList");
                                int size = arr.length();
                                ArrayList<ObjChampList> list = new ArrayList<>();

                                for (int a=0; a<size; a++) {

                                    JSONObject j = arr.getJSONObject(a);
                                    list.add(new ObjChampList(
                                            j.getString("name"),
                                            j.getString("count")
                                    ));
                                }

                                AdapterChampList adapter = new AdapterChampList(context, list);
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
                params.put("group_id", groupId);
                Log.e("Response", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}