package app.gify.co.id.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.gify.co.id.Fragment.favorit.FavoritFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.viewpager.widget.ViewPager;

import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterFavorit;
import app.gify.co.id.adapter.AdapterViewPager;

import static app.gify.co.id.baseurl.UrlJson.CHECKFAV;
import static app.gify.co.id.baseurl.UrlJson.DETAILKADO;
import static app.gify.co.id.baseurl.UrlJson.CHECKCART;
import static app.gify.co.id.baseurl.UrlJson.DELETEFAV;
import static app.gify.co.id.baseurl.UrlJson.GETBARANG;
import static app.gify.co.id.baseurl.UrlJson.GETCART;
import static app.gify.co.id.baseurl.UrlJson.GETFAV;
import static app.gify.co.id.baseurl.UrlJson.SENDCART;
import static app.gify.co.id.baseurl.UrlJson.SENDFAV;

public class DetailKado extends AppCompatActivity {

    ImageView slide;
    TextView nama, harga, desc, namapopup, jumlah, hargapopuptop, hargapopupdown, kodebarang;
    Button belikadodetail;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button proses, batal;
    ImageView tambah, kurang, favorit, unfavorit, cart;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    CarouselView carouselView;
    String idbarangku, uid, id, photobyid, kodeBarangbyid, namabyid, deskripsibyid, berat, getJumlah, photobyid1, photobyid2;
    int posisibarang;
    int id_barang, hargabyid, cingpai = 1, gambar, gambar1, gambar2, hargaAwal;
    int[] sourceImg;
    Boolean faforit;
    ImageView buatJadiWistlist, back;
    ArrayList<String> imgUrls;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_barang_nocard);
        cart = findViewById(R.id.chartDetailKado);
        imgUrls = new ArrayList<>();
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailKado.this, CartActivity.class));
            }
        });

        posisibarang = getIntent().getIntExtra("posisibarang", -1);

        back = findViewById(R.id.backDetailKado);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d("jinjin", hargabyid + "");

        belikadodetail = findViewById(R.id.belikadodetail);
        carouselView = findViewById(R.id.carousel);
        nama = findViewById(R.id.namadetail);
        //kodebarang = findViewById(R.id.kodebarang);
        harga = findViewById(R.id.hargadetail);
        desc = findViewById(R.id.descdetails);
        favorit = findViewById(R.id.favoritdeta);
        unfavorit = findViewById(R.id.unfavoritdet);

        id = getIntent().getStringExtra("id");

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

        getDetailBarangById(id);

        belikadodetail.setOnClickListener(view -> {
            popup();
        });

        /*Toast.makeText(getApplicationContext(), "Id mu adalah " + id, Toast.LENGTH_LONG).show();*/
    }

    /*ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(final int position, final ImageView imageView) {

            Glide.with(DetailKado.this)
                    .asBitmap()
                    .load(imgUrls.get(position))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@Nullable Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageView.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                    });
        }

    };*/
    private void delfav() {
        StringRequest request = new StringRequest(Request.Method.GET, DELETEFAV+"?idtetap="+uid+"&idbarang="+idbarangku, response -> {
            if (response.equalsIgnoreCase("bisa")){
                Toast.makeText(this, "Barang telah di hapus dari favorite", Toast.LENGTH_SHORT).show();
                getIntent().removeExtra("favorit");
                Intent intent = new Intent(DetailKado.this, List_Kado.class);
                startActivity(intent);
                finish();
                favorit.setVisibility(View.GONE);
                unfavorit.setVisibility(View.VISIBLE);
            }
        }, error -> {

        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

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
                cingpai += 1;
            }
            jumlah.setText(String.valueOf(cingpai));
            getJumlah = jumlah.getText().toString();
            hargapopupdown.setText("Rp. " + hargabyid*cingpai);
            hargaAwal = hargabyid * cingpai;
        });
        kurang.setOnClickListener(view1 -> {
            if (cingpai==1){

            }else {
                cingpai = cingpai - 1;
            }

            jumlah.setText(String.valueOf(cingpai));
            getJumlah = jumlah.getText().toString();
            hargapopupdown.setText("Rp. " + hargabyid*cingpai);;
            hargaAwal = hargabyid * cingpai;
        });

        hargaAwal = hargabyid;
        jumlah.setText(String.valueOf(1));
        getJumlah = jumlah.getText().toString();

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
                    intent.putExtra("harga", hargaAwal);
                    intent.putExtra("quantitys", cingpai);
                    Log.d("hargaAwalInt", hargaAwal + "");
                    startActivity(intent);
                    finish();
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
                params.put("berat", berat);
                params.put("harga", String.valueOf(hargabyid));
                params.put("quantity", getJumlah);
                Log.d("lele", getJumlah + "");
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
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, CHECKFAV+uid+"&idbarang="+idbarangku, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                Log.d("trycart", "getCart: " + response + array.length());
                Log.d("nullsebelum", "getCart: " + array.toString());
                if (array.isNull(0)){
                    sendFavorit();
                }else {
                    Toast.makeText(this, "Barang sudah ada di cart", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Log.d("exceptioncart", "getCart: " + e.getMessage());
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
                    photobyid1 = object.getString("photo1");
                    photobyid2 = object.getString("photo2");
                    namabyid = object.getString("nama");
                    hargabyid = object.getInt("harga");
                    deskripsibyid = object.getString("deskripsi");
                    kodeBarangbyid = object.getString("kode_barang");
                    Log.d("descharga", "getDetailBarangById: " + hargabyid + " s " + deskripsibyid + " s " + photobyid);
                    nama.setText(namabyid + "(" + kodeBarangbyid + ")");
                    berat = object.getString("berat");
                    ImageListener imageListener = new ImageListener() {
                        @Override
                        public void setImageForPosition(final int position, final ImageView imageView) {

                            Glide.with(DetailKado.this)
                                    .asBitmap()
                                    .load(imgUrls.get(position))
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@Nullable Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            imageView.setImageBitmap(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {

                                        }

                                    });
                        }

                    };
                    imgUrls.add(photobyid);
                    imgUrls.add(photobyid1);
                    imgUrls.add(photobyid2);
                    carouselView.setImageListener(imageListener);
                    carouselView.setPageCount(imgUrls.size());
                    carouselView.isDisableAutoPlayOnUserInteraction();



                    nama.setText(namabyid + " (" + kodeBarangbyid + ")");
                    /*Glide.with(DetailKado.this).load(photobyid,photobyid1,photobyid2).into(slide);*/
                    //kodebarang.setText(kodeBarangbyid);
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
