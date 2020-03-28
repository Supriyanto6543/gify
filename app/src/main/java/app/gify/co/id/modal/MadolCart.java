package app.gify.co.id.modal;

public class MadolCart {
    private int harga,jumlah, kode;
    private String namacart, gambar;
    private int berat, quantity;

    public MadolCart(String gambar, int harga, String namacart, int kode, int jumlah, int berat, int quantity) {
        this.gambar = gambar;
        this.harga = harga;
        this.namacart = namacart;
        this.kode = kode;
        this.jumlah = jumlah;
        this.berat = berat;
        this.quantity = quantity;
    }

    public int getBerat() {
        return berat;
    }

    public void setBerat(int berat) {
        this.berat = berat;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getNamacart() {
        return namacart;
    }

    public void setNamacart(String nama) {
        this.namacart = nama;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
}
