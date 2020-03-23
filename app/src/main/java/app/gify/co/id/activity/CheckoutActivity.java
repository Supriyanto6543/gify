package app.gify.co.id.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.LineNumberReader;

import app.gify.co.id.R;

public class CheckoutActivity extends AppCompatActivity {

    Button prosescekout;
    ImageView back;

    EditText NamaPenerima, NoPenerima;
    String currentUserID, Lnama, LNohp, Lalamat;
    TextView textViewCheckOutAlamat;

    DatabaseReference RootRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        RootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        textViewCheckOutAlamat = findViewById(R.id.textviewAlamatCheckout);
        NamaPenerima = findViewById(R.id.namaPenerimaCheckout);
        NoPenerima = findViewById(R.id.noHPPenerimaCheckout);

        prosescekout = findViewById(R.id.prorsesCheckout);
        back = findViewById(R.id.backCheckout);
        back.setOnClickListener(v -> finish());

        prosescekout.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, Pembelian.class);
            startActivity(intent);
        });

        getCheck();
    }

    private void getCheck() {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Lnama = dataSnapshot.child("nama").getValue().toString();
                        LNohp = dataSnapshot.child("noHp").getValue().toString();

                        if (!dataSnapshot.child("alamat").exists()) {
                            Toast.makeText(getApplicationContext(), "isi alamat terlebih dahulu di pengaturan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                            startActivity(intent);
                        } else {
                            Lalamat = dataSnapshot.child("alamat").getValue().toString();
                            textViewCheckOutAlamat.setText(Lalamat);
                            NamaPenerima.setText(Lnama);
                            NoPenerima.setText(LNohp);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
