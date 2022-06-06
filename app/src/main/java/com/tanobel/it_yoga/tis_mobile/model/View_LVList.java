package com.tanobel.it_yoga.tis_mobile.model;

public class View_LVList {

    String plant, docno, code, nama, tipe;

    public View_LVList() {
        // TODO Auto-generated constructor stub
    }

    public View_LVList(String plant, String docno, String code, String nama, String tipe) {
        super();
        this.plant = plant;
        this.docno = docno;
        this.code = code;
        this.nama = nama;
        this.tipe = tipe;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
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
}