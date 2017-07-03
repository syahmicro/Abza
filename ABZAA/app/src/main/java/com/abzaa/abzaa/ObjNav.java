package com.abzaa.abzaa;

/**
 * Created by Zarul Izham on 9/3/2016.
 */
public class ObjNav {

    String id;
    String group;
    String juz;
    String completed;
    String picture;

    public ObjNav(String id, String group, String juz, String completed, String picture) {
        this.id = id;
        this.group = group;
        this.juz = juz;
        this.completed = completed;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }
    public String getGroup() {
        return group;
    }
    public String getJuz() {
        return juz;
    }
    public String getCompleted() {
        return completed;
    }
    public String getPicture() {
        return picture;
    }
}
