package app.gify.co.id.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterListKado;
import app.gify.co.id.baseurl.UrlJson;
import app.gify.co.id.modal.MadolKado;

import static app.gify.co.id.baseurl.UrlJson.GETBARANG;

public class List_Kado extends AppCompatActivity {

    AdapterListKado adapterListKado;
    RecyclerView recycler;
    ArrayList<MadolKado> madolKados;
    String kado, acara, range;
    GridLayoutManager glm;
    ImageView backDetailKado, cartListKado;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_kado);
        dialog  = new Dialog(List_Kado.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);
        Glide.with(List_Kado.this)
                .load(R.drawable.gifygif)
                .placeholder(R.drawable.gifygif)
                .centerCrop()
                .into(imageViewTarget);
        dialog.show();

        recycler = findViewById(R.id.recycler);
        cartListKado = findViewById(R.id.cartListKado);
        backDetailKado = findViewById(R.id.backDetailKado);
        backDetailKado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getApplicationContext()).openDrawer();
            }
        });

        cartListKado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(List_Kado.this, CartActivity.class));
            }
        });

        madolKados = new ArrayList<>();
        glm = new GridLayoutManager(getApplicationContext(), 2);
        recycler.setLayoutManager(glm);

        kado = getIntent().getStringExtra("buat").replace(" ", "%20");
        acara = getIntent().getStringExtra("acara").replace(" ", "%20");
        range = getIntent().getStringExtra("range");
        Log.d("rangesa", "onCreate: " + kado + " s " + acara + " s " + range + " s ");

        getBarang();

    }

    private void getBarang() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETBARANG+"?buat="+kado+"&ranges="+range+"&acara="+acara, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("YukNgaji");
                    Log.d("array", "onResponse: " + array.toString() + response.toString());
                    if (array.length() == 0){
                        Toast.makeText(List_Kado.this, "Belum ada barang", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(List_Kado.this, MainActivity.class);
                    }
                    for (int a = 0; a < array.length(); a++){
                        JSONObject object = array.getJSONObject(a);
                        String nama = object.getString("nama");
                        int harga = object.getInt("harga");
                        String gambar = object.getString("photo").replace(" ", "%20");
                        String tipe = object.getString("kode_barang");
                        String desc = object.getString("deskripsi");
                        String idbarang = object.getString("id");
                        MadolKado madolKado = new MadolKado(gambar, harga, nama, tipe, desc, idbarang);
                        madolKados.add(madolKado);
                        adapterListKado = new AdapterListKado(madolKados, getApplicationContext());
                        recycler.setAdapter(adapterListKado);
                        dialog.dismiss();
                        Log.d("listkadoharga", "onResponse: " + harga + tipe + " s " + idbarang + "\n" + gambar);
                    }
                } catch (JSONException e) {
                    Log.d("jsonbarangerror", "onResponse: " + e.getMessage());
                    dialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, error -> {
            Log.d("errorlistkadojson", "getBarang: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(objectRequest);
    }
}
