package app.gify.co.id.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;

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
    private ArrayList<MadolKado> filterArrayList;
    View view;
    Context context;
    SharedPreferences preferences;
    String uid, id_barang;
    private NameFilter nameFilter;

    public AdapterListKado(ArrayList<MadolKado> kados, Context context) {
        this.kados = kados;
        this.filterArrayList = kados;
        this.context = context;
    }

    class MyKado extends RecyclerView.ViewHolder {

        ImageView photo, favorit, favoritku;
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
        ((MyKado)holder).nama.setText(kados.get(position).getNama()+" ("+kados.get(position).getKode()+")");
        ((MyKado)holder).harga.setText("Rp. " + kados.get(position).getHarga() + ",-");

        if (kados.get(position).getGambar().isEmpty()){
            Toast.makeText(context, "tydac ada gambar", Toast.LENGTH_SHORT).show();
        }else {
            Glide.with(view).load(kados.get(position).getGambar()).into(((MyKado)holder).photo);

            Log.d("udin", kados.get(position).getGambar() + "");
        }
        String id = kados.get(position).getId_barang();
        ((MyKado)holder).linear.setOnClickListener(view1 -> {
            Intent intent = new Intent(context.getApplicationContext(), DetailKado.class);
            intent.putExtra("gambar", kados.get(position).getGambar());
            intent.putExtra("gambar1", kados.get(position).getGambar1());
            intent.putExtra("gambar2", kados.get(position).getGambar2());
            intent.putExtra("desc", kados.get(position).getDesc());
            intent.putExtra("harga", kados.get(position).getHarga());
            intent.putExtra("idbarang", kados.get(position).getId_barang());
            intent.putExtra("id", kados.get(position).getId_barang());
            intent.putExtra("posisibarang", position);
            intent.putExtra("nama", kados.get(position).getNama() + "(" + kados.get(position).getKode() + ")");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        uid = preferences.getString("uid", "");



    }

    @Override
    public int getItemCount() {
        return kados.size();
    }

    public Filter getFilter() {
        if (nameFilter == null) {
            nameFilter = new NameFilter();
        }
        return nameFilter;
    }

    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<MadolKado> filteredItems = new ArrayList<>();

                for (int i = 0, l = filterArrayList.size(); i < l; i++) {
                    String nameList = filterArrayList.get(i).getNama();
                    if (nameList.toLowerCase().contains(constraint))
                        filteredItems.add(filterArrayList.get(i));
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = filterArrayList;
                    result.count = filterArrayList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            kados = (ArrayList<MadolKado>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
