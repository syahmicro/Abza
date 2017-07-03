package com.abzaa.abzaa;

/**
 * Created by zarul on 14/04/16.
 */
public class ObjGroupUser {

    String id;
    String user_id;
    String group_id;
    String juz_read;
    String date_joined;
    String group_name;
    String group_picture;
    String status;
    String gstatus;
    String created_by;

    public ObjGroupUser(String id, String user_id, String group_id, String juz_read, String date_joined, String group_name, String group_picture, String status, String gstatus, String created_by) {
        this.id = id;
        this.user_id = user_id;
        this.group_id = group_id;
        this.juz_read = juz_read;
        this.date_joined = date_joined;
        this.group_name = group_name;
        this.group_picture = group_picture;
        this.status = status;
        this.gstatus = gstatus;
        this.created_by = created_by;
    }

    public String getId() {
        return id;
    }
    public String getUser_id() {
        return user_id;
    }
    public String getGroup_id() {
        return group_id;
    }
    public String getJuz_read() {
        return juz_read;
    }
    public String getDate_joined() {
        return date_joined;
    }
    public String getGroup_name() {
        return group_name;
    }
    public String getGroup_picture() {
        return group_picture;
    }
    public String getStatus() {
        return status;
    }
    public String getGstatus() {
        return gstatus;
    }
    public String getCreated_by() {
        return created_by;
    }
}
