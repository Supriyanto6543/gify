package app.gify.co.id.notification;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by SUPRIYANTO on 3/26/2020.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private static Context context;
    private RequestQueue requestQueue;

    private MySingleton(Context ctx){
        context = ctx;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getmInstance(Context ctx){
        if (mInstance == null){
            mInstance = new MySingleton(ctx);
        }
        return mInstance;
    }

    public<T> void addToRequest(Request<T> request){
        getRequestQueue().add(request);
    }

}
