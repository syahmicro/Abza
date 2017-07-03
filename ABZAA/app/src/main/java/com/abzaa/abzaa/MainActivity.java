package com.abzaa.abzaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public ListView lv;
    public DrawerLayout drawer;
    public DBHelper dbHelper;
    public ArrayList<ObjGroupUser> gu;
    public Toolbar toolbar;
    public Context context;
    public ImageView imgToolbar;
    public TextView lblToolbar;
    public int currentIndex = 0;

    public void init() {
        context = MainActivity.this;
        dbHelper = new DBHelper(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv = (ListView) findViewById(R.id.listView);

        imgToolbar = (ImageView) findViewById(R.id.imgToolbar);
        lblToolbar = (TextView) findViewById(R.id.lblToolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();


        cacheLv();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentIndex = i;
                lv.setItemChecked(i, true);
                lblToolbar.setText(gu.get(i).getGroup_name());
                final String url = "http://abza.000webhostapp.com/group-picture/" + gu.get(i).getGroup_picture();
                UrlImageViewHelper.setUrlDrawable(imgToolbar, url, R.mipmap.ic_launcher, 6000 * 60);
                getSupportFragmentManager().beginTransaction().replace(R.id.ll, NavMain.newInstance(i, gu.get(i).group_id, gu.get(i).getGroup_name(), gu.get(i).getJuz_read())).commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        SharedPreferences sp = getSharedPreferences("Profile", 0);
        if (!sp.getBoolean("isLogin", false)) {
            Intent i = new Intent(this, splash.class);
            startActivity(i);
            finish();
        } else {
            if (dbHelper.groupUserRows() == 0) {
                Intent i = new Intent(this, NoGroup.class);
                startActivity(i);
                finish();
            } else {
                refreshInfo(sp.getString("phone", "0"), sp.getString("pwd", "0"));
                lv.setItemChecked(0, true);
                lblToolbar.setText(gu.get(0).getGroup_name());
                final String url = "http://abza.000webhostapp.com/group-picture/" + gu.get(0).getGroup_picture();
                UrlImageViewHelper.setUrlDrawable(imgToolbar, url, R.mipmap.ic_launcher, 6000 * 60);
                getSupportFragmentManager().beginTransaction().replace(R.id.ll, NavMain.newInstance(0, gu.get(0).group_id, gu.get(0).getGroup_name(), gu.get(0).getJuz_read())).commit();

            }
        }


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.donate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent i = new Intent(this, About.class);
            startActivity(i);
        } else if (id == R.id.setting) {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
        } else if (id == R.id.feedback) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","abc@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else if (id == R.id.logout) {
            SharedPreferences sp = getSharedPreferences("Profile", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

        } else if (id == R.id.donate) {
            String url = "http://abza.000webhostapp.com/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private ProgressDialog pd;
    public void refreshInfo(final String phone, final String pwd) {
        pd = new ProgressDialog(context);
        pd.setMessage("Refreshing...");
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

                                Toast.makeText(context, "Refresh failed.", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
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

                                cacheLv();
                                dbHelper.closeConnection();
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
                params.put("phone", phone);
                params.put("password", pwd);
                Log.e("Params", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }

    private void cacheLv() {
        ArrayList<ObjNav> list = new ArrayList<>();
        gu = dbHelper.getGroupUser();
        for (int a=0; a<dbHelper.groupUserRows(); a++) {
            Log.e("GroupID", gu.get(a).getGroup_id());
            list.add(new ObjNav(
                    gu.get(a).getGroup_id(),
                    gu.get(a).getGroup_name(),
                    "Juz " + gu.get(a).getJuz_read(),
                    "10% completed",
                    gu.get(a).getGroup_picture()
            ));
        }
        AdapterNav adapter = new AdapterNav(this, list, this);

        lv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sp = getSharedPreferences("Profile", 0);
        refreshInfo(sp.getString("phone", "0"), sp.getString("pwd", "0"));
        cacheLv();
        final String url = "http://abza.000webhostapp.com/group-picture/" + gu.get(currentIndex).getGroup_picture();
        UrlImageViewHelper.setUrlDrawable(imgToolbar, url, R.mipmap.ic_launcher, 6000 * 60);
        getSupportFragmentManager().beginTransaction().replace(R.id.ll, NavMain.newInstance(currentIndex, gu.get(currentIndex).group_id, gu.get(currentIndex).getGroup_name(), gu.get(0).getJuz_read())).commit();
        drawer.closeDrawer(GravityCompat.START);
    }

}
