package app.gify.co.id.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.synnapps.carouselview.CarouselView;

import app.gify.co.id.R;

public class DetailKado extends AppCompatActivity {

    CarouselView slide;
    TextView nama, harga, desc, namapopup, jumlah, hargapopuptop, hargapopupdown;
    Button belikadodetail;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button proses, batal;
    ImageView tambah, kurang;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int hargas;
    int cingpai = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_barang);

        belikadodetail = findViewById(R.id.belikadodetail);
        slide = findViewById(R.id.carousel);
        nama = findViewById(R.id.namadetail);
        harga = findViewById(R.id.hargadetail);
        desc = findViewById(R.id.descdetail);


        hargas = getIntent().getIntExtra("harga", -1);

        nama.setText(getIntent().getStringExtra("nama"));
        harga.setText("Rp. " + hargas);
        desc.setText(getIntent().getStringExtra("desc"));

        belikadodetail.setOnClickListener(view -> {
            popup();
        });
    }

    private void popup() {

        builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_beli_barang, null);
        namapopup = view.findViewById(R.id.nama);
        jumlah = view.findViewById(R.id.jumlah);
        hargapopuptop = view.findViewById(R.id.hargapopuptop);
        hargapopupdown = view.findViewById(R.id.hargabrng);
        proses = view.findViewById(R.id.buttonproses);
        batal = view.findViewById(R.id.buttonbatal);
        tambah = view.findViewById(R.id.tambahkuantitas);
        kurang = view.findViewById(R.id.kurangkuantitas);


        namapopup.setText(getIntent().getStringExtra("nama"));
        hargapopuptop.setText("Rp. " + hargas);
        hargapopupdown.setText("Rp. " + hargas);

        tambah.setOnClickListener(view1 -> {
            if (cingpai==9){

            }else {
                cingpai = cingpai + 1;
            }
            jumlah.setText(String.valueOf(cingpai));
            hargapopupdown.setText("Rp. " + hargas*cingpai);;
        });
        kurang.setOnClickListener(view1 -> {
            if (cingpai==1){

            }else {
                cingpai = cingpai - 1;
            }

            jumlah.setText(String.valueOf(cingpai));
            hargapopupdown.setText("Rp. " + hargas*cingpai);;
        });

        batal.setOnClickListener(view1 -> dialog.dismiss());
        proses.setOnClickListener(view1 -> {
            Intent intent = new Intent(DetailKado.this, CartActivity.class);
            intent.putExtra("nama", getIntent().getStringExtra("nama"));
            intent.putExtra("gambar", getIntent().getStringExtra("gambar"));
            intent.putExtra("idbarang", Integer.valueOf(getIntent().getStringExtra("idbarang")));
            intent.putExtra("hargas", hargas);
            intent.putExtra("quantity", cingpai);
            startActivity(intent);
        });

        dialog = builder.create();
        dialog.setView(view);
        dialog.show();



    }
}
