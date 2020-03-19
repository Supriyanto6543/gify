package app.gify.co.id.modal;

public class MadolCart {
    private int harga;
    private String namacart, kode, jumlah, gambar;

    public MadolCart(String gambar, int harga, String namacart, String kode, String jumlah) {
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

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
