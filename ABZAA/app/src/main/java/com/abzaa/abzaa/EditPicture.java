package com.abzaa.abzaa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Zarul Izham on 9/3/2016.
 */
public class EditPicture extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView lblName;
    public AlertDialog.Builder adb;
    public AlertDialog ad;
    private Context context;
    private String id;
    private EditText txtName;
    private ImageButton btnEditName, btnChoosePic, btnSave;
    private ImageView imgView;
    private String compressed = "";
    private DBHelper dbHelper;

    public void init() {
        context = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lblName = (TextView) findViewById(R.id.lblName);
        btnEditName = (ImageButton) findViewById(R.id.btnEditName);
        btnChoosePic = (ImageButton) findViewById(R.id.btnChoosePic);
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        imgView = (ImageView) findViewById(R.id.imgView);
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.edit_picture);
        init();

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        Intent i = getIntent();
        id = i.getStringExtra("groupId");

        lblName.setText(i.getStringExtra("groupName"));

        LayoutInflater li = LayoutInflater.from(context);
        final View v = li.inflate(R.layout.edit_group_name, null);
        txtName = (EditText) v.findViewById(R.id.txtName);
        txtName.setText(i.getStringExtra("groupName"));

        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.show();
            }
        });

        btnChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressed = "";
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        adb = new AlertDialog.Builder(context);
        adb.setView(v)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeName(txtName.getText().toString(), id);
                    }
                });
        ad = adb.create();

        final String url = "http://abza.000webhostapp.com/group-picture/" + i.getStringExtra("picture");

        UrlImageViewHelper.setUrlDrawable(imgView, url, R.mipmap.ic_launcher, 6000 * 60);

    }

    private ProgressDialog pd;
    public void changeName(final String groupName, final String id) {
        pd = new ProgressDialog(context);
        pd.setMessage("Updating...");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
//        String url = "http://www.tetunpai.net/abzaa/edit-group-name.php";
        String url = "http://abza.000webhostapp.com/edit-group-name.php";


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            int success = json.getInt("status");
                            if (success == 0) {
                                Toast.makeText(context, "Rename error", Toast.LENGTH_SHORT).show();
                            } else if (success == 1) {
                                Toast.makeText(context, "Succesfully renamed.", Toast.LENGTH_SHORT).show();
                                lblName.setText(groupName);
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
                params.put("groupName", groupName);
                params.put("groupId", id);
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            imgView.setImageURI(selectedImage);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if(columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor

            compressImage(picturePath);
        } else if (requestCode == 10 && resultCode == RESULT_OK) {
            Log.e("Result", "Code10 + OK");
            setResult(RESULT_OK);
            finish();
        }


    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        //String filePath = photo;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            Log.e("Error 1", "Here");
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            Log.e("Error 2", "Here");
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            Log.e("Error 3", "Here");
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            imgView.setImageBitmap(scaledBitmap);
        } catch (Exception e) {
            Log.e("Error 4", "Here");
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "abzaa/group-photo");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        compressed = uriSting;
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);

        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private String UPLOAD_URL ="http://abza.000webhostapp.com/upload-group-picture.php";
    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading", null, false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Response", s);
                        loading.dismiss();

                        try {
                            JSONObject jsonResponse = new JSONObject(s);
                            int success = jsonResponse.getInt("status");
                            if (success == 1) {
                                Toast.makeText(context, "Group picture has been updated.", Toast.LENGTH_LONG).show();
                                SharedPreferences sp = getSharedPreferences("Profile", 0);
                                refreshInfo(sp.getString("phone", "0"), sp.getString("pwd", "0"));
                            }  else if (success == 2) {
                                Toast.makeText(context, "Error.", Toast.LENGTH_LONG).show();

                            } else if (success == 15){
                                Toast.makeText(context, "Your account has not been activated. Please try again later.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Server Error.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(context, "Server Error.", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("Convert", "yes");
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , Uri.parse("file://" + compressed));
                } catch (Exception e) {
                    Log.e("Error 4", "Here");
                    e.printStackTrace();
                }
                String image = getStringImage(bitmap);
                Map<String,String> params = new Hashtable<>();
                params.put("image", image);
                params.put("groupId", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public void refreshInfo(final String phone, final String pwd) {
        pd = new ProgressDialog(context);
        pd.setMessage("Please wait...");
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
}
