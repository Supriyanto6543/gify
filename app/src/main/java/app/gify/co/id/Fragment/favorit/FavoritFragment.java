package app.gify.co.id.Fragment.favorit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.activity.CartActivity;
import app.gify.co.id.activity.MainActivity;
import app.gify.co.id.adapter.AdapterFavorit;
import app.gify.co.id.modal.MadolFavorit;
import app.gify.co.id.modal.MadolKado;

import static app.gify.co.id.baseurl.UrlJson.GETBARANG;
import static app.gify.co.id.baseurl.UrlJson.GETFAV;


public class FavoritFragment extends Fragment {

    ImageView navView, toChart, ya, cari;
    AdapterFavorit adapterFavorit;
    TextView textView;
    RecyclerView recyclerView;
    ArrayList<MadolFavorit> kados;
    GridLayoutManager glm;
    SharedPreferences preferences;
    String uid, id_barang;
    EditText cariBarang, searchView;
    ProgressDialog mDialog;
    NavigationView navigationView;
    Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorit, container, false);
        recyclerView = view.findViewById(R.id.recyclerfavorit);
        ya = view.findViewById(R.id.cariBarangFavoritYa);
        cariBarang = view.findViewById(R.id.cariBarangEdittext);
        textView = view.findViewById(R.id.textFavoriteFavorite);
        cari = view.findViewById(R.id.cariBarangFavorit);
        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ya.setVisibility(View.VISIBLE);
                cari.setVisibility(View.GONE);
                cariBarang.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }
        });

        searchView = view.findViewById(R.id.listKadoEdittext);
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

        toChart = view.findViewById(R.id.chartBarangFavorit);
        toChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        dialog  = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);
        Glide.with(getActivity())
                .load(R.drawable.gifygif)
                .placeholder(R.drawable.gifygif)
                .centerCrop()
                .into(imageViewTarget);
        dialog.show();

        kados = new ArrayList<>();
        glm = new GridLayoutManager(getActivity(), 2);

        recyclerView.setLayoutManager(glm);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = preferences.getString("uid", "");
        getFavorit();

        navView = view.findViewById(R.id.backtoMenuFavorit);
        navView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).openDrawer();


            }
        });
        return view;
    }

    private void getBarang() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETBARANG, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("YukNgaji");
                    for (int a = 0; a < array.length(); a++){
                        JSONObject object = array.getJSONObject(a);
                        String nama = object.getString("nama");
                        int harga = object.getInt("harga");
                        String gambar = object.getString("photo");
                        String tipe = object.getString("kode_barang");
                        String desc = object.getString("deskripsi");
                        String idbarang = object.getString("id");
                        Log.d("idbarang", "onResponse: " + idbarang + " s " +id_barang);
                        if (idbarang.equalsIgnoreCase(id_barang)){
                            Log.d("idbarangif", "onResponse: " + idbarang + id_barang);
                            MadolFavorit madolFavorit = new MadolFavorit(gambar, harga, nama, tipe, desc, idbarang);
                            kados.add(madolFavorit);
                            adapterFavorit = new AdapterFavorit(kados, getContext());
                            recyclerView.setAdapter(adapterFavorit);
                            dialog.dismiss();
                        }
                        Log.d("listkadoharga", "onResponse: " + harga + tipe + " s " + idbarang);

                    }
                } catch (JSONException e) {
                    Log.d("jsonbarangerror", "onResponse: " + e.getMessage());
                    e.printStackTrace();
                }

            }
        }, error -> {
            Log.d("errorlistkadojson", "getBarang: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(objectRequest);
    }

    private void getFavorit(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETFAV,null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String id_tetap = object.getString("id_tetap");
                    Log.d("idbarangfor", "getFavorit: " + id_tetap + " s " + uid);
                    if (id_tetap.contains(uid)){
                        id_barang = object.getString("id_barang");
                        Log.d("idbarangku", "getFavorit: " + id_tetap + uid + id_barang);
                        getBarang();
                    }else {
                        dialog.dismiss();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("adapterlist", "getFavorit: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(objectRequest);
    }

    private void filter(String text){
        ArrayList<MadolFavorit> filterKu = new ArrayList<>();
        for (MadolFavorit item : kados){
            if (item.getNama().toLowerCase().contains(text.toLowerCase()))
                filterKu.add(item);
        }
        adapterFavorit.filterList(filterKu);
    }
}
