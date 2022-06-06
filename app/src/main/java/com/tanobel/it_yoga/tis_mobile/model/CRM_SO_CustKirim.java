package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 16/11/2018.
 */

public class CRM_SO_CustKirim {

    String kode, nama, alamat;

    public CRM_SO_CustKirim() {
        // TODO Auto-generated constructor stub
    }

    public CRM_SO_CustKirim(String kode, String nama, String alamat) {
        super();
        this.kode = kode;
        this.nama = nama;
        this.alamat = alamat;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

}