package app.gify.co.id.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import app.gify.co.id.R;

public class Pengaturan extends AppCompatActivity {

    EditText NamaDepan, NamaBelakang, NoHp, Email;
    LinearLayout changePicture;
    String namadepan, namabelakang, noHp, email, currentUserID, nama;
    ImageView CheckList, Back;
    ProgressDialog loadingBar;

    FirebaseAuth mAuth;
    DatabaseReference RootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pengaturan);

        NamaDepan = findViewById(R.id.namaDepanPengaturan);
        NamaBelakang = findViewById(R.id.namaBelakangPengaturan);
        NoHp = findViewById(R.id.noHpPengaturan);
        Email = findViewById(R.id.emailPengaturan);
        CheckList = findViewById(R.id.checklistPengaturan);
        Back = findViewById(R.id.backPengaturan);
        changePicture = findViewById(R.id.changePicturePengaturan);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = mAuth.getCurrentUser().getUid();

        CheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namadepan = NamaDepan.getText().toString().trim();
                namabelakang = NamaBelakang.getText().toString().trim();
                noHp = NoHp.getText().toString().trim();
                email = Email.getText().toString().trim();
                nama = namadepan + " " + namabelakang;
                loadingBar = new ProgressDialog(Pengaturan.this);
                loadingBar.setTitle("Mengubah Data...");
                loadingBar.setMessage("Harap Tunggu...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                if (namadepan.isEmpty() || namabelakang.isEmpty() || noHp.isEmpty() || email.isEmpty()) {
                    Toast.makeText(Pengaturan.this, "Isi yang kosong terlebih dahulu", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else {
                    RootRef.child("Users").child(currentUserID).child("nama").setValue(nama)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    NamaDepan.setText("");
                                    NamaBelakang.setText("");
                                    loadingBar.dismiss();
                                }
                            });
                    RootRef.child("Users").child(currentUserID).child("noHp").setValue(noHp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    NoHp.setText("");
                                    loadingBar.dismiss();
                                }
                            });
                    RootRef.child("Users").child(currentUserID).child("email").setValue(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Email.setText("");
                                    loadingBar.dismiss();
                                }
                            });
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
