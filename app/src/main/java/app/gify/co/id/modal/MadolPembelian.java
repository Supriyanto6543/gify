package app.gify.co.id.modal;

public class MadolPembelian {
    private int tanggal, idpesanan, resi, status;
    private String jenis, penerima;

    public MadolPembelian(int tanggal, int idpesanan, int resi, int status, String jenis, String penerima) {
        this.tanggal = tanggal;
        this.idpesanan = idpesanan;
        this.resi = resi;
        this.status = status;
        this.jenis = jenis;
        this.penerima = penerima;
    }

    public int getTanggal() {
        return tanggal;
    }

    public void setTanggal(int tanggal) {
        this.tanggal = tanggal;
    }

    public int getIdpesanan() {
        return idpesanan;
    }

    public void setIdpesanan(int idpesanan) {
        this.idpesanan = idpesanan;
    }

    public int getResi() {
        return resi;
    }

    public void setResi(int resi) {
        this.resi = resi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }
}
