package com.tanobel.it_yoga.tis_mobile.model;

public class ApproveSMP_LVList {

    String code, nama, tipe, chanelcode;

    public ApproveSMP_LVList() {
        // TODO Auto-generated constructor stub
    }

    public ApproveSMP_LVList(String code, String nama, String tipe, String chanelcode) {
        super();
        this.code = code;
        this.nama = nama;
        this.tipe = tipe;
        this.chanelcode = chanelcode;
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

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getChanelcode() {
        return chanelcode;
    }

    public void setChanelcode(String chanelcode) {
        this.chanelcode = chanelcode;
    }
}