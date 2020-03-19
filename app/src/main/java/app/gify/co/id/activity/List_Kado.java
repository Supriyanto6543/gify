package app.gify.co.id.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterListKado;
import app.gify.co.id.baseurl.UrlJson;
import app.gify.co.id.modal.MadolKado;

import static app.gify.co.id.baseurl.UrlJson.GETBARANG;

public class List_Kado extends AppCompatActivity {

    int kado, acara, range;
    AdapterListKado adapterListKado;
    RecyclerView recycler;
    ArrayList<MadolKado> madolKados;
    GridLayoutManager glm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_kado);

        recycler = findViewById(R.id.recycler);

        madolKados = new ArrayList<>();
        glm = new GridLayoutManager(getApplicationContext(), 2);
        recycler.setLayoutManager(glm);

        kado = getIntent().getIntExtra("buat", -1);
        acara = getIntent().getIntExtra("acara", -1);
        range = getIntent().getIntExtra("range", -1);

        getBarang();

    }

    private void getBarang() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETBARANG, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("YukNgaji");
                    for (int a = 0; a < array.length(); a++){
                        JSONObject object = array.getJSONObject(a);
                        int buat = object.getInt("buat");
                        int ranges = object.getInt("ranges");
                        int acaraku = object.getInt("acara");
                        Log.d("acara", "onResponse: " + buat + " s " + kado + " s " + ranges + " s " + range + " s " + acaraku + " s " + acara);
                        if (buat == kado && ranges == range && acaraku == acara){
                            String nama = object.getString("nama");
                            int harga = object.getInt("harga");
                            String gambar = object.getString("photo");
                            String tipe = object.getString("kode_barang");
                            String desc = object.getString("deskripsi");
                            String idbarang = object.getString("id");
                            MadolKado madolKado = new MadolKado(gambar, harga, nama, tipe, desc, idbarang);
                            madolKados.add(madolKado);
                            adapterListKado = new AdapterListKado(madolKados, getApplicationContext());
                            recycler.setAdapter(adapterListKado);
                            Log.d("listkadoharga", "onResponse: " + harga + tipe + " s " + idbarang);
                        }

                    }
                } catch (JSONException e) {
                    Log.d("jsonbarangerror", "onResponse: " + e.getMessage());
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
