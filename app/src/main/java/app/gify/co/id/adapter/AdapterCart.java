package app.gify.co.id.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.gify.co.id.R;
import app.gify.co.id.activity.CartActivity;
import app.gify.co.id.modal.MadolCart;
import app.gify.co.id.modal.MadolTotal;

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolCart> carts;
    View view;
    View viewku;
    Context context;
    int kuantitas1, kuantitas2, kuantitas3, kuantitas4, kuantitas5;
    int harga1, harga2, harga3, harga4, harga5, total1, total2, total3, total4, total5;
    Boolean barang1= false, barang2= false, barang3= false, barang4= false, barang5= false;
    TextView totalharga, totalbarang;

    public AdapterCart(ArrayList<MadolCart> carts, Context context, TextView totalbarang, TextView totalharga) {
        this.carts = carts;
        this.context = context;
        this.totalbarang = totalbarang;
        this.totalharga = totalharga;
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
        int hargaku = carts.get(position).getHarga();
        int kuantitasawal = carts.get(position).getJumlah();
        Log.d("cartsize", "onBindViewHolder: " + carts.size());
        if (carts.size() == 5){
            kuantitas1 = carts.get(0).getJumlah();
            harga1 = carts.get(0).getHarga();
            kuantitas2 = carts.get(1).getJumlah();
            harga2 = carts.get(1).getHarga();
            kuantitas3 = carts.get(2).getJumlah();
            harga3 = carts.get(2).getHarga();
            kuantitas4 = carts.get(3).getJumlah();
            harga4 = carts.get(3).getHarga();
            kuantitas5 = carts.get(4).getJumlah();
            harga5 = carts.get(4).getHarga();
            Log.d("cars1", "onBindViewHolder: " + harga1);
        }else if (carts.size() == 4){
            kuantitas1 = carts.get(0).getJumlah();
            harga1 = carts.get(0).getHarga();
            kuantitas2 = carts.get(1).getJumlah();
            harga2 = carts.get(1).getHarga();
            kuantitas3 = carts.get(2).getJumlah();
            harga3 = carts.get(2).getHarga();
            kuantitas4 = carts.get(3).getJumlah();
            harga4 = carts.get(3).getHarga();
            Log.d("cars12", "onBindViewHolder: " + harga2);
        }else if (carts.size() == 3){
            kuantitas1 = carts.get(0).getJumlah();
            harga1 = carts.get(0).getHarga();
            kuantitas2 = carts.get(1).getJumlah();
            harga2 = carts.get(1).getHarga();
            kuantitas3 = carts.get(2).getJumlah();
            harga3 = carts.get(2).getHarga();
            Log.d("cars13", "onBindViewHolder: " + harga3);
        }else if (carts.size() == 2){
            kuantitas1 = carts.get(0).getJumlah();
            harga1 = carts.get(0).getHarga();
            kuantitas2 = carts.get(1).getJumlah();
            harga2 = carts.get(1).getHarga();
            Log.d("cars14", "onBindViewHolder: " + harga4);
        }else if (carts.size() == 1){
            kuantitas1 = carts.get(0).getJumlah();
            harga1 = carts.get(0).getHarga();
            Log.d("cars15", "onBindViewHolder: " + harga5);
        }

        ((MyCart)holder).harga.setText(String.valueOf(hargaku*kuantitasawal));
        ((MyCart)holder).nama.setText(carts.get(position).getNamacart());
        Glide.with(view).load(carts.get(position).getGambar()).into(((MyCart)holder).gambar);
        Log.d("preposisi", "onBindViewHolder: " + position);
        ((MyCart)holder).tambah.setOnClickListener(view1 -> {
            Log.d("tambahbutton", "onBindViewHolder: ");
            if (position == 0){
                Log.d("tambahbuttonp", "onBindViewHolder: " + 0);
                if (kuantitas1 < 9){
                    Log.d("tambahbuttoni", "onBindViewHolder: " + kuantitas1);
                    kuantitas1+=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga1*kuantitas1));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas1));
                    barang1 = true;
                    total1 = 0;
                    total1 = harga1*kuantitas1;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang1");
                    intent.putExtra("quantity", kuantitas1);
                    intent.putExtra("harga", harga1);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
            if (position == 1){
                Log.d("tambahbuttonp", "onBindViewHolder: " + 1);
                if (kuantitas2 < 9){
                    Log.d("tambahbuttoni", "onBindViewHolder: " + kuantitas2);
                    kuantitas2+=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga2*kuantitas2));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas2));
                    barang2 = true;
                    total2 = 0;
                    total2 = harga2*kuantitas2;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang2");
                    intent.putExtra("quantity", kuantitas2);
                    intent.putExtra("harga", harga2);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
            if (position == 2){
                if (kuantitas3 < 9){
                    kuantitas3+=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga3*kuantitas3));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas3));
                    barang3 = true;
                    total3 = 0;
                    total3 = harga3*kuantitas3;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang3");
                    intent.putExtra("quantity", kuantitas3);
                    intent.putExtra("harga", harga3);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }

            }
            if (position == 3){
                if (kuantitas4 < 9){
                    kuantitas4+=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga4*kuantitas4));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas4));
                    barang4 = true;
                    total4 = 0;
                    total4 = harga4*kuantitas4;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang4");
                    intent.putExtra("quantity", kuantitas4);
                    intent.putExtra("harga", harga4);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }

            }
            if (position == 4){
                if (kuantitas5 < 9){
                    kuantitas5+=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga5*kuantitas5));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas5));
                    barang5 = true;
                    total5 = 0;
                    total5 = harga5*kuantitas5;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang4");
                    intent.putExtra("quantity", kuantitas5);
                    intent.putExtra("harga", harga5);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }

        });
        ((MyCart)holder).kurang.setOnClickListener(view -> {
            if (position == 0){
                if (kuantitas1 > 1){
                    kuantitas1-=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga1*kuantitas1));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas1));
                    barang1 = true;
                    total1 = 0;
                    total1 = harga1*kuantitas1;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang1");
                    intent.putExtra("quantity", kuantitas1);
                    intent.putExtra("harga", harga1);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }else if (position == 1){
                if (kuantitas2 > 1){
                    kuantitas2-=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga2*kuantitas2));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas2));
                    barang2 = true;
                    total2 = 0;
                    total2 = harga2*kuantitas2;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang2");
                    intent.putExtra("quantity", kuantitas2);
                    intent.putExtra("harga", harga2);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }else if (position == 2){
                if (kuantitas3 > 1){
                    kuantitas3-=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga3*kuantitas3));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas3));
                    barang3 = true;
                    total3 = 0;
                    total3 = harga3*kuantitas3;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang3");
                    intent.putExtra("quantity", kuantitas3);
                    intent.putExtra("harga", harga3);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }

            }else if (position == 3){
                if (kuantitas4 > 1){
                    kuantitas4-=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga4*kuantitas4));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas4));
                    barang4 = true;
                    total4 = 0;
                    total4 = harga4*kuantitas4;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang4");
                    intent.putExtra("quantity", kuantitas4);
                    intent.putExtra("harga", harga4);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }

            }else if (position == 4){
                if (kuantitas5 > 1){
                    kuantitas5-=1;
                    ((MyCart)holder).harga.setText(String.valueOf(harga5*kuantitas5));
                    ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas5));
                    barang5 = true;
                    total5 = 0;
                    total5 = harga5*kuantitas5;
                    totalharga.setText(String.valueOf(total1+total2+total3+total4+total5));
                    Intent intent = new Intent("barang4");
                    intent.putExtra("quantity", kuantitas5);
                    intent.putExtra("harga", harga5);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }
}
