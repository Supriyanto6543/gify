package app.gify.co.id.rajaongkir.modelongkir.biaya;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class ItemCost {
    @SerializedName("rajaongkir")
    @Expose
    private Rajaongkir rajaongkir;

    public Rajaongkir getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(Rajaongkir rajaongkir) {
        this.rajaongkir = rajaongkir;
    }
}
