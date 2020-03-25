package app.gify.co.id.rajaongkir.modelongkir.kurir;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class ItemExpedisi {

    protected String id;
    protected String name;

    public ItemExpedisi() {

    }

    public ItemExpedisi(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
