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
import android.widget.TextView;
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
public class StatPersonal extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Activity act;
    private Context context;
    private TextView lblChamp, lblAvg, lblLate;


    public StatPersonal() {
    }

    public void init(View v) {
        lblChamp = (TextView) v.findViewById(R.id.lblChamp);
        lblAvg = (TextView) v.findViewById(R.id.lblAvg);
        lblLate = (TextView) v.findViewById(R.id.lblLate);

    }

    public static StatPersonal newInstance(int sectionNumber, String groupId) {
        StatPersonal fragment = new StatPersonal();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("groupId", groupId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stat_personal, container, false);
        init(rootView);

        SharedPreferences sp = act.getSharedPreferences("Profile", 0);
        getStat(sp.getString("id", "0"), this.getArguments().getString("groupId", "0"));

        return rootView;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        act = (Activity) c;
        context = c;
    }

    private ProgressDialog pd;
    public void getStat(final String user_id, final String group_id) {
        pd = new ProgressDialog(context);
        pd.setMessage("Retrieving statistic...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
//      String url = "http://www.tetunpai.net/abzaa/stat-personal.php";
        String url = "http://abza.000webhostapp.com/stat-personal.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {

                                Toast.makeText(context, "Group not found", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Statistic refreshed", Toast.LENGTH_SHORT).show();
                                lblChamp.setText(json.getString("no_champ"));
                                lblAvg.setText(json.getString("avg_time"));
                                lblLate.setText(json.getString("no_late"));
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
                params.put("user_id", user_id);
                params.put("group_id", group_id);
                Log.e("ResponsePers", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}