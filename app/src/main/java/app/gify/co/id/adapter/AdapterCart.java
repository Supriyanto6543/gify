package app.gify.co.id.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.activity.CartActivity;
import app.gify.co.id.modal.MadolCart;

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolCart> carts;
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
        ((MyCart)holder).harga.setText(String.valueOf(hargaku*kuantitas));
        ((MyCart)holder).nama.setText(carts.get(position).getNamacart());
        //Picasso.get().load(carts.get(position).getGambar()).into(((MyCart)holder).gambar);
        Glide.with(view).load(carts.get(position).getGambar()).into(((MyCart)holder).gambar);
        ((MyCart)holder).tambah.setOnClickListener(view1 -> {
            if (kuantitas<9){
                kuantitas = kuantitas + 1;
                ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas));
                ((MyCart)holder).harga.setText(String.valueOf(hargaku*kuantitas));
            }

        });
        ((MyCart)holder).kurang.setOnClickListener(view -> {

            if (kuantitas>1){
                kuantitas = kuantitas - 1;
                ((MyCart)holder).harga.setText(String.valueOf(hargaku*kuantitas));
                ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas));
            }

        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }
}
