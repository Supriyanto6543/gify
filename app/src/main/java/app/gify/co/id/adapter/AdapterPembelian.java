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

        TextView tanggal, invoice, namakado, penerima, resi, status;

        public MyPembelian(@NonNull View itemView) {
            super(itemView);
            tanggal = itemView.findViewById(R.id.tanggalpembelian);
            invoice = itemView.findViewById(R.id.invoice);
            namakado = itemView.findViewById(R.id.namapembelian);
            penerima = itemView.findViewById(R.id.penerima);
            resi = itemView.findViewById(R.id.resi);
            status = itemView.findViewById(R.id.statuspembelian);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pembelian, parent, false);
        return new MyPembelian(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (pembelians.get(position).getStatus() == 1){
            ((MyPembelian)holder).status.setText("Menunggu Pembayaran");
        }
        if (pembelians.get(position).getStatus() == 2){
            ((MyPembelian)holder).status.setText("Lunas (Proses)");
        }
        if (pembelians.get(position).getStatus() == 3){
            ((MyPembelian)holder).status.setText("Lunas (Dikirim)");
        }
        if (pembelians.get(position).getStatus() == 4){
            ((MyPembelian)holder).status.setText("Cancel / Return");
        }
        if (pembelians.get(position).getStatus() == 5){
            ((MyPembelian)holder).status.setText("Return");
        }

        ((MyPembelian)holder).invoice.setText(String.valueOf(pembelians.get(position).getIdpesanan()));
        ((MyPembelian)holder).namakado.setText(pembelians.get(position).getNama_barang());
        ((MyPembelian)holder).resi.setText(String.valueOf(pembelians.get(position).getResi()));
        ((MyPembelian)holder).penerima.setText(pembelians.get(position).getPenerima());
        ((MyPembelian)holder).tanggal.setText(pembelians.get(position).getTtl());
    }

    @Override
    public int getItemCount() {
        return pembelians.size();
    }
}
