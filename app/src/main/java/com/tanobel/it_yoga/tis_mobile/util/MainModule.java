package com.tanobel.it_yoga.tis_mobile.util;

import android.app.Application;

public class MainModule extends Application {

    private String Purpose, UserCode, UserTIS;
    private byte[] UserImage;

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String Purpose) {
        this.Purpose = Purpose;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public byte[] getUserImage() {
        return UserImage;
    }

    public void setUserImage(byte[] UserImage) {
        this.UserImage = UserImage;
    }

    public String getUserTIS() {
        return UserTIS;
    }

    public void setUserTIS(String UserTIS) {
        this.UserTIS = UserTIS;
    }

    public static String getcharfromascii(String s){
        String str = "";
        int ascii = 0;
        String parts[] = s.split(",");
        for(String part: parts) {
            ascii = Integer.parseInt(String.valueOf(part));
            str = str + (char) ascii;
        }

        return str;
    }
}
