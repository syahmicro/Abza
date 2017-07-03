package com.abzaa.abzaa;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zarul on 14/04/16.
 */
public class AdapterGrid extends BaseAdapter {
    Context context;
    ArrayList<ObjGrid> listNews;
    private int pos;

    public AdapterGrid(Context context, ArrayList<ObjGrid> listNews) {
        super();
        this.context = context;
        this.listNews = listNews;
    }

    @Override
    public int getCount() {
        return listNews.size();
    }

    @Override
    public Object getItem(int position) {
        return listNews.get(position);
    }

    public long getItemId(int position) {
        return listNews.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pos = position;
        v = null;

        if (v == null) {
            v = mInflater.inflate(R.layout.child_grid, parent, false);

            TextView lblTitle = (TextView)v.findViewById(R.id.lblTitle);
            ImageView img = (ImageView) v.findViewById(R.id.img);


            ObjGrid schedule = listNews.get(position);

            lblTitle.setText(schedule.getTitle());
            img.setImageResource(schedule.getPicture());
        }
        return v;
    }
}
