package app.gify.co.id.rajaongkir.modelongkir.kota;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class Query {
    @SerializedName("province")
    @Expose
    private String province;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
