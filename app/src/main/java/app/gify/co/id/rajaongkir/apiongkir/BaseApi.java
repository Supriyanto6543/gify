package app.gify.co.id.rajaongkir.apiongkir;

import java.util.concurrent.TimeUnit;

import app.gify.co.id.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class BaseApi {
    public static final String BASE_URL = "https://api.rajaongkir.com/starter/";

    public static ApiRaja callJson(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(25, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG){
            builder.addInterceptor(httpLoggingInterceptor);
        }

        builder.cache(null);
        OkHttpClient ok = builder.build();

        Retrofit retro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ok)
                .build();

        return retro.create(ApiRaja.class);
    }
}
