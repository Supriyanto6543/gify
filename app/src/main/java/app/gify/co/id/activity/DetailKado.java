package app.gify.co.id.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import app.gify.co.id.Fragment.favorit.FavoritFragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import app.gify.co.id.Fragment.home.HomeFragment;
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
    CarouselView carouselView;
    int sourceImg[] = {R.drawable.lupa_password_background, R.drawable.profile_image};
    ImageView buatJadiWistlist, back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_barang_nocard);

        back = findViewById(R.id.backDetailKado);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buatJadiWistlist = findViewById(R.id.buatJadiWistlist);
        buatJadiWistlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FavoritFragment fragment = new FavoritFragment();
                fm.beginTransaction().add(R.id.frameFavorite,fragment).commit();
            }
        });

        belikadodetail = findViewById(R.id.belikadodetail);
        slide = findViewById(R.id.carousel);
        nama = findViewById(R.id.namadetail);
        harga = findViewById(R.id.hargadetail);
        desc = findViewById(R.id.descdetail);

        //carousel
        carouselView = (CarouselView) findViewById(R.id.carousel);
        carouselView.setPageCount(sourceImg.length);
        carouselView.setImageListener(slideImage);

        hargas = getIntent().getIntExtra("harga", -1);

        nama.setText(getIntent().getStringExtra("nama"));
        harga.setText("Rp. " + hargas);
        desc.setText(getIntent().getStringExtra("desc"));

        belikadodetail.setOnClickListener(view -> {
            popup();
        });
    }


    ImageListener slideImage = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sourceImg[position]);
        }
    };

    public void replaceFragment(Fragment someFragment) {
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameFavorite, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
            hargapopupdown.setText("Rp. " + hargas*cingpai);
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
