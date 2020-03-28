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
import app.gify.co.id.modal.MadolFavorit;
import app.gify.co.id.modal.MadolKado;

import static app.gify.co.id.baseurl.UrlJson.GETFAV;

public class AdapterFavorit extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolFavorit> kados;
    View view;
    Context context;
    SharedPreferences preferences;
    String uid, id_barang;

    public AdapterFavorit(ArrayList<MadolFavorit> kados, Context context) {
        this.kados = kados;
        this.context = context;
    }

    class MyFav extends RecyclerView.ViewHolder {

        ImageView photo, favorit, nonfavorit;
        TextView harga, nama;
        LinearLayout linear;

        public MyFav(@NonNull View itemView) {
            super(itemView);
            linear = itemView.findViewById(R.id.linear);
            photo = itemView.findViewById(R.id.fovoriteAdapter);
            favorit = itemView.findViewById(R.id.favoritbarang);
            harga = itemView.findViewById(R.id.hargaBarangFavoritAdapter);
            nama = itemView.findViewById(R.id.namabarangfavorit);
            nonfavorit = itemView.findViewById(R.id.favoritku);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_barang_favorit, parent, false);
        return new MyFav(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyFav)holder).nama.setText(kados.get(position).getNama()+"("+kados.get(position).getKode()+")");
        ((MyFav)holder).harga.setText("Rp. " + kados.get(position).getHarga());
        if (kados.get(position).getGambar().isEmpty()){
            Toast.makeText(context, "tydac ada gambar", Toast.LENGTH_SHORT).show();
        }else {
            Picasso.get().load(kados.get(position).getGambar()).into(((MyFav)holder).photo);
        }
        ((MyFav)holder).linear.setOnClickListener(view1 -> {
            Intent intent = new Intent(context.getApplicationContext(), DetailKado.class);
            intent.putExtra("gambar", kados.get(position).getGambar());
            intent.putExtra("desc", kados.get(position).getDesc());
            intent.putExtra("harga", kados.get(position).getHarga());
            intent.putExtra("id", kados.get(position).getId_barang());
            intent.putExtra("idbarang", kados.get(position).getId_barang());
            if (((MyFav)holder).favorit.getVisibility()==View.VISIBLE){
                intent.putExtra("favorit", true);
            }
            intent.putExtra("nama", kados.get(position).getNama() + "(" + kados.get(position).getKode() + ")");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        getFav(position, holder);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        uid = preferences.getString("uid", "");
        Log.d("uidkua", "onBindViewHolder: " + id_barang + " s " + kados.get(position).getId_barang());

    }

    @Override
    public int getItemCount() {
        return kados.size();
    }

    public void filterList(ArrayList<MadolFavorit> filteredList){
        kados = filteredList;
        notifyDataSetChanged();
    }

    private void getFav(int position, RecyclerView.ViewHolder holder){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETFAV, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String idtetap = object.getString("id_tetap");
                    if (uid.contains(idtetap)){
                        int idbarang = object.getInt("id_barang");
                        if (kados.get(position).getId_barang().equalsIgnoreCase(String.valueOf(idbarang))){
                            ((MyFav)holder).favorit.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(objectRequest);
    }
}
