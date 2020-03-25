package app.gify.co.id.rajaongkir.modelongkir.biaya;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class Cost_ {
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("etd")
    @Expose
    private String etd;
    @SerializedName("note")
    @Expose
    private String note;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
