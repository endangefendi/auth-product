package com.agus.hendrik.model;

public class Barang {
    private String nama, code_barang, foto, merk, satuan, keterangan, ukuran, kategori, status, jenis;
    private int no;
    private double harga;

    public Barang() {
    }

    public Barang(String nama, int no, String foto, String code_barang, String merk,String satuan, String keterangan, String ukuran, String kategori, double harga, String status, String jenis) {
        this.nama = nama;
        this.jenis = jenis;
        this.no = no;
        this.foto = foto;
        this.code_barang = code_barang;
        this.merk = merk;
        this.satuan = satuan;
        this.keterangan = keterangan;
        this.ukuran = ukuran;
        this.kategori = kategori;
        this.status = status;
        this.harga = harga;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getCode_barang() {
        return code_barang;
    }

    public void setCode_barang(String code_barang) {
        this.code_barang = code_barang;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getUkuran() {
        return ukuran;
    }

    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}