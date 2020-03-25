package app.gify.co.id.rajaongkir.apiongkir;

import app.gify.co.id.rajaongkir.modelongkir.biaya.ItemCost;
import app.gify.co.id.rajaongkir.modelongkir.kota.ItemCity;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.Province;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public interface ApiRaja {

    //province
    @Headers("key:cfc2d1eac754c1b41f383bbfa6fe45b6")
    @GET("province")
    Call<Province> getProvince();

    //city
    @Headers("key:cfc2d1eac754c1b41f383bbfa6fe45b6")
    @GET("city")
    Call<ItemCity> getCity(
            @Query("province") String province
    );

    //cost
    @FormUrlEncoded
    @POST("cost")
    Call<ItemCost> getCost(
            @Field("key") String token,
            @Field("origin") String origin,
            @Field("destination") String destination,
            @Field("weight") String weight,
            @Field("courier") String courier
    );
}
