package app.gify.co.id.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import app.gify.co.id.R;
import app.gify.co.id.activity.List_Kado;
import app.gify.co.id.modal.MadolCart;

public class AdapterCart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<MadolCart> carts;
    MadolCart mm;
    View view;
    View viewku;
    Context context;
    int kuantitas;
    String totalname;
    int totalBerat, totalharga;
    TextView totalhargas, totalberats;

    public AdapterCart(ArrayList<MadolCart> carts, Context context, TextView totalhargas, TextView totalberats) {
        this.carts = carts;
        this.context = context;
        this.totalhargas = totalhargas;
        this.totalberats = totalberats;
    }

    public class MyCart extends RecyclerView.ViewHolder {

        public ImageView gambar, tambah, kurang;
        public TextView harga, nama, quantitas;
        public RelativeLayout background, foreground;
        public ElegantNumberButton quantity;

        public MyCart(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambarcart);
            tambah = itemView.findViewById(R.id.tambahcart);
            kurang = itemView.findViewById(R.id.kurangcart);
            harga = itemView.findViewById(R.id.hargacart);
            nama = itemView.findViewById(R.id.namacart);
            quantitas = itemView.findViewById(R.id.quantitas);
            background = itemView.findViewById(R.id.background);
            foreground = itemView.findViewById(R.id.foreground);
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
        int hargaku = carts.get(position).getHarga() * kuantitas;
        for (int a = 0; a < carts.size(); a++){
            Log.d("cartsizeku", "onBindViewHolder: " + carts.size()+ " s " + carts.get(a).getNamacart() + " s " + carts.get(a).getHarga());
            String nama = carts.get(position).getNamacart();
            if (nama.equals(carts.get(a).getNamacart())){
                totalhargas.setText(String.valueOf(totalCart(carts, carts.get(a).getNamacart())));
                totalberats.setText(String.valueOf(beratCart(carts, carts.get(a).getNamacart())));
            }

        }

        Locale locale = new Locale("id", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        ((MyCart)holder).harga.setText(format.format(Double.valueOf(hargaku)));
        ((MyCart)holder).nama.setText(carts.get(position).getNamacart());
        Glide.with(view).load(carts.get(position).getGambar()).into(((MyCart)holder).gambar);
        Intent intent = new Intent("message_subject_intent");
//        intent.putExtra("name", String.valueOf((totalCart(carts))));
        intent.putExtra("title", String.valueOf((getName(carts))));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


        ((MyCart) holder).tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(((MyCart) holder).quantitas.getText().toString());
                if (count<9){
                    count+=1;
                    ((MyCart)holder).quantitas.setText(String.valueOf(count));
                    carts.get(position).setQuantity(count);
                    int harga = carts.get(position).getHarga()*count;
                    ((MyCart)holder).harga.setText(String.valueOf(format.format(Double.valueOf(harga))));
                    String nama = carts.get(position).getNamacart();
                    totalhargas.setText(String.valueOf(totalCart(carts, nama)));
                    totalberats.setText(String.valueOf(beratCart(carts, nama)));
                }

//                ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas));
                    Intent intents = new Intent("message_subject_intent");
                    intents.putExtra("name", String.valueOf((getName(carts))));
                    intents.putExtra("qty", String.valueOf((getSeperatedquantity(carts))));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intents);
            }
        });
        ((MyCart) holder).kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(((MyCart)holder).quantitas.getText().toString());
                if (count>1){
                    count-=1;
                    ((MyCart)holder).quantitas.setText(String.valueOf(count));
                    carts.get(position).setQuantity(count);
                    int harga = carts.get(position).getHarga()*count;
                    ((MyCart)holder).harga.setText(String.valueOf(format.format(Double.valueOf(harga))));
                    String nama = carts.get(position).getNamacart();
                    totalhargas.setText(String.valueOf(kurangtotalcart(carts, nama)));
                    totalberats.setText(String.valueOf(kurangberatCart(carts, nama)));
                }


//                ((MyCart)holder).quantitas.setText(String.valueOf(kuantitas));
                    Intent intents = new Intent("message_subject_intent");
                    intents.putExtra("name", String.valueOf((getName(carts))));
                    intents.putExtra("qty", String.valueOf((getSeperatedquantity(carts))));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intents);


            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public int totalCart(ArrayList<MadolCart> items, String name){

        for(int i = 0 ; i < items.size(); i++) {
            totalname = items.get(i).getNamacart();
            if (totalname.equals(name)){
                totalharga += items.get(i).getHarga();
            }
        }
        return totalharga;
    }
    public int kurangtotalcart(ArrayList<MadolCart> items, String name){

        for(int i = 0 ; i < items.size(); i++) {
            totalname = items.get(i).getNamacart();
            if (totalname.equals(name)){
                totalharga -= items.get(i).getHarga();
            }
        }
        return totalharga;
    }

    public int beratCart(ArrayList<MadolCart> items, String name){
        for(int i = 0 ; i < items.size(); i++) {
            totalname = items.get(i).getNamacart();
            if (totalname.equals(name)){
                totalBerat += items.get(i).getBerat();
            }
        }
        return totalBerat;
    }

    public int kurangberatCart(ArrayList<MadolCart> items, String name){
        for(int i = 0 ; i < items.size(); i++) {
            totalname = items.get(i).getNamacart();
            if (totalname.equals(name)){
                totalBerat -= items.get(i).getBerat();
            }
        }
        return totalBerat;
    }

    public String getName(List<MadolCart> name){

        String ku = "";
        for (int i = 0; i < name.size(); i++){
            ku += name.get(i).getNamacart() + ", ";
        }

        return ku;
    }

    public String getSeperatedquantity(List<MadolCart> quantity){

        String kus = "";
        for (int i = 0; i < quantity.size(); i++){
            kus += quantity.get(i).getQuantity() + ", ";
        }

        return kus;
    }

    public void removeItem(int item){
        carts.remove(item);

        notifyItemRemoved(item);
    }

    public void restoreItem(MadolCart madolCart, int item){
        carts.add(item, madolCart);

        notifyItemInserted(item);
    }

    public void quantityPlus(MadolCart madolCart, int item){


    }

}
