package com.abzaa.abzaa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zarulizham on 19/04/2016.
 */
public class AdapterChampList extends BaseAdapter {
    Context context;
    ArrayList<ObjChampList> listNews;
    private int pos;

    public AdapterChampList(Context context, ArrayList<ObjChampList> listNews) {
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
            v = mInflater.inflate(R.layout.child_champ_list, parent, false);

            TextView lblName = (TextView)v.findViewById(R.id.lblName);
            TextView lblCount = (TextView)v.findViewById(R.id.lblCount);

            ObjChampList schedule = listNews.get(position);

            lblName.setText(schedule.getName());
            lblCount.setText(schedule.getCount());
        }
        return v;
    }
}
