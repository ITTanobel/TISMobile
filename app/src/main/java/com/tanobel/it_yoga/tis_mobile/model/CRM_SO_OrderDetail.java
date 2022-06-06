package com.tanobel.it_yoga.tis_mobile.model;

//import java.math.double;

/**
 * Created by IT_Yoga on 25/09/2018.
 */

public class CRM_SO_OrderDetail {
    private String kode, nama, satuan, tglkirim;
    private int id, qty;

    public CRM_SO_OrderDetail() {
        // TODO Auto-generated constructor stub
    }

    public CRM_SO_OrderDetail(int id, String kode, String nama, int qty, String satuan, String tglkirim) {
        super();
        this.id = id;
        this.kode=kode;
        this.nama=nama;
        this.qty=qty;
        this.satuan=satuan;
        this.tglkirim=tglkirim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getTglkirim() {
        return tglkirim;
    }

    public void setTglkirim(String tglkirim) {
        this.tglkirim = tglkirim;
    }
}
