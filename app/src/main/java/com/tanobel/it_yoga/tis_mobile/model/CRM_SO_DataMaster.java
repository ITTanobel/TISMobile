package com.tanobel.it_yoga.tis_mobile.model;

public class CRM_SO_DataMaster {

    String prefix, sufix, tgl, status, kodestatus;

    public CRM_SO_DataMaster() {
        // TODO Auto-generated constructor stub
    }

    public CRM_SO_DataMaster(String prefix, String sufix, String tgl, String status, String kodestatus) {
        super();
        this.prefix = prefix;
        this.sufix = sufix;
        this.tgl = tgl;
        this.status = status;
        this.kodestatus = kodestatus;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSufix() {
        return sufix;
    }

    public void setSufix(String sufix) {
        this.sufix = sufix;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKodestatus() {
        return kodestatus;
    }

    public void setKodestatus(String kodestatus) {
        this.kodestatus = kodestatus;
    }
}