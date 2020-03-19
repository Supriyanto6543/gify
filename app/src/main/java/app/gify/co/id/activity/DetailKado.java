package app.gify.co.id.activity;

import android.content.Intent;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_barang);

        belikadodetail = findViewById(R.id.belikadodetail);
        slide = findViewById(R.id.carousel);
        nama = findViewById(R.id.namadetail);
        harga = findViewById(R.id.hargadetail);
        desc = findViewById(R.id.descdetail);

        int hargas;
        hargas = getIntent().getIntExtra("harga", -1);

        nama.setText(getIntent().getStringExtra("nama"));
        harga.setText("Rp. " + hargas);
        desc.setText(getIntent().getStringExtra("desc"));

        belikadodetail.setOnClickListener(view -> {
            popup();
        });
    }

    private void popup() {

        builder = new AlertDialog.Builder(getApplicationContext());
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_beli_barang, null);
        /*namapopup = view.findViewById(R.id.personal);
        jumlah = view.findViewById(R.id.keluarga);*/



        dialog = builder.create();
        dialog.setView(view);
        dialog.show();


    }
}
