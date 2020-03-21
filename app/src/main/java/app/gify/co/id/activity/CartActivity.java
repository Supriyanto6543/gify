package app.gify.co.id.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterCart;
import app.gify.co.id.modal.MadolCart;
import app.gify.co.id.modal.MadolTotal;

import static app.gify.co.id.baseurl.UrlJson.GETBARANG;
import static app.gify.co.id.baseurl.UrlJson.GETCART;

public class CartActivity extends AppCompatActivity {

    Button Checkout, lanjutBelanja;
    ImageView backCart;
    public TextView totalbelanjar, totalberat;
    AdapterCart adapterCart;
    ArrayList<MadolCart> madolCarts;
    String namacart, gambarcart, uidku;
    int kuantitas, qty1,qty2, qty3, qty4, qty5, harga1, harga2, harga3, harga4, harga5;
    GridLayoutManager glm;
    RecyclerView recyclerView;
    MainActivity mainActivity;
    NavigationView navigationView;
    public int hargaku, beratku;
    SharedPreferences preferences;
    public int madolTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        lanjutBelanja = findViewById(R.id.lanjutBelanjaChart);
        lanjutBelanja.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), List_Kado.class);
            startActivity(intent);
        });

        backCart = findViewById(R.id.backCartNav);
        backCart.setOnClickListener(v -> finish());

        Checkout = findViewById(R.id.checkoutChart);
        totalbelanjar = findViewById(R.id.totalBelanjaChart);
        totalberat = findViewById(R.id.totalBeratChart);
        recyclerView = findViewById(R.id.rvChart);

        preferences = PreferenceManager.getDefaultSharedPreferences(CartActivity.this);
        uidku = preferences.getString("uid", "");
        madolCarts = new ArrayList<>();
        getCart();
        glm = new GridLayoutManager(CartActivity.this, 1);
        recyclerView.setLayoutManager(glm);

        Checkout.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });

        Log.d("madoloption", "onCreate: " + madolTotal);
        totalbelanjar.setText(String.valueOf(madolTotal));

//        if (madolCarts.size() == 5){
//            LocalBroadcastManager.getInstance(this).registerReceiver(item5,
//                    new IntentFilter("barang5"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item4,
//                    new IntentFilter("barang4"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item3,
//                    new IntentFilter("barang3"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item2,
//                    new IntentFilter("barang2"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item1,
//                    new IntentFilter("barang1"));
//            int hasil5 = harga5 * qty5;
//            int hasil4 = harga4 * qty4;
//            int hasil3 = harga3 * qty3;
//            int hasil2 = harga2 * qty2;
//            int hasil1 = harga1 * qty1;
//            int total = hasil1 + hasil2 + hasil3 + hasil4 + hasil5;
//            totalbelanjar.setText("Rp " + total);
//        }else if (madolCarts.size() == 4){
//            LocalBroadcastManager.getInstance(this).registerReceiver(item4,
//                    new IntentFilter("barang4"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item3,
//                    new IntentFilter("barang3"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item2,
//                    new IntentFilter("barang2"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item1,
//                    new IntentFilter("barang1"));
//            int hasil4 = harga4 * qty4;
//            int hasil3 = harga3 * qty3;
//            int hasil2 = harga2 * qty2;
//            int hasil1 = harga1 * qty1;
//            int total = hasil1 + hasil2 + hasil3 + hasil4;
//            totalbelanjar.setText("Rp " + total);
//        }else if (madolCarts.size() == 3){
//            LocalBroadcastManager.getInstance(this).registerReceiver(item3,
//                    new IntentFilter("barang3"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item2,
//                    new IntentFilter("barang2"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item1,
//                    new IntentFilter("barang1"));
//            int hasil3 = harga3 * qty3;
//            int hasil2 = harga2 * qty2;
//            int hasil1 = harga1 * qty1;
//            int total = hasil1 + hasil2 + hasil3;
//            totalbelanjar.setText("Rp " + total);
//        }else if (madolCarts.size() == 2){
//            LocalBroadcastManager.getInstance(this).registerReceiver(item2,
//                    new IntentFilter("barang2"));
//            LocalBroadcastManager.getInstance(this).registerReceiver(item1,
//                    new IntentFilter("barang1"));
//            int hasil2 = harga2 * qty2;
//            int hasil1 = harga1 * qty1;
//            int total = hasil1 + hasil2;
//            totalbelanjar.setText("Rp " + total);
//        }else if (madolCarts.size() == 1){
//            LocalBroadcastManager.getInstance(this).registerReceiver(item1,
//                    new IntentFilter("barang1"));
//            int hasil1 = harga1 * qty1;
//            totalbelanjar.setText("Rp " + hasil1);
//        }

    }

    private void getCart(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETCART, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String id_tetap = object.getString("id_tetap");
                    Log.d("uidsa", "getCart: " + uidku + " s "  + id_tetap);
                    if (id_tetap.equalsIgnoreCase(uidku)){
                        kuantitas = object.getInt("jumlah");
                        int idbarang = object.getInt("id_barang");
                        Log.d("tagku", "getCart: " + kuantitas + " s "  + idbarang);
                        getBerat(idbarang);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("getcart", "getCart: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(CartActivity.this);
        queue.add(objectRequest);
    }

    private void getBerat(int idbarang){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETBARANG, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    int id_barang = object.getInt("id");
                    Log.d("idbarangkudal", "getCart: " + idbarang + " s "  + id_barang);
                    if (idbarang==id_barang){
                        String gambar = object.getString("photo");
                        int harga = object.getInt("harga");
                        String namacart = object.getString("nama");
                        int berat = object.getInt("berat");
                        Log.d("beratget", "getBerat: " + berat);
                        Log.d("idbarangkuas", "getCart: " + gambar + " s "  + harga + " s " + namacart + " s " + berat);
                        MadolCart madolCart = new MadolCart(gambar, harga, namacart, idbarang, kuantitas, berat);
                        madolCarts.add(madolCart);
                        adapterCart = new AdapterCart(madolCarts, CartActivity.this, totalberat, totalbelanjar);
                        recyclerView.setAdapter(adapterCart);

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

    public BroadcastReceiver item1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            harga1 = intent.getIntExtra("harga1", -1);
            qty1 = intent.getIntExtra("quantity", -1);
        }
    };
    public BroadcastReceiver item2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            harga2 = intent.getIntExtra("harga2", -1);
            qty2 = intent.getIntExtra("quantity", -1);
        }
    };
    public BroadcastReceiver item3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            harga3 = intent.getIntExtra("harga3", -1);
            qty3 = intent.getIntExtra("quantity", -1);
        }
    };
    public BroadcastReceiver item4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            harga4 = intent.getIntExtra("harga4", -1);
            qty4 = intent.getIntExtra("quantity", -1);
        }
    };
    public BroadcastReceiver item5 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            harga5 = intent.getIntExtra("harga5", -1);
            qty5 = intent.getIntExtra("quantity", -1);
        }
    };
}
