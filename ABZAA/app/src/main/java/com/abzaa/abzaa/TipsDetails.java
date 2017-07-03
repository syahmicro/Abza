package com.abzaa.abzaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zarulizham on 07/08/2016.
 */
public class TipsDetails extends AppCompatActivity {

    private TextView lblTitle, lblInfo;
    private Toolbar toolbar;

    public void init() {
        lblTitle = (TextView) findViewById(R.id.lblTitle);
        lblInfo = (TextView) findViewById(R.id.lblInfo);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.tips_details);
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
        Intent intent = getIntent();
        lblTitle.setText(intent.getStringExtra("title"));
        lblInfo.setText(intent.getStringExtra("info"));
    }
}
