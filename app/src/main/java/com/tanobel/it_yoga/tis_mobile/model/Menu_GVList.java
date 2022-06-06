package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 05/10/2018.
 */

public class Menu_GVList {
    private String code, nama, gambar, bg;
    private int notifcount;

    public Menu_GVList() {
        // TODO Auto-generated constructor stub
    }

    public Menu_GVList(String code, String nama, String gambar, String bg, int notifcount) {
        super();
        this.code=code;
        this.nama=nama;
        this.gambar=gambar;
        this.bg=bg;
        this.notifcount=notifcount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getBG() {
        return bg;
    }

    public void setBG(String bg) {
        this.bg = bg;
    }

    public int getNotifcount() {
        return notifcount;
    }

    public void setNotifcount(int notifcount) {
        this.notifcount = notifcount;
    }

}


