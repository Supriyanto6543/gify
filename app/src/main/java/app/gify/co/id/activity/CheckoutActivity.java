package app.gify.co.id.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.LineNumberReader;

import app.gify.co.id.Fragment.pembelian.PembelianFragment;
import app.gify.co.id.R;

public class CheckoutActivity extends AppCompatActivity {

    Button prosescekout;
    ImageView back;

    EditText NamaPenerima, NoPenerima;
    String currentUserID, Lnama, LNohp, Lalamat;
    EditText textViewCheckOutAlamat;
    Spinner provinsi, kota;

    DatabaseReference RootRef;
    FirebaseAuth mAuth;
    private NotificationManager mNotificationManager;

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
        provinsi = findViewById(R.id.provinsiCheckout);
        kota = findViewById(R.id.kotaCheckout);

        prosescekout = findViewById(R.id.prorsesCheckout);
        back = findViewById(R.id.backCheckout);
        back.setOnClickListener(v -> finish());

        prosescekout.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, PembelianFragment.class);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.setBigContentTitle("Pembelian Berhasil");
            bigText.setSummaryText("silahkan lakukan pembayaran");

            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentTitle("Pembelian Berhasil");
            mBuilder.setContentText("silahkan lakukan pembayaran");
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            mBuilder.setStyle(bigText);

            mNotificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }

            mNotificationManager.notify(0, mBuilder.build());
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
                        Lalamat = dataSnapshot.child("alamat").getValue().toString();

                        if (Lalamat == null) {
                            Toast.makeText(getApplicationContext(), "isi alamat terlebih dahulu di pengaturan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                            startActivity(intent);
                        } else {
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
