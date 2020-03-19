package app.gify.co.id.modal;

public class MadolCart {
    private int harga,jumlah, kode;
    private String namacart, gambar;

    public MadolCart(String gambar, int harga, String namacart, int kode, int jumlah) {
        this.gambar = gambar;
        this.harga = harga;
        this.namacart = namacart;
        this.kode = kode;
        this.jumlah = jumlah;
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
}
