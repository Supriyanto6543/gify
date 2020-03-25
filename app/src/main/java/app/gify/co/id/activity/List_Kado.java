package app.gify.co.id.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;

import app.gify.co.id.Fragment.favorit.FavoritFragment;
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
    ImageView backDetailKado, cartListKado, cariBarangListKado;
    Dialog dialog;
    EditText searchView;
    TextView tulisanListKado;
    SharedPreferences preferences;
    LayoutInflater inflater;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_kado);

        tulisanListKado = findViewById(R.id.tulisanListKado);
        cariBarangListKado = findViewById(R.id.cariBarangListKado);
        cariBarangListKado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
                tulisanListKado.setVisibility(View.GONE);
            }
        });
        dialog  = new Dialog(List_Kado.this);
        inflater = (LayoutInflater)getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.loading, null);
        ImageView goku = layout.findViewById(R.id.custom_loading_imageView);
        goku.animate().rotationBy(360).setDuration(3000).setInterpolator(new LinearInterpolator()).start();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(layout);
        dialog.show();

        searchView = findViewById(R.id.listKadoEdittext);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        recycler = findViewById(R.id.recycler);
        cartListKado = findViewById(R.id.cartListKado);
        backDetailKado = findViewById(R.id.backDetailKado);
        backDetailKado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        kado = preferences.getString("buatAcara", "").replace(" ", "%20");
        acara = preferences.getString("namaAcara", "").replace(" ", "%20");
        range = preferences.getString("namaRange", "").replace(" ", "%20");
        kado = preferences.getString("buat", "");
        acara = preferences.getString("acara", "");
        range = preferences.getString("range", "");
        Log.d("kadoacararange", "onCreate: " + kado + " s " + acara + " s " + range );

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
                        String gambar1 = object.getString("photo1").replace(" ", "%20");
                        String gambar2 = object.getString("photo2").replace(" ", "%20");
                        String tipe = object.getString("kode_barang");
                        String desc = object.getString("deskripsi");
                        String idbarang = object.getString("id");
                        //MadolKado madolKado = new MadolKado(gambar, harga, nama, tipe, desc, idbarang);
                        MadolKado madolKado = new MadolKado(gambar, gambar1, gambar2, harga, nama, tipe, desc, idbarang);
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

    private void filter(String text){
        ArrayList<MadolKado> filterKu = new ArrayList<>();
        for (MadolKado item : madolKados){
            if (item.getNama().toLowerCase().contains(text.toLowerCase()))
                filterKu.add(item);
        }

        if (adapterListKado == null){
            Toast.makeText(getApplicationContext(), "Kado tidak ditemukan", Toast.LENGTH_SHORT).show();
        }else{
            adapterListKado.filterList(filterKu);
        }
    }
}
