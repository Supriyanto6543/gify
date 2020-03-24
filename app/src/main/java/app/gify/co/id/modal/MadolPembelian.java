package app.gify.co.id.modal;

import android.content.Context;

public class MadolPembelian {


    private int idpesanan, status;
    private String id_tetap, ttl, penerima, alamat, kelurahan, kecamatan, kota, provinsi, resi, nama_barang, ucapan;

    public MadolPembelian(int idpesanan, int status, String id_tetap, String ttl, String penerima, String alamat, String kelurahan, String kecamatan, String kota, String provinsi, String resi, String nama_barang, String ucapan) {
        this.idpesanan = idpesanan;
        this.status = status;
        this.id_tetap = id_tetap;
        this.ttl = ttl;
        this.penerima = penerima;
        this.alamat = alamat;
        this.kelurahan = kelurahan;
        this.kecamatan = kecamatan;
        this.kota = kota;
        this.provinsi = provinsi;
        this.resi = resi;
        this.nama_barang = nama_barang;
        this.ucapan = ucapan;
    }

    public int getIdpesanan() {
        return idpesanan;
    }

    public void setIdpesanan(int idpesanan) {
        this.idpesanan = idpesanan;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId_tetap() {
        return id_tetap;
    }

    public void setId_tetap(String id_tetap) {
        this.id_tetap = id_tetap;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getResi() {
        return resi;
    }

    public void setResi(String resi) {
        this.resi = resi;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getUcapan() {
        return ucapan;
    }

    public void setUcapan(String ucapan) {
        this.ucapan = ucapan;
    }
}
