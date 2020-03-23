package app.gify.co.id.modal;

public class MadolKado {
    private int harga;
    private String nama, kode, gambar, gambar1, gambar2, desc, id_barang;

    public MadolKado(String gambar, int harga, String nama, String kode, String desc, String id_barang) {
        this.gambar = gambar;
        this.harga = harga;
        this.nama = nama;
        this.kode = kode;
        this.desc = desc;
        this.id_barang = id_barang;
    }

    public MadolKado(String gambar, String gambar1, String gambar2, int harga, String nama, String kode, String desc, String id_barang) {
        this.harga = harga;
        this.nama = nama;
        this.kode = kode;
        this.gambar = gambar;
        this.gambar1 = gambar1;
        this.gambar2 = gambar2;
        this.desc = desc;
        this.id_barang = id_barang;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getGambar1() {
        return gambar;
    }

    public void setGambar1(String gambar1) {
        this.gambar = gambar1;
    }

    public String getGambar2() {
        return gambar;
    }

    public void setGambar2(String gambar2) {
        this.gambar = gambar2;
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
}
