package app.gify.co.id.rajaongkir.modelongkir.provinsi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class Province {

    @SerializedName("rajaongkir")
    @Expose
    private SourceOngkir sourceOngkir;

    public Province(SourceOngkir sourceOngkir) {
        this.sourceOngkir = sourceOngkir;
    }

    public SourceOngkir getSourceOngkir(){
        return sourceOngkir;
    }

    public void setRajaOngkir(SourceOngkir sourceOngkir){
        this.sourceOngkir = sourceOngkir;
    }
}
