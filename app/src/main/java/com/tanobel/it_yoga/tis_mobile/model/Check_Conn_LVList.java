package com.tanobel.it_yoga.tis_mobile.model;

public class Check_Conn_LVList {
    String branch, desc, ip, status;

    public Check_Conn_LVList() {
        // TODO Auto-generated constructor stub
    }

    public Check_Conn_LVList(String branch, String desc, String ip, String status) {
        super();
        this.branch = branch;
        this.desc = desc;
        this.ip = ip;
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
