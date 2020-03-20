package app.gify.co.id.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.modal.MadolCart;
import app.gify.co.id.modal.MadolPembelian;

public class AdapterPembelian extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolPembelian> pembelians;
    View view;
    Context context;

    public AdapterPembelian(ArrayList<MadolPembelian> pembelians, Context context) {
        this.pembelians = pembelians;
        this.context = context;
    }

    class MyPembelian extends RecyclerView.ViewHolder {

        public MyPembelian(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);
        return new MyPembelian(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return pembelians.size();
    }
}
