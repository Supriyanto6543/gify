package app.gify.co.id.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.activity.DetailKado;
import app.gify.co.id.modal.MadolCart;
import app.gify.co.id.modal.MadolKado;

import static app.gify.co.id.baseurl.UrlJson.GETFAV;

public class AdapterListKado extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolKado> kados;
    View view;
    Context context;
    SharedPreferences preferences;
    String uid, id_barang;

    public AdapterListKado(ArrayList<MadolKado> kados, Context context) {
        this.kados = kados;
        this.context = context;
    }

    class MyKado extends RecyclerView.ViewHolder {

        ImageView photo, favorit;
        TextView harga, nama;
        LinearLayout linear;

        public MyKado(@NonNull View itemView) {
            super(itemView);
            linear = itemView.findViewById(R.id.linear);
            photo = itemView.findViewById(R.id.fovoriteAdapter);
            favorit = itemView.findViewById(R.id.favoritbarang);
            harga = itemView.findViewById(R.id.hargaBarangFavoritAdapter);
            nama = itemView.findViewById(R.id.namabarangfavorit);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_barang_favorit, parent, false);
        return new MyKado(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyKado)holder).nama.setText(kados.get(position).getNama()+"("+kados.get(position).getKode()+")");
        ((MyKado)holder).harga.setText("Rp. " + kados.get(position).getHarga());
        if (kados.get(position).getGambar().isEmpty()){
            Toast.makeText(context, "tydac ada gambar", Toast.LENGTH_SHORT).show();
        }else {
            Picasso.get().load(kados.get(position).getGambar()).into(((MyKado)holder).photo);
        }
        ((MyKado)holder).linear.setOnClickListener(view1 -> {
            Intent intent = new Intent(context.getApplicationContext(), DetailKado.class);
            intent.putExtra("gambar", kados.get(position).getGambar());
            intent.putExtra("desc", kados.get(position).getDesc());
            intent.putExtra("harga", kados.get(position).getHarga());
            intent.putExtra("nama", kados.get(position).getNama() + "(" + kados.get(position).getKode() + ")");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        uid = preferences.getString("uid", "");
        getFavorit(position, holder);
        Log.d("uidkua", "onBindViewHolder: " + id_barang + " s " + kados.get(position).getId_barang());

    }

    @Override
    public int getItemCount() {
        return kados.size();
    }

    private void getFavorit(int Position, RecyclerView.ViewHolder holder){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETFAV,null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String id_tetap = object.getString("id_tetap");
                    Log.d("idbarangfor", "getFavorit: " + id_tetap + " s " + uid);
                    if (id_tetap.contains(uid)){
                        id_barang = object.getString("id_barang");
                        Log.d("idbarangku", "getFavorit: " + id_tetap + uid);
                        if (kados.get(Position).getId_barang().equalsIgnoreCase(id_barang)){
                            ((MyKado)holder).favorit.setVisibility(View.VISIBLE);
                            Log.d("kados", "getFavorit: " + id_barang + kados.get(Position).getId_barang());
                        }else {
                            ((MyKado)holder).favorit.setVisibility(View.GONE);
                            Log.d("kadosfail", "getFavorit: " + id_barang + kados.get(Position).getId_barang());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("adapterlist", "getFavorit: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(objectRequest);
    }
}
