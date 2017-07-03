package com.abzaa.abzaa;

/**
 * Created by zarul on 15/04/16.
 */
public class ObjNameList {

    String index;
    String name;
    String juz;
    String champ;
    String status;
    String pictures;

    public ObjNameList(String index, String name, String juz, String champ, String status, String picture) {
        this.index = index;
        this.name = name;
        this.juz = juz;
        this.champ = champ;
        this.status = status;
        this.pictures = picture;
    }

    public String getIndex() {
        return index;
    }
    public String getName() {
        return name;
    }
    public String getJuz() {
        return juz;
    }
    public String getChamp() {
        return champ;
    }
    public String getStatus() {
        return status;
    }
    public String getPicture() {
        return pictures;
    }
}
