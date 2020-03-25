package app.gify.co.id.rajaongkir.modelongkir.provinsi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class SourceOngkir {
    @SerializedName("query")
    @Expose
    private List<Object> query = null;
    @SerializedName("status")
    @Expose
    private StatusOngkri status;
    @SerializedName("results")
    @Expose
    private List<ResultOngkir> results = null;

    private List<Object> getQuery(){
        return query;
    }

    public void setQuery(List<Object> query) {
        this.query = query;
    }

    public StatusOngkri getStatus() {
        return status;
    }

    public void setStatus(StatusOngkri status) {
        this.status = status;
    }

    public List<ResultOngkir> getResults() {
        return results;
    }

    public void setResults(List<ResultOngkir> results) {
        this.results = results;
    }
}
