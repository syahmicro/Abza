package com.abzaa.abzaa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zarul on 14/04/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "abzadb";
    public static final String TABLE_NAME = "groupUser";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS groupUser (" +
                "id TEXT PRIMARY KEY," +
                "user_id TEXT," +
                "group_id TEXT," +
                "juz_read TEXT," +
                "date_joined TEXT," +
                "group_name TEXT," +
                "group_picture TEXT," +
                "status TEXT," +
                "gstatus TEXT," +
                "created_by TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS news");
        onCreate(db);
    }


//    public boolean insertGroup  (String id, String user_id, String group_id, String juz_read, String date_joined, String group_name, String group_picture, String status, String gstatus, String created_by)
public boolean insertGroup  (String id, String user_id, String group_id, String juz_read, String group_name, String group_picture, String status, String gstatus, String created_by)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("user_id", user_id);
        contentValues.put("group_id", group_id);
        contentValues.put("juz_read", juz_read);
        //contentValues.put("date_joined", date_joined);
        contentValues.put("group_name", group_name);
        contentValues.put("group_picture", group_picture);
        contentValues.put("status", status);
        contentValues.put("gstatus", gstatus);
        contentValues.put("created_by", created_by);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public void clearGroupUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public int groupUserRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public ArrayList<ObjGroupUser> getGroupUser() {
        ArrayList<ObjGroupUser> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("" +
                "SELECT * " +
                "FROM groupUser " +
                "ORDER BY id ", null );
        res.moveToFirst();
        ObjGroupUser cr;

        while(!res.isAfterLast()){
            cr = new ObjGroupUser(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6),
                    res.getString(7),
                    res.getString(8),
                    res.getString(9)
            );

            array_list.add(cr);

            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<ObjGroupUser> getSingleGroupUser(String id)
    {
        ArrayList<ObjGroupUser> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("" +
                "SELECT * " +
                "FROM groupUser " +
                "WHERE group_id = '" + id + "' ", null );
        res.moveToFirst();
        ObjGroupUser cr;

        while(!res.isAfterLast()){
            cr = new ObjGroupUser(
                    res.getString(0),
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6),
                    res.getString(7),
                    res.getString(8),
                    res.getString(9)
            );

            array_list.add(cr);

            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public void closeConnection() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.close();

    }
}