package app.gify.co.id.Fragment.pembelian;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.R;
import app.gify.co.id.activity.CartActivity;
import app.gify.co.id.activity.MainActivity;
import app.gify.co.id.adapter.AdapterPembelian;
import app.gify.co.id.modal.MadolPembelian;

import static app.gify.co.id.baseurl.UrlJson.GETPEMBELIAN;


public class PembelianFragment extends Fragment {

    ImageView navView, toChart;
    Button cariKadoPembelian;
    RecyclerView rc;
    GridLayoutManager glm;
    String uid;
    SharedPreferences preferences;
    ArrayList<MadolPembelian> pembelians;
    AdapterPembelian adapterPembelian;
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pembelian, container, false);
        cariKadoPembelian = view.findViewById(R.id.cariKadoPembelians);
        cariKadoPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                HomeFragment llf = new HomeFragment();
                ft.replace(R.id.frame, llf);
                ft.commit();
            }
        });
        dialog  = new Dialog(getActivity());
        inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.loading, null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(layout);
        dialog.show();

        pembelians = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        uid = preferences.getString("uid", "0");

        rc = view.findViewById(R.id.pembelianrecycle);

        glm = new GridLayoutManager(getContext(), 1);

        toChart = view.findViewById(R.id.cartPembelian);
        toChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        navView = view.findViewById(R.id.backPembelian);
        navView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        Log.d("uidpembelian", "onCreateView: " + uid);
        getPembelian();
        rc.setLayoutManager(glm);
        return view;
    }



    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();

            return true;
        }

        return false;
    }

    private void getPembelian(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GETPEMBELIAN+"?id_tetap="+uid, null ,response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    int idpesanan = object.getInt("id");
                    String id_tetap = object.getString("id_tetap");
                    String ttl = object.getString("ttl");
                    String penerima = object.getString("penerima");
                    String alamat = object.getString("alamat");
                    String kelurahan = object.getString("kelurahan");
                    String kecamatan = object.getString("kecamatan");
                    String kota = object.getString("kota");
                    String provinsi = object.getString("provinsi");
                    Log.d("getuidp", "onCreateView: " + uid);
                    String resi = object.getString("resi");
                    int status = object.getInt("status");
                    String nama_barang = object.getString("nama_barang");
                    String ucapan = object.getString("ucapan");

                    MadolPembelian pembelian = new MadolPembelian(idpesanan, status, id_tetap, ttl, penerima, alamat, kelurahan, kecamatan, kota, provinsi, resi, nama_barang, ucapan);
                    pembelians.add(pembelian);
                    adapterPembelian = new AdapterPembelian(pembelians, getContext());
                    rc.setAdapter(adapterPembelian);
                    /*dialog.dismiss();*/
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("errexget", "onCreateView: " + e.getMessage());
            }
        }, error -> {
            Log.d("errorexget", "onCreateView: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }




}
