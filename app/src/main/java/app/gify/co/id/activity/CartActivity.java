package app.gify.co.id.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterCart;
import app.gify.co.id.modal.MadolCart;

import static app.gify.co.id.baseurl.UrlJson.GETBARANG;

public class CartActivity extends AppCompatActivity {

    Button Checkout;
    ImageView backCart;
    TextView totalbelanjar, totalberat;
    AdapterCart adapterCart;
    ArrayList<MadolCart> madolCarts;
    String namacart, gambarcart;
    int idbarang, kuantitas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        backCart = findViewById(R.id.backCartNav);
        backCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getApplicationContext()).openDrawer();
            }
        });

        Checkout = findViewById(R.id.checkoutChart);
        totalbelanjar = findViewById(R.id.totalBelanjaChart);
        totalberat = findViewById(R.id.totalBeratChart);



        madolCarts = new ArrayList<>();
        namacart = getIntent().getStringExtra("nama");
        gambarcart = getIntent().getStringExtra("gambar");
        idbarang = getIntent().getIntExtra("idbarang", -1);
        kuantitas = getIntent().getIntExtra("quantity", -1);

        getBerat();

        Checkout.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });
    }

    private void getBerat(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETBARANG, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    int id_barang = object.getInt("id");
                    if (idbarang==id_barang){
                        String berat = object.getString("berat");
                        Log.d("beratget", "getBerat: " + berat);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("jsonex", "getBerat: " + e.getMessage());
            }
        }, error -> {
            Log.d("jsoner", "getBerat: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(CartActivity.this);
        queue.add(objectRequest);
    }
}
