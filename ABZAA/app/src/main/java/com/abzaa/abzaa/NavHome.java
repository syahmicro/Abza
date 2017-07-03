package com.abzaa.abzaa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zarul on 14/04/16.
 */
public class NavHome extends Fragment {

    public Activity act;
    public Context context;
    public TextView lblGroupName;
    public int index;
    public String juz_no;
    public GridView gridView;
    public AlertDialog.Builder adb;
    public AlertDialog ad;
    public DBHelper dbHelper;
    public ArrayList<ObjGroupUser> gu;

    public static NavHome newInstance(int index, String group_id, String group_name, String juz_no) {
        NavHome fragment = new NavHome();
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("group_id", group_id);
        args.putString("group_name", group_name);
        args.putString("juz_no", juz_no);
        fragment.setArguments(args);
        return fragment;
    }

    public NavHome() {

    }

    public void init(View v) {
        dbHelper = new DBHelper(context);
        lblGroupName = (TextView) v.findViewById(R.id.lblGroupName);
        gridView = (GridView) v.findViewById(R.id.gridView);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle b) {
        View rootView = inflater.inflate(R.layout.nav_home, container, false);
        init(rootView);
        index = this.getArguments().getInt("index", 0);
        juz_no = this.getArguments().getString("juz_no", "1");
        final String group_id = this.getArguments().getString("group_id", "0");
        String group_name = this.getArguments().getString("group_name", "Group Name");

        lblGroupName.setText(group_name);

        ArrayList<ObjGrid> list = new ArrayList<>();
        list.add(new ObjGrid("1", "Send", R.drawable.send));
        list.add(new ObjGrid("2", "Quran", R.drawable.quran));
        list.add(new ObjGrid("3", "Stats", R.drawable.stat));
        list.add(new ObjGrid("4", "Profile", R.drawable.profile));
        list.add(new ObjGrid("5", "Name List", R.drawable.list));
        list.add(new ObjGrid("6", "Tips & Extras", R.drawable.tips));
        list.add(new ObjGrid("7", "Group", R.drawable.group));

        AdapterGrid adapter = new AdapterGrid(context, list);
        gridView.setAdapter(adapter);
        gu = dbHelper.getSingleGroupUser(group_id);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    if (gu.get(0).getJuz_read().equalsIgnoreCase("not started")) {
                        Toast.makeText(context, "Group did not started yet.", Toast.LENGTH_SHORT).show();
                    } else {

                        new AlertDialog.Builder(context)
                                .setMessage("Did you have completed your task?\n\nIf you do please proceed.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferences sp = act.getSharedPreferences("Profile", 0);
                                        setRead(group_id, sp.getString("id", "0"));
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                } else if (i == 1) {
                    openApp(context, "com.guidedways.iQuranPro");
                } else if (i == 2) {
                    if (gu.get(0).getJuz_read().equalsIgnoreCase("not started")) {
                        Toast.makeText(context, "Group did not started yet.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, Stat.class);
                        intent.putExtra("group_id", group_id);
                        startActivity(intent);
                    }
                } else if (i == 3) {
                    Intent intent = new Intent(context, Profile.class);
                    startActivity(intent);
                } else if (i == 4) {
                    Intent intent = new Intent(context, NameList.class);
                    intent.putExtra("group_id", group_id);
                    startActivity(intent);
                } else if (i == 5) {
                    Intent intent = new Intent(context, Tips.class);
                    intent.putExtra("juz_no", juz_no);
                    startActivity(intent);
                } else if (i == 6) {
                    ad.show();
                }
            }
        });

        //LayoutInflater li = LayoutInflater.from(getActivity());
        LayoutInflater li = LayoutInflater.from(context);
        final View v = li.inflate(R.layout.grid7, null);
        Button btnCreate = (Button) v.findViewById(R.id.btnCreate);
        Button btnJoin = (Button) v.findViewById(R.id.btnJoin);
        Button btnStart = (Button) v.findViewById(R.id.btnStart);
        Button btnStop = (Button) v.findViewById(R.id.btnStop);
        Button btnForceStop = (Button) v.findViewById(R.id.btnForceStop);

        ArrayList<ObjGroupUser> singleList = dbHelper.getSingleGroupUser(group_id);
        String stat = singleList.get(0).getGstatus();
        String userId = singleList.get(0).getUser_id();
        String createdBy = singleList.get(0).getCreated_by();
        Log.e("userid", userId);
        Log.e("createdby", createdBy);

        if (stat.equalsIgnoreCase("active")) {
            btnStart.setVisibility(View.GONE);

        } else {
//            if (!userId.equalsIgnoreCase(createdBy)) {
//                btnStart.setVisibility(View.GONE);
//                btnStop.setVisibility(View.GONE);
//                btnForceStop.setVisibility(View.GONE);
//            }
            btnStart.setVisibility(View.VISIBLE);
        }

        if (!userId.equalsIgnoreCase(createdBy)) {
            btnStart.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);
            btnForceStop.setVisibility(View.GONE);
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CreateNewGroupLoggedIn.class);
                startActivityForResult(i, 1);
                ad.dismiss();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, JoinNewGroup.class);
                startActivityForResult(i, 2);
                ad.dismiss();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, StartPeriod.class);
                i.putExtra("group_id", group_id);
                startActivity(i);
                ad.dismiss();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, StopPeriod.class);
                i.putExtra("group_id", group_id);
                startActivity(i);
                ad.dismiss();
            }
        });

        btnForceStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ForceStopPeriod.class);
                i.putExtra("group_id", group_id);
                startActivity(i);
                ad.dismiss();
            }
        });

        adb = new AlertDialog.Builder(context);
        adb.setView(v);
        ad = adb.create();

        return rootView;
    }

    @Override
    public void onAttach(Context c) {
        super.onAttach(c);
        act = (Activity) c;
        context = c;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                Toast.makeText(context, "There's no iQuran found.", Toast.LENGTH_LONG).show();
                return false;
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "There's no iQuran found.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private ProgressDialog pd;
    public void setRead(final String group_id, final String user_id) {
        pd = new ProgressDialog(context);
        pd.setMessage("Updating...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();

        String url = "http://abza.000webhostapp.com/set-read.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pd2.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {
                                new AlertDialog.Builder(context)
                                        .setMessage("You've submitted :)")
                                        .setPositiveButton("OK", null)
                                        .show();
                            } else if (success == 1) {
                                int totalCompleted = json.getInt("totalCompleted");
                                String msg = "You have completed " + totalCompleted + " juz.";
                                new AlertDialog.Builder(context)
                                        .setMessage(msg)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, "Server error.", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}
