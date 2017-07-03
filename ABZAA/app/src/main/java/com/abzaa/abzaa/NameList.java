package com.abzaa.abzaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zarul on 15/04/16.
 */
public class NameList extends AppCompatActivity {

    private ListView lv;
    private Context context;
    ArrayList<ObjNameList> list;
    private Toolbar toolbar;
    private TextView lblName, lblWeek, lblDate, lblRound;

    public void init() {
        context = NameList.this;
        lv = (ListView) findViewById(R.id.listView2);
        list = new ArrayList<>();

        lblName = (TextView) findViewById(R.id.lblName);
        lblWeek = (TextView) findViewById(R.id.lblWeek);
        lblDate = (TextView) findViewById(R.id.lblDate);
        lblRound = (TextView) findViewById(R.id.lblRound);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.name_list);
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

        Intent i = getIntent();
        SharedPreferences sp = getSharedPreferences("Profile", 0);


        getName(i.getStringExtra("group_id"), sp.getString("id", "0"));
    }

    public Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public boolean validationLateStatus (String day,String datestarted, String dateperiod){
        boolean stat = false;
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        SimpleDateFormat dateStartedValidate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        String dayFormat = sdf_.format(date);
        String dateFormat = dateStartedValidate.format(date);
        Date timeFormat = getEndOfDay(date);


//            //after clicked btn stop period, while go to the group btn, btn start period will appear(refreshing)
//            //after clicked btn start period, while go to the group btn,btn start btn will disappear(refreshing)
//            //After set Stop Period, then dateline will update to NOT Started, but when clicked JUZ it will update as completed. It should not like that


            //check if group is Not started
            if (day.equalsIgnoreCase("Not started"))
            {
                return false;
            }

            try {
                Date periodDateparse =  dateStartedValidate.parse(dateperiod);
                Date currentDate = dateStartedValidate.parse(dateFormat);
                if (periodDateparse.compareTo(currentDate) > 0) {
                    System.out.println("period Date is bigger then current date, so you have time to read");
                } else if (periodDateparse.compareTo(currentDate) < 0) {
                    if (dayFormat.equalsIgnoreCase(day))
                    {
                        String removeTime = datestarted.replaceAll(" .*", "");
                        if(removeTime.equals(dateFormat)) //checking user joined group on current day
                        {
                            stat = false;
                        }
                        if (timeFormat.equals(date)){
                            Toast.makeText(context, "This is you Last day, Please finish your Juz", Toast.LENGTH_SHORT).show();
                            stat = true;
                        }
                    }
                    else {

                        Calendar calendar = Calendar.getInstance();

                        calendar.setTime(periodDateparse);
                        calendar.add(Calendar.DAY_OF_YEAR, +7);
                        Date newDate = calendar.getTime();
                        String date1 = dateFormat.format(newDate.toString());

                        //need apply validation only for pending status
                        //if completed sign/champion no need appear this message

                        if ((newDate.compareTo(currentDate) < 0)){
                            stat = true;
                            Toast.makeText(context, "You are late!!!. Thus you need to pay for penalty", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                else if (periodDateparse.compareTo(currentDate) == 0) {
                    System.out.println("your are in current date");
                    String removeTime = datestarted.replaceAll(" .*", "");
                    if(removeTime.equals(dateFormat)) //checking user joined group on current day
                    {
                        stat = false;
                    }
                } else {
                    System.out.println("failed?");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return stat;
    }

    public boolean validationForceStopPeriod(String dateperiod,String forceStopPeriod,String stopperiodstat){
        boolean stat = false;
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        SimpleDateFormat dateStartedValidate = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String dayFormat = sdf_.format(date);
        String dateFormat = dateStartedValidate.format(date);

        //if stop period check before fsp thus it will not set user to pending
        if (stopperiodstat.equalsIgnoreCase("1")){
            stat = false;
        }
        else {
            //check if force stop period was assign or not. if assigned so waiting until fsp date and automatically set to pending
            Calendar calendar = Calendar.getInstance();
            Date dateperiodparse = null;
            Date forstopperiod = null;

            try {
                dateperiodparse = dateStartedValidate.parse(dateperiod);
                forstopperiod = dateStartedValidate.parse(forceStopPeriod);

                String forstopperiodday = sdf_.format(forstopperiod);

                calendar.setTime(dateperiodparse);
                calendar.add(Calendar.DAY_OF_YEAR, +7);
                Date newDate = calendar.getTime();
                String date1 = dateFormat.format(newDate.toString());


            if ((newDate.compareTo(forstopperiod) < 0)){
                if (dayFormat.equalsIgnoreCase(forstopperiodday))
                {
                    stat = true;
                    Toast.makeText(context, "Your Force Stop Period is due,all users updated to pending", Toast.LENGTH_SHORT).show();
                }
            }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //After Status being late, if it is not click STOP PERIOD thus it remain red until it match force stop period. For stop period will update status to PENDING
        //if STOP Period btn clicked thus status automatically become Pending even The Days is match
        return stat;
    }

    public String validationCheckCompleted(ArrayList<String> status)
    {
        //if user late but he/she completed after dateline it will update late to completed//change icon
        String statuscompletion = "null";
        ArrayList<String> statuscompleted = new ArrayList<>();
        statuscompleted.add("Completed");

        for (int i = 0; i < status.size(); i++) {
            if (status.contains(statuscompleted)){
                statuscompletion = "Completed";
            }
        }
        return statuscompletion;
    }

    public String validationCheckAllCompleted(ArrayList<String> statusCompleted)
    {
        //When all completed,appear messages and automatically set status to pending
        //only group admin got notifications
        String statusAllCompleted = "null";
        ArrayList<String> status = new ArrayList<>();
        status.add("Completed");

            if (statusCompleted.containsAll(status)){
                statusAllCompleted = "Completed";
            }

        return statusAllCompleted;
    }


    private ProgressDialog pd;
    public void getName(final String group_id, final String user_id) {
        pd = new ProgressDialog(context);
        pd.setMessage("Refreshing list...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();

        String url = "http://abza.000webhostapp.com/get-name-list.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject json = new JSONObject(response);

                            int success = json.getInt("status");
                            if (success == 0) {

                                Toast.makeText(context, "There is no members yet", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Successfully refreshed", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray = json.getJSONArray("list");
                                int size = jsonArray.length();

                                lblName.setText(json.getString("name"));
                                lblWeek.setText(json.getString("week"));
                                lblDate.setText(json.getString("date"));
                                lblRound.setText(json.getString("round"));
                                ArrayList<String> allStatus = new ArrayList<>();

                                list.add(new ObjNameList("No", "Name", "Juz", "Status", "Status","picture"));

                                for (int a=0; a<size; a++) {
                                    JSONObject j = jsonArray.getJSONObject(a);
                                    list.add(new ObjNameList(
                                            Integer.toString(a+1),
                                            j.getString("name"),
                                            j.getString("juz_read"),
                                            j.getString("champ"),
                                            j.getString("status"),
                                            j.getString("picture")

                                    ));
                                    allStatus.add(j.getString("status"));
                                }

//                                AdapterNameList adapter = new AdapterNameList(context, list);
//                                lv.setAdapter(adapter);

                                String currentDay = (String) json.get("currentdate");
                                String datestarted = (String) json.get("datestarted");
                                String dateperiod = (String) json.get("dateperiod");
                                String stopperiod = (String) json.get("stopperioddate");
                                String fspdateperiod = (String) json.get("force_stop_perioddate");
                                String stopperiodStatus = (String) json.get("stopperiodstatus");
                                if (validationLateStatus(currentDay,datestarted,dateperiod) == true){
                                    if (validationCheckCompleted(allStatus).equalsIgnoreCase("Completed"))
                                    {
                                        Toast.makeText(context, "Thanks for Completing your Read", Toast.LENGTH_SHORT).show();
                                    }
                                    if (validationForceStopPeriod(dateperiod,fspdateperiod,stopperiodStatus)){
                                        String url_allcompleted = "http://abza.000webhostapp.com/set-new-week.php";

                                        StringRequest postRequestSetLate = new StringRequest(Request.Method.POST, url_allcompleted,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.e("Response", response);
                                                        try {
                                                            JSONObject json = new JSONObject(response);

                                                            int success = json.getInt("status");
                                                            if (success == 1) {
                                                                Toast.makeText(context, "New week Started!. All members status are pending", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            Toast.makeText(context, "Set as Pending Failed.", Toast.LENGTH_SHORT).show();
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
                                                params.put("group_id", group_id);
                                                params.put("user_id", user_id);
                                                return params;
                                            }
                                        };
                                        Volley.newRequestQueue(context).add(postRequestSetLate);
                                    }
                                    else {
                                        String url2 = "http://abza.000webhostapp.com/set-late.php";
//
                                        StringRequest postRequestSetLate = new StringRequest(Request.Method.POST, url2,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.e("Response", response);
                                                        try {
                                                            JSONObject json = new JSONObject(response);

                                                            int success = json.getInt("status");
                                                            if (success == 0) {
                                                                Toast.makeText(context, "Late update Failed", Toast.LENGTH_SHORT).show();
                                                            } else if (success == 1) {
                                                                //Toast.makeText(context, "Late update Success", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            Toast.makeText(context, "Set as Late Failed.", Toast.LENGTH_SHORT).show();
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
                                                params.put("group_id", group_id);
                                                params.put("user_id", user_id);
                                                return params;
                                            }
                                        };
                                        Volley.newRequestQueue(context).add(postRequestSetLate);
                                    }

                                }

                                //validate all completed
//                                if (validationCheckAllCompleted(allStatus).equalsIgnoreCase("Completed")){
//
//                                    String url_allcompleted = "http://abza.000webhostapp.com/set-allcompleted.php";
//
//                                    StringRequest postRequestSetnewweek = new StringRequest(Request.Method.POST, url_allcompleted,
//                                            new Response.Listener<String>() {
//                                                @Override
//                                                public void onResponse(String response) {
//                                                    Log.e("Response", response);
//                                                    try {
//                                                        JSONObject json = new JSONObject(response);
//
//                                                        int success = json.getInt("status");
//                                                        if (success == 0) {
//                                                            Toast.makeText(context, "Some member's not completing yet", Toast.LENGTH_SHORT).show();
////                                                            new AlertDialog.Builder(context)
////                                                                    .setMessage("Did you have completed your task?\n\nIf you do please proceed.")
////                                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
////                                                                        @Override
////                                                                        public void onClick(DialogInterface dialogInterface, int i) {
////                                                                            SharedPreferences sp = act.getSharedPreferences("Profile", 0);
////                                                                            setRead(group_id, sp.getString("id", "0"));
////                                                                        }
////                                                                    })
////                                                                    .setNegativeButton("Cancel", null)
////                                                                    .show();
//                                                        } else if (success == 1) {
//                                                            Toast.makeText(context, "All member's are completed", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    } catch (JSONException e) {
//                                                        Toast.makeText(context, "Set as Pending Failed.", Toast.LENGTH_SHORT).show();
//                                                        Log.e("VolleyError", e.toString());
//                                                    }
//                                                    pd.dismiss();
//                                                }
//                                            },
//                                            new Response.ErrorListener() {
//                                                @Override
//                                                public void onErrorResponse(VolleyError error) {
//                                                    Toast.makeText(context, "Please connect to internet.", Toast.LENGTH_SHORT).show();
//                                                    error.printStackTrace();
//                                                    pd.dismiss();
//                                                }
//
//                                            }
//                                    ) {
//                                        @Override
//                                        protected Map<String, String> getParams() {
//                                            Map<String, String> params = new HashMap<>();
//                                            params.put("group_id", group_id);
//                                            params.put("user_id", user_id);
//                                            return params;
//                                        }
//                                    };
//                                    Volley.newRequestQueue(context).add(postRequestSetnewweek);
//
//                                }

                                AdapterNameList adapter = new AdapterNameList(context, list);
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
                        Toast.makeText(context, "Please connect to internet.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        pd.dismiss();
                    }

                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("group_id", group_id);
                params.put("user_id", user_id);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }
}
