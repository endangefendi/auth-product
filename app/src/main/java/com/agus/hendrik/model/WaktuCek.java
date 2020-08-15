package com.agus.hendrik.model;


public class WaktuCek {
    private String uid, code_barang, date;
    private int idcek;

    public WaktuCek() {
    }

    public WaktuCek(int idcek, String uid, String code_barang, String date){
        this.idcek = idcek;
        this.uid = uid;
        this.code_barang = code_barang;
        this.date = date;
    }

    public int getIdcek() {
        return idcek;
    }

    public void setIdcek(int idcek) {
        this.idcek = idcek;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode_barang() {
        return code_barang;
    }

    public void setCode_barang(String code_barang) {
        this.code_barang = code_barang;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
