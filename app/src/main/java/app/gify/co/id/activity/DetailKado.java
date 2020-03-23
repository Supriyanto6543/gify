package app.gify.co.id.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import app.gify.co.id.Fragment.favorit.FavoritFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterFavorit;

import static app.gify.co.id.baseurl.UrlJson.DETAILKADO;
import static app.gify.co.id.baseurl.UrlJson.GETCART;
import static app.gify.co.id.baseurl.UrlJson.GETFAV;
import static app.gify.co.id.baseurl.UrlJson.SENDCART;
import static app.gify.co.id.baseurl.UrlJson.SENDFAV;

public class DetailKado extends AppCompatActivity {

    CarouselView slide;
    TextView nama, harga, desc, namapopup, jumlah, hargapopuptop, hargapopupdown, kodebarang;
    Button belikadodetail;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button proses, batal;
    ImageView tambah, kurang, favorit, unfavorit;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CarouselView carouselView;
    String idbarangku, uid, id, photobyid, kodeBarangbyid, namabyid, deskripsibyid;
    int id_barang, hargabyid, cingpai, gambar, gambar1, gambar2;
    int sourceImg[];
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
        kodebarang = findViewById(R.id.kodebarang);
        harga = findViewById(R.id.hargadetail);
        desc = findViewById(R.id.descdetail);
        favorit = findViewById(R.id.favoritdeta);
        unfavorit = findViewById(R.id.unfavoritdet);

        id = getIntent().getStringExtra("id");
        gambar = getIntent().getIntExtra("gambar", 0);
        gambar1 = getIntent().getIntExtra("gambar1", 0);
        gambar2 = getIntent().getIntExtra("gambar2", 0);

        sourceImg = new int[]{gambar, gambar1, gambar2};

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

        getDetailBarangById(id);

        belikadodetail.setOnClickListener(view -> {
            popup();
        });

        Toast.makeText(getApplicationContext(), "Id mu adalah " + id, Toast.LENGTH_LONG).show();
    }

    ImageListener slideImage = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            //imageView.setImageResource(sourceImg[position]);
            Glide.with(DetailKado.this).load(sourceImg[position]).into(imageView);
            new DownloadImageTask(imageView).execute(String.valueOf(sourceImg[position]));
        }
    };

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
        hargapopuptop.setText("Rp. " + hargabyid);
        hargapopupdown.setText("Rp. " + hargabyid);

        tambah.setOnClickListener(view1 -> {
            if (cingpai==9){

            }else {
                cingpai = cingpai + 1;
            }
            jumlah.setText(String.valueOf(cingpai));
            hargapopupdown.setText("Rp. " + hargabyid*cingpai);
        });
        kurang.setOnClickListener(view1 -> {
            if (cingpai==1){

            }else {
                cingpai = cingpai - 1;
            }

            jumlah.setText(String.valueOf(cingpai));
            hargapopupdown.setText("Rp. " + hargabyid*cingpai);;
        });

        batal.setOnClickListener(view1 -> dialog.dismiss());
        proses.setOnClickListener(view1 -> {
            sendtocart(id);
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
                        if (id_barang == Integer.parseInt(idbarangku)){
                            Toast.makeText(this, "Barang sudah ada di keranjang", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d("testgetcartelse", "getCart: ");
                            sendtocart(String.valueOf(idbarangku));
                        }
                    }
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

                            Log.d("idbarangequalfav", "getFav: " + id_barang + " s " + idbarangku);
                        }else {
                            sendFavorit();
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

    private void getDetailBarangById(String id){
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.POST, DETAILKADO+id, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++){
                    JSONObject object = response.getJSONObject(i);
                    photobyid = object.getString("photo");
                    kodeBarangbyid = object.getString("kode_barang");
                    namabyid = object.getString("nama");
                    hargabyid = object.getInt("harga");
                    deskripsibyid = object.getString("deskripsi");

                    nama.setText(namabyid);
                    kodebarang.setText(kodeBarangbyid);
                    harga.setText("Rp. " + hargabyid);
                    desc.setText(deskripsibyid);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(arrayRequest);
    }
}
