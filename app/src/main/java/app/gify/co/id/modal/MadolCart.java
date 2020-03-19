package app.gify.co.id.modal;

public class MadolCart {
    private int harga;
    private String nama, kode, jumlah, gambar;

    public MadolCart(String gambar, int harga, String nama, String kode, String jumlah) {
        this.gambar = gambar;
        this.harga = harga;
        this.nama = nama;
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
