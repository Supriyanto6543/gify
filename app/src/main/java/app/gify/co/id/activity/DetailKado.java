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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

import app.gify.co.id.Fragment.favorit.FavoritFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterFavorit;

import static app.gify.co.id.baseurl.UrlJson.CHECKCART;
import static app.gify.co.id.baseurl.UrlJson.DELETEFAV;
import static app.gify.co.id.baseurl.UrlJson.GETCART;
import static app.gify.co.id.baseurl.UrlJson.GETFAV;
import static app.gify.co.id.baseurl.UrlJson.SENDCART;
import static app.gify.co.id.baseurl.UrlJson.SENDFAV;

public class DetailKado extends AppCompatActivity {

    CarouselView slide;
    TextView nama, harga, desc, namapopup, jumlah, hargapopuptop, hargapopupdown;
    Button belikadodetail;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button proses, batal;
    ImageView tambah, kurang, favorit, unfavorit;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int hargas;
    int cingpai = 1;
    CarouselView carouselView;
    String uid;
    int id_barang;
    String idbarang;
    String idbarangku;
    int sourceImg[] = {R.drawable.lupa_password_background, R.drawable.profile_image};
    Boolean faforit;
    ImageView buatJadiWistlist, back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_barang_nocard);

        back = findViewById(R.id.backDetailKado);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        belikadodetail = findViewById(R.id.belikadodetail);
        slide = findViewById(R.id.carousel);
        nama = findViewById(R.id.namadetail);
        harga = findViewById(R.id.hargadetail);
        desc = findViewById(R.id.descdetail);
        favorit = findViewById(R.id.favoritdeta);
        unfavorit = findViewById(R.id.unfavoritdet);

        //carousel
        carouselView = (CarouselView) findViewById(R.id.carousel);
        carouselView.setPageCount(sourceImg.length);
        carouselView.setImageListener(slideImage);

        idbarangku = getIntent().getStringExtra("idbarang");
        preferences = PreferenceManager.getDefaultSharedPreferences(DetailKado.this);
        faforit = getIntent().getBooleanExtra("favorit", false);
        if (faforit){
            favorit.setVisibility(View.VISIBLE);
        }else {
            favorit.setVisibility(View.GONE);
            unfavorit.setVisibility(View.VISIBLE);
        }

        getFavo();

        uid = preferences.getString("uid", "");

        unfavorit.setOnClickListener(view -> getFav());
        favorit.setOnClickListener(view -> delfav());

        hargas = getIntent().getIntExtra("harga", -1);
        nama.setText(getIntent().getStringExtra("nama"));
        harga.setText("Rp. " + hargas);
        desc.setText(getIntent().getStringExtra("desc"));

        belikadodetail.setOnClickListener(view -> {
            popup();
        });
    }

    private void delfav() {
        StringRequest request = new StringRequest(Request.Method.GET, DELETEFAV+"?idtetap="+uid+"&idbarang="+idbarangku, response -> {
            if (response.equalsIgnoreCase("bisa")){
                Toast.makeText(this, "Barang telah di hapus dari favorite", Toast.LENGTH_SHORT).show();
                faforit = false;
                favorit.setVisibility(View.GONE);
                unfavorit.setVisibility(View.VISIBLE);
            }
        }, error -> {

        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
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
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, CHECKCART+"?idtetap="+uid+"&idbarang="+idbarangku, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                Log.d("trycart", "getCart: " + response + array.length());
                Log.d("nullsebelum", "getCart: " + array.toString());
                if (array.isNull(0)){
                    sendtocart(idbarangku);
                }else {
                    Toast.makeText(this, "Barang sudah ada di cart", Toast.LENGTH_SHORT).show();
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

    private void sendFavorit(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SENDFAV, response -> {
            try {
                if (response.equalsIgnoreCase("bisa")){
                    Log.d("masuksendf", "getFav: " + response );
                    Toast.makeText(this, "Sudah di tambahkan di List Favorit", Toast.LENGTH_SHORT).show();
                    favorit.setVisibility(View.VISIBLE);
                    unfavorit.setVisibility(View.GONE);
                    getIntent().removeExtra("favorit");
                    Intent intent = new Intent(DetailKado.this, List_Kado.class);
                    intent.putExtra("favorit", true);
                    startActivity(intent);
                    finish();
                }
            }catch (Exception e) {
                Log.d("sendexceptione", "sendFavorit: " + e.getMessage());
            }
        }, error -> {
            Log.d("errorsend", "sendFavorit: " + error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_tetap", uid);
                params.put("id_barang", idbarangku);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(DetailKado.this);
        queue.add(stringRequest);
    }

    private void getFav(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETFAV, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String id_tetap = object.getString("id_tetap");
                    Log.d("masukgetfav", "getFav: " + id_tetap + " s " + uid);
                    if (id_tetap.contains(uid)){
                        String id_barang = object.getString("id_barang");
                        Log.d("uidgetfav", "getFav: " + id_barang + " s " + idbarangku);
                        if (id_barang.equalsIgnoreCase(idbarangku)){
                            Toast.makeText(this, "barang sudah ada di favorit", Toast.LENGTH_SHORT).show();
                            Log.d("idbarangequalfav", "getFav: " + id_barang + " s " + idbarangku);
                        }
                    }else {
                        sendFavorit();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("errordetailkado", "getFav: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(DetailKado.this);
        queue.add(objectRequest);
    }

    private void getFavo(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETFAV, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String id_tetap = object.getString("id_tetap");
                    Log.d("masukgetfav", "getFav: " + id_tetap + " s " + uid);
                    if (id_tetap.contains(uid)){
                        String id_barang = object.getString("id_barang");
                        Log.d("uidgetfav", "getFav: " + id_barang + " s " + idbarangku);
                        if (id_barang.equalsIgnoreCase(idbarangku)){
                            favorit.setVisibility(View.VISIBLE);
                            unfavorit.setVisibility(View.GONE);
                            Log.d("idbarangequalfav", "getFav: " + id_barang + " s " + idbarangku);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("errordetailkado", "getFav: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(DetailKado.this);
        queue.add(objectRequest);
    }


}
