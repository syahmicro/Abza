package com.abzaa.abzaa;

/**
 * Created by zarul on 14/04/16.
 */
public class ObjGrid {

    String id;
    String title;
    int picture;

    public ObjGrid(String id, String title, int picture) {
        this.id = id;
        this.title = title;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getPicture() {
        return picture;
    }
}
