package app.gify.co.id.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import app.gify.co.id.R;
import app.gify.co.id.modal.MadolCart;

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolCart> carts;
    MadolCart mm;
    View view;
    View viewku;
    Context context;
    int kuantitas;

    public AdapterCart(ArrayList<MadolCart> carts, Context context) {
        this.carts = carts;
        this.context = context;
    }

    class MyCart extends RecyclerView.ViewHolder {

        ImageView gambar, tambah, kurang;
        TextView harga, nama, quantitas;

        public MyCart(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambarcart);
            tambah = itemView.findViewById(R.id.tambahcart);
            kurang = itemView.findViewById(R.id.kurangcart);
            harga = itemView.findViewById(R.id.hargacart);
            nama = itemView.findViewById(R.id.namacart);
            quantitas = itemView.findViewById(R.id.quantitas);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);
        viewku = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
        return new MyCart(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        kuantitas = carts.get(position).getJumlah();
        int hargaku = carts.get(position).getHarga();

        Locale locale = new Locale("id", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        ((MyCart)holder).harga.setText(format.format(Double.valueOf(hargaku*kuantitas)));
        ((MyCart)holder).nama.setText(carts.get(position).getNamacart());
        Glide.with(view).load(carts.get(position).getGambar()).into(((MyCart)holder).gambar);
        Intent intent = new Intent("message_subject_intent");
        intent.putExtra("name", String.valueOf((totalCart(carts))));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        Intent intentx = new Intent("message_subject_intent");
        //intentx.putExtra("name", );
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        ((MyCart)holder).tambah.setOnClickListener(view1 -> {
            if (kuantitas<9){
                kuantitas = kuantitas + 1;
                ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas));
                ((MyCart)holder).harga.setText(String.valueOf(hargaku*kuantitas));
                int total = hargaku * kuantitas;
                Intent intents = new Intent("message_subject_intent");
                intents.putExtra("name", String.valueOf((total)));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intents);
            }

        });
        ((MyCart)holder).kurang.setOnClickListener(view -> {

            if (kuantitas>1){
                kuantitas = kuantitas - 1;
                ((MyCart)holder).harga.setText(String.valueOf(hargaku*kuantitas));
                ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas));
                int total = hargaku * kuantitas;
                Intent intents = new Intent("message_subject_intent");
                intents.putExtra("name", String.valueOf((total)));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intents);
            }

        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public int totalCart(List<MadolCart> items){

        int totalPrice = 0;
        for(int i = 0 ; i < items.size(); i++) {
            totalPrice += items.get(i).getHarga();
        }

        return totalPrice;
    }
}
