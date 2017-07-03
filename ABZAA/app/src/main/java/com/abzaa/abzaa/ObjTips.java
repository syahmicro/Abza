package com.abzaa.abzaa;

/**
 * Created by zarulizham on 06/08/2016.
 */
public class ObjTips {

    String id;
    String juz_no;
    String title;
    String info;
    String status;

    public ObjTips(String id, String juz_no, String title, String info, String status) {
        this.id = id;
        this.juz_no = juz_no;
        this.title = title;
        this.info = info;
        this.status = status;
    }

    public String getId() {
        return id;
    }
    public String getJuz_no() {
        return juz_no;
    }
    public String getTitle() {
        return title;
    }
    public String getInfo() {
        return info;
    }
    public String getStatus() {
        return status;
    }
}
