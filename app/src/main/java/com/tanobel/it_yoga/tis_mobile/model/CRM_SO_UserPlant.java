package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 16/11/2018.
 */

public class CRM_SO_UserPlant {

    String CustCode, Branch, Desc;

    public CRM_SO_UserPlant() {
        // TODO Auto-generated constructor stub
    }

    public CRM_SO_UserPlant(String CustCode, String Branch, String Desc) {
        super();
        this.CustCode = CustCode;
        this.Branch = Branch;
        this.Desc = Desc;
    }

    public String getCustCode() {
        return CustCode;
    }

    public void setCustCode(String CustCode) {
        this.CustCode = CustCode;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String Branch) {
        this.Branch = Branch;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String Desc) {
        this.Desc = Desc;
    }

}