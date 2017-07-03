package com.abzaa.abzaa;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.abzaa.abzaa.R.id.imgView;

/**
 * Created by zarul on 15/04/16.
 */
public class AdapterNameList extends BaseAdapter {
    Context context;
    ArrayList<ObjNameList> listNews;
    private int pos;

    public AdapterNameList(Context context, ArrayList<ObjNameList> listNews) {
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
            v = mInflater.inflate(R.layout.child_name_list, parent, false);

            TextView lblIndex = (TextView)v.findViewById(R.id.lblIndex);
            TextView lblName = (TextView)v.findViewById(R.id.lblName);
            TextView lblJuz = (TextView)v.findViewById(R.id.lblJuz);
            TextView lblStatus = (TextView)v.findViewById(R.id.lblStatus);
            TextView lblChampion = (TextView)v.findViewById(R.id.textChampion);
            TextView lblPending = (TextView)v.findViewById(R.id.textPending);
            TextView lblGreenRightSign = (TextView)v.findViewById(R.id.textGreenRightSign);
            TextView lblStatusDateline = (TextView)v.findViewById(R.id.lblDate);
            ImageView img = (ImageView) v.findViewById(R.id.imgChamp);
            ImageView img2 = (ImageView) v.findViewById(R.id.imgPending);
            ImageView img3 = (ImageView) v.findViewById(R.id.imgRightSign);
            ImageView img4 = (ImageView) v.findViewById(R.id.imgredcircle);
            ImageView img5 = (ImageView) v.findViewById(R.id.imguserprofile);
            LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll2);
            //FrameLayout frame = (FrameLayout)v.findViewById(R.id.frame);


            ObjNameList schedule = listNews.get(position);

            lblIndex.setText(schedule.getIndex());
            lblName.setText(schedule.getName());
            lblJuz.setText(schedule.getJuz());
            lblStatus.setText(schedule.getStatus());

            String champ = schedule.getChamp();
            String stat = schedule.getStatus();
            String picture = schedule.getPicture();

            //================================================================================
            SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
            Date date = new Date();

            String dayFormat = sdf_.format(date);

            //================================================================================

            //initialize components
            //frame.setVisibility(View.GONE);
            img5.setImageDrawable(null);
            img4.setVisibility(View.GONE);


            if (champ.equalsIgnoreCase("1")) {
                img.setVisibility(View.VISIBLE);
//                img.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
                lblStatus.setVisibility(View.GONE);
//                lblChampion.setVisibility(View.VISIBLE);
            } else {
                img.setVisibility(View.GONE);
            }

            if (stat.equalsIgnoreCase("Completed")){
                img3.setVisibility(View.VISIBLE);
                lblStatus.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
//                img3.setVisibility(View.GONE);
//                lblGreenRightSign.setVisibility(View.VISIBLE);
                if (champ.equalsIgnoreCase("1")){
                    img3.setVisibility(View.GONE);
//                    lblGreenRightSign.setVisibility(View.GONE);
                }
            }
            else {
                img3.setVisibility(View.GONE);
            }

            if (stat.equalsIgnoreCase("Late")){
                img3.setVisibility(View.GONE);
                lblStatus.setVisibility(View.GONE);
                img2.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                img4.setVisibility(View.VISIBLE);
            }
            else {
                img4.setVisibility(View.GONE);
            }

            if (stat.equalsIgnoreCase("Pending")){
                img2.setVisibility(View.VISIBLE);
                lblStatus.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
//                img2.setVisibility(View.GONE);
                img3.setVisibility(View.GONE);
//                lblPending.setVisibility(View.VISIBLE);
            }
            else {
                img2.setVisibility(View.GONE);
            }

            if (!picture.equalsIgnoreCase("")){
                final String url = "http://abza.000webhostapp.com/profile-picture/" + picture;
                Log.e("url", url);
                UrlImageViewHelper.setUrlDrawable(img5, url, R.mipmap.ic_launcher, 6000 * 60);
                img5.setVisibility(View.VISIBLE);
            }
            else {
                img5.setImageResource(R.mipmap.ic_launcher);
                //img5.setVisibility(View.GONE);
            }

            if (picture.equalsIgnoreCase("picture")){
                img5.setVisibility(View.INVISIBLE);
            }


            if (schedule.getIndex().equalsIgnoreCase("no")) {
                ll.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        }
        return v;
    }
}
