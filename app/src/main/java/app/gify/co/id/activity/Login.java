package app.gify.co.id.activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.os.PersistableBundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.gify.co.id.R;
import app.gify.co.id.sessions.SessionManager;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText Email, Password;
    TextView lupaSandi;
    String email, password, userID, Uemail;
    Button Masuk, Daftar;
    ImageView lihatPassword, hidePassword;
    ProgressDialog progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private long bakPressedTime;
    SessionManager sessionManager;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private NotificationManager mNotificationManager;
    private Dialog dialog;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.emailLogin);
        Password = findViewById(R.id.passwordLogin);
        Masuk = findViewById(R.id.masuk);
        Daftar = findViewById(R.id.daftar);
        lupaSandi = findViewById(R.id.lupaSandi);
        lihatPassword = findViewById(R.id.lihatPassword);
        hidePassword = findViewById(R.id.hidePassword);

        lihatPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                lihatPassword.setVisibility(View.GONE);
                hidePassword.setVisibility(View.VISIBLE);
            }
        });

        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                lihatPassword.setVisibility(View.VISIBLE);
                hidePassword.setVisibility(View.GONE);
            }
        });


        Daftar.setOnClickListener(v -> {
            Intent registerIntent = new Intent(getApplication(), Register.class);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            startActivity(registerIntent);
        });

        sessionManager = new SessionManager(Login.this);

        if (sessionManager.isLogged()) {
            Intent mainIntent = new Intent(getApplication(), MainActivity.class);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            String emailKu = sharedPreferences.getString("email", "");
            String uidku = sharedPreferences.getString("uid", "");
            Log.d("emailLoginSession", emailKu + " s " + uidku);
            startActivity(mainIntent);
            finish();
        }

        lupaSandi.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), LupaSandi.class);
            startActivity(intent);
        });

        Masuk.setOnClickListener(v -> {

            email = Email.getText().toString().trim();
            password = Password.getText().toString().trim();
            dialog  = new Dialog(Login.this);
            inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.loading, null);
            ImageView goku = layout.findViewById(R.id.custom_loading_imageView);
            goku.animate().rotationBy(360).setDuration(3000).setInterpolator(new LinearInterpolator()).start();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
            dialog.setContentView(layout);
            dialog.show();

            //wajib di tambahkan untuk menghindari null
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(Login.this, "Isi yang kosong terlebih dahulu", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Masuk.setVisibility(View.VISIBLE);
            }else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        Toast.makeText(Login.this, "Selamat Datang!", Toast.LENGTH_SHORT).show();
                                        SendUserToMainActivity();
                                    } else {
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_002");

                                            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                                            bigText.setBigContentTitle("Verivikasi Email");
                                            bigText.setSummaryText("buka gmail dan verivikasi aku kamu");

                                            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                                            mBuilder.setContentTitle("Verivikasi Email");
                                            mBuilder.setContentText("buka gmail dan verivikasi aku kamu");
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

                                            mNotificationManager.notify(1, mBuilder.build());
                                            Toast.makeText(Login.this, "Silahkan cek email anda", Toast.LENGTH_SHORT).show();
                                        });
                                    }
                                Masuk.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(Login.this, "Email / Sandi salah " , Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Masuk.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });
    }

    private void SendUserToMainActivity() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDb = mDatabase.getReference();
        String user = mAuth.getCurrentUser().getUid();

        //First Approach
        mDb.child("Users").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userID = String.valueOf(dataSnapshot.child("nama depan").getValue());
                Uemail = String.valueOf(dataSnapshot.child("email").getValue());
                mDb.child("Users").child(user).child("password").setValue(password);
                Log.d("namaku", "onDataChange: " + userID);
                Intent mainIntent = new Intent(getApplication(), MainActivity.class);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                sessionManager.checkLogin(true);
                editor = sharedPreferences.edit();
                editor.putString("email", Uemail);
                editor.putString("nama", userID);
                editor.putString("uid", user);
                editor.apply();
                startActivity(mainIntent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (bakPressedTime + 2000 > System.currentTimeMillis()){
            tekanLagi();
            /*super.onBackPressed();*/
            return;
        }else {
            Toast.makeText(getApplicationContext(), "Tekan Sekali Lagi Untuk Keluar",Toast.LENGTH_SHORT).show();
        }

        bakPressedTime = System.currentTimeMillis();
    }

    private void tekanLagi(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
