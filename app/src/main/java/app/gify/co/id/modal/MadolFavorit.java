package app.gify.co.id.modal;

public class MadolFavorit {
    private int harga;
    private String nama, kode, gambar, desc, id_barang;

    public MadolFavorit(String gambar, int harga, String nama, String kode, String desc, String id_barang) {
        this.gambar = gambar;
        this.harga = harga;
        this.nama = nama;
        this.kode = kode;
        this.desc = desc;
        this.id_barang = id_barang;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
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
}
