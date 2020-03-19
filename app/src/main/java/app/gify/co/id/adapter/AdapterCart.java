package app.gify.co.id.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.modal.MadolCart;

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolCart> carts;
    View view;
    Context context;

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
        return new MyCart(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }
}
