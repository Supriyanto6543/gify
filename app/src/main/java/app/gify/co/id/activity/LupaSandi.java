package app.gify.co.id.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.gify.co.id.R;

public class LupaSandi extends AppCompatActivity {

    EditText Email;
    String email;
    Button restPassword;

    ProgressDialog loadingBar;

    FirebaseAuth mAuth;
    DatabaseReference RootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lupa_password);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();;

        Email = findViewById(R.id.emailResetPassword);
        restPassword = findViewById(R.id.resetPassword);

        restPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar = new ProgressDialog(LupaSandi.this);
                loadingBar.setTitle("Mengirim Pesan");
                loadingBar.setMessage("Harap Tunggu");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                mAuth.sendPasswordResetEmail(Email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LupaSandi.this,
                                    "Silahkan cek email mu", Toast.LENGTH_SHORT).show();
                            SendUserToLogin();
                            loadingBar.dismiss();
                        } else {
                            Toast.makeText(LupaSandi.this,
                                    task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void SendUserToLogin() {
        Intent intent = new Intent(getApplication(), Login.class);
        startActivity(intent);
    }
}
