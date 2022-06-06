package com.tanobel.it_yoga.tis_mobile.model;

/**
 * Created by IT_Yoga on 16/11/2018.
 */

public class View_UserPlant {

    String UserCode, Branch, Desc;

    public View_UserPlant() {
        // TODO Auto-generated constructor stub
    }

    public View_UserPlant(String UserCode, String Branch, String Desc) {
        super();
        this.UserCode = UserCode;
        this.Branch = Branch;
        this.Desc = Desc;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
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