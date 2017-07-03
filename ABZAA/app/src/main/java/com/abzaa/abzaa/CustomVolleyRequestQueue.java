package com.abzaa.abzaa;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by zarulizham on 10/05/2016.
 */
public class CustomVolleyRequestQueue {

    private static CustomVolleyRequestQueue mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    private CustomVolleyRequestQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get("1" + url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        int scaleWidth = mCtx.getResources().getDisplayMetrics().widthPixels;
                        int scaleHeight = mCtx.getResources().getDisplayMetrics().heightPixels;

                        Bitmap decoded = resize2(bitmap, scaleWidth, scaleHeight);
                        cache.put("1" + url, decoded);
                    }
                });
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CustomVolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 20 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    private static Bitmap resize2(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {

            int width = image.getWidth();
            int height = image.getHeight();

            if (width > maxWidth || height > maxHeight) {
                //Log.e("image", width + "-" + height);
                //Log.e("max", maxWidth + "-" + maxHeight);

                float ratW = maxWidth / (float) width;
                float ratH = maxHeight / (float) height;

                //Log.e("ratio", ratW + "-" + ratH);

                float finalW = 0, finalH = 0;

                if (ratW < ratH) {
                    finalW = width * ratW;
                    finalH = height * ratW;
                } else {
                    finalW = width * ratH;
                    finalH = height * ratH;
                }

                Log.e("FinalWidth", (int) finalW + "");
                Log.e("FinalHeight", (int) finalH + "");
                image = Bitmap.createScaledBitmap(image, (int) finalW, (int) finalH, true);
                return image;
            }
            return image;
        } else {
            return image;
        }
    }
}

