package app.gify.co.id.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.gify.co.id.R;

import static app.gify.co.id.baseurl.UrlJson.GETCART;
import static app.gify.co.id.baseurl.UrlJson.SENDCART;

public class DetailKado extends AppCompatActivity {

    CarouselView slide;
    TextView nama, harga, desc, namapopup, jumlah, hargapopuptop, hargapopupdown;
    Button belikadodetail;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button proses, batal;
    ImageView tambah, kurang;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int hargas;
    int cingpai = 1;
    CarouselView carouselView;
    String uid;
    int id_barang;
    String idbarangku;
    int sourceImg[] = {R.drawable.lupa_password_background, R.drawable.profile_image};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_barang_nocard);

        belikadodetail = findViewById(R.id.belikadodetail);
        slide = findViewById(R.id.carousel);
        nama = findViewById(R.id.namadetail);
        harga = findViewById(R.id.hargadetail);
        desc = findViewById(R.id.descdetail);

        //carousel
        carouselView = (CarouselView) findViewById(R.id.carousel);
        carouselView.setPageCount(sourceImg.length);
        carouselView.setImageListener(slideImage);

        idbarangku = getIntent().getStringExtra("idbarang");
        preferences = PreferenceManager.getDefaultSharedPreferences(DetailKado.this);
        uid = preferences.getString("uid", "");
        hargas = getIntent().getIntExtra("harga", -1);
        nama.setText(getIntent().getStringExtra("nama"));
        harga.setText("Rp. " + hargas);
        desc.setText(getIntent().getStringExtra("desc"));

        belikadodetail.setOnClickListener(view -> {
            popup();
        });
    }

    ImageListener slideImage = (position, imageView) -> imageView.setImageResource(sourceImg[position]);

    private void popup() {

        builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_beli_barang, null);
        namapopup = view.findViewById(R.id.nama);
        jumlah = view.findViewById(R.id.jumlah);
        hargapopuptop = view.findViewById(R.id.hargapopuptop);
        hargapopupdown = view.findViewById(R.id.hargabrng);
        proses = view.findViewById(R.id.buttonproses);
        batal = view.findViewById(R.id.buttonbatal);
        tambah = view.findViewById(R.id.tambahkuantitas);
        kurang = view.findViewById(R.id.kurangkuantitas);


        namapopup.setText(getIntent().getStringExtra("nama"));
        hargapopuptop.setText("Rp. " + hargas);
        hargapopupdown.setText("Rp. " + hargas);

        tambah.setOnClickListener(view1 -> {
            if (cingpai==9){

            }else {
                cingpai = cingpai + 1;
            }
            jumlah.setText(String.valueOf(cingpai));
            hargapopupdown.setText("Rp. " + hargas*cingpai);
        });
        kurang.setOnClickListener(view1 -> {
            if (cingpai==1){

            }else {
                cingpai = cingpai - 1;
            }

            jumlah.setText(String.valueOf(cingpai));
            hargapopupdown.setText("Rp. " + hargas*cingpai);;
        });

        batal.setOnClickListener(view1 -> dialog.dismiss());
        proses.setOnClickListener(view1 -> {
            getCart();

        });



        dialog = builder.create();
        dialog.setView(view);
        dialog.show();

    }

    private void sendtocart(final String idbarang){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SENDCART, response -> {
            try {
                if (response.equalsIgnoreCase("bisa")){
                    Intent intent = new Intent(DetailKado.this, CartActivity.class);
                    startActivity(intent);
                    Log.d("sendtocartif", "getCart: ");
                }
            }catch (Exception e){
                Log.d("elil", "sendtocart: " + e.getMessage());
            }
        }, error -> {
            Log.d("errorsendtocart", "sendtocart: " + error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_tetap", uid);
                params.put("id_barang", idbarang);
                params.put("jumlah", String.valueOf(cingpai));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(DetailKado.this);
        queue.add(stringRequest);
    }

    private void getCart(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETCART, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                Log.d("trycart", "getCart: " + response + array.length());
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String id_tetapku = object.getString("id_tetap");
                    Log.d("testgetcart", "getCart: " + uid + " s " + id_tetapku);
                    if (uid.equalsIgnoreCase(id_tetapku)){
                        id_barang = object.getInt("id_barang");
                        Log.d("testgetcartif", "getCart: ");
                    }
                }
                if (id_barang == Integer.parseInt(idbarangku)){
                    Toast.makeText(this, "Barang sudah ada di keranjang", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("testgetcartelse", "getCart: ");
                    sendtocart(String.valueOf(idbarangku));
                }

            } catch (JSONException e) {
                Log.d("exceptioncart", "getCart: " + e.getMessage());
                e.printStackTrace();
            }
        }, error -> {
            Log.d("errordetail", "getCart: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(DetailKado.this);
        queue.add(objectRequest);
    }
}
