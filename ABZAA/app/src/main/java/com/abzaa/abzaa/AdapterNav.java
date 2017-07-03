package com.abzaa.abzaa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

/**
 * Created by Zarul Izham on 9/3/2016.
 */
public class AdapterNav extends BaseAdapter {

    Context context;
    ArrayList<ObjNav> listTodo;
    private int pos;
    Activity act;

    public AdapterNav(Context context, ArrayList<ObjNav> listTodo, Activity activity) {
        super();
        this.context = context;
        this.listTodo = listTodo;
        act = activity;

    }

    @Override
    public int getCount() {
        return listTodo.size();
    }

    @Override
    public Object getItem(int position) {
        return listTodo.get(position);
    }

    public long getItemId(int position) {
        return listTodo.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pos = position;
        View row = v;
        v = null;
        YourWrapper wrapper;

        if (v == null) {
            v = mInflater.inflate(R.layout.drawer_child, parent, false);

            TextView lblGroup = (TextView)v.findViewById(R.id.lblGroup);
            TextView lblJuz = (TextView)v.findViewById(R.id.lblJuz);
            TextView lblCompleted = (TextView)v.findViewById(R.id.lblCompleted);
            ImageView imgPic = (ImageView) v.findViewById(R.id.imgPic);

            final ObjNav todo = listTodo.get(position);

            lblGroup.setText(todo.getGroup());
            lblJuz.setText(todo.getJuz());
            lblCompleted.setText(todo.getCompleted());

//            final String url = "http://abza.000webhostapp.com/group-picture/" + todo.getPicture();
            final String url = "http://abza.000webhostapp.com/group-picture/" + todo.getPicture();

            UrlImageViewHelper.setUrlDrawable(imgPic, url, R.mipmap.ic_launcher, 60000 * 60);

            wrapper = new YourWrapper (v);
            ImageView imgBtn = wrapper.getImg();

            imgBtn.setFocusable(false);
            imgBtn.setFocusableInTouchMode(false);

            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditPicture.class);
                    //Log.e("GroupIdAdapter", todo.getId());
                    i.putExtra("groupId", todo.getId());
                    i.putExtra("groupName", todo.getGroup());
                    i.putExtra("picture", todo.getPicture());
                    act.startActivityForResult(i, 2);

                }
            });

        } else {
            wrapper = (YourWrapper) row.getTag();
        }
        return v;
    }

    public class YourWrapper
    {
        private View base;
        private ImageView imgDone;

        public YourWrapper(View base)
        {
            this.base = base;
        }

        public ImageView getImg() {
            if (imgDone == null) {
                imgDone = (ImageView) base.findViewById(R.id.imgPic);
            }
            return (imgDone);
        }

    }


}

