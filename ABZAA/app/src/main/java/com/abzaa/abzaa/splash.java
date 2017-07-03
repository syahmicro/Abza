package com.abzaa.abzaa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Zarul Izham on 9/3/2016.
 */
public class splash extends AppCompatActivity {

    Button btnRegister;
    TextView btnLogin;
    Context context;

    public void init() {
        context = splash.this;
        btnRegister = (Button) findViewById(R.id.btnStart);
        btnLogin = (TextView) findViewById(R.id.btnLogin);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.splash);
        init();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Register.class);
                startActivityForResult(i, 1);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            Intent i = new Intent(splash.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }
}
