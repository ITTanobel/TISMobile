package com.tanobel.it_yoga.tis_mobile.model;

//import java.math.double;

/**
 * Created by IT_Yoga on 25/09/2018.
 */

public class ApproveSMP_RVList {
    private String kdbrg, namabrg, satuan, tglmulai, tglakhir, kditem, kditem2, kditem4, kditem5, kditem6, kditem7, kditem8, kditem9;
    private double harga, qty1, qty2, disc1pcn, disc2pcn, disc1val, disc2val, kditem3;

    public ApproveSMP_RVList() {
        // TODO Auto-generated constructor stub
    }

    public ApproveSMP_RVList(String kdbrg, String namabrg, String satuan, String tglmulai, String tglakhir, double harga, double qty1, double qty2, double disc1pcn,
                             double disc2pcn, double disc1val, double disc2val, String kditem, String kditem2, double kditem3, String kditem4, String kditem5,
                             String kditem6, String kditem7, String kditem8, String kditem9) {
        super();
        this.kdbrg=kdbrg;
        this.namabrg=namabrg;
        this.satuan=satuan;
        this.tglmulai=tglmulai;
        this.tglakhir=tglakhir;
        this.harga=harga;
        this.qty1=qty1;
        this.qty2=qty2;
        this.disc1pcn=disc1pcn;
        this.disc2pcn=disc2pcn;
        this.disc1val=disc1val;
        this.disc2val=disc2val;
        this.kditem=kditem;
        this.kditem2=kditem2;
        this.kditem3=kditem3;
        this.kditem4=kditem4;
        this.kditem5=kditem5;
        this.kditem6=kditem6;
        this.kditem7=kditem7;
        this.kditem8=kditem8;
        this.kditem9=kditem9;
    }

    public String getKdbrg() {
        return kdbrg;
    }

    public void setKdbrg(String kdbrg) {
        this.kdbrg = kdbrg;
    }

    public String getNamabrg() {
        return namabrg;
    }

    public void setNamabrg(String namabrg) {
        this.namabrg = namabrg;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getTglmulai() {
        return tglmulai;
    }

    public void setTglmulai(String tglmulai) {
        this.tglmulai = tglmulai;
    }

    public String getTglakhir() {
        return tglakhir;
    }

    public void setTglakhir(String tglakhir) {
        this.tglakhir = tglakhir;
    }

    public double getQty1() {
        return qty1;
    }

    public void setQty1(double qty1) {
        this.qty1 = qty1;
    }

    public double getQty2() {
        return qty2;
    }

    public void setQty2(double qty2) {
        this.qty2 = qty2;
    }

    public double getDisc1pcn() {
        return disc1pcn;
    }

    public void setDisc1pcn(double disc1pcn) {
        this.disc1pcn = disc1pcn;
    }

    public double getDisc2pcn() {
        return disc2pcn;
    }

    public void setDisc2pcn(double disc2pcn) {
        this.disc2pcn = disc2pcn;
    }

    public double getDisc1val() {
        return disc1val;
    }

    public void setDisc1val (double disc1val) {
        this.disc1val = disc1val;
    }

    public double getDisc2val() {
        return disc2val;
    }

    public void setDisc2val(double disc2val) {
        this.disc2val = disc2val;
    }

    public String getKditem() {
        return kditem;
    }

    public void setKditem(String kditem) {
        this.kditem = kditem;
    }

    public String getKditem2() {
        return kditem2;
    }

    public void setKditem2(String kditem2) {
        this.kditem2 = kditem2;
    }

    public double getKditem3() {
        return kditem3;
    }

    public void setKditem3(double kditem3) {
        this.kditem3 = kditem3;
    }

    public String getKditem4() {
        return kditem4;
    }

    public void setKditem4(String kditem4) {
        this.kditem4 = kditem4;
    }

    public String getKditem5() {
        return kditem5;
    }

    public void setKditem5(String kditem5) {
        this.kditem5 = kditem5;
    }

    public String getKditem6() {
        return kditem6;
    }

    public void setKditem6(String kditem6) {
        this.kditem6 = kditem6;
    }

    public String getKditem7() {
        return kditem7;
    }

    public void setKditem7(String kditem7) {
        this.kditem7 = kditem7;
    }

    public String getKditem8() {
        return kditem8;
    }

    public void setKditem8(String kditem8) {
        this.kditem8 = kditem8;
    }

    public String getKditem9() {
        return kditem9;
    }

    public void setKditem9(String kditem9) {
        this.kditem9 = kditem9;
    }
}
