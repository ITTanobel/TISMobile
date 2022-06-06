package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 16/11/2018.
 */

public class CRM_SO_Barang {

    String kode, nama, satcode;

    public CRM_SO_Barang() {
        // TODO Auto-generated constructor stub
    }

    public CRM_SO_Barang(String kode, String nama, String satcode) {
        super();
        this.kode = kode;
        this.nama = nama;
        this.satcode = satcode;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSatcode() {
        return satcode;
    }

    public void setSatcode(String satcode) {
        this.satcode = satcode;
    }

}