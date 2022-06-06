package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 24/01/2019.
 */

public class CRM_SO_HisDtlLVList {

    String docno, brgname, satuan, harga, diskon, netto, tglkrm;
    int qty;

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getBrgname() {
        return brgname;
    }

    public void setBrgname(String brgname) {
        this.brgname = brgname;
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

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getNetto() {
        return netto;
    }

    public void setNetto(String netto) {
        this.netto = netto;
    }

    public String getTglkrm() {
        return tglkrm;
    }

    public void setTglkrm(String tglkrm) {
        this.tglkrm = tglkrm;
    }

}
