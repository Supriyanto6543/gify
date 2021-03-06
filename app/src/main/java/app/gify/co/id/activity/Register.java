package app.gify.co.id.activity;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.gify.co.id.R;

import static app.gify.co.id.baseurl.UrlJson.REGISTER;

public class Register extends AppCompatActivity {

    EditText Nama, Email, NoHp, Password;
    TextView TanggalLahir;
    String nama, email, noHp, password, currentUserID;
    Button Masuk;
    ImageView lihatPassword, hidePassword;
    Calendar date;
    StringBuilder selectedDate;

    FirebaseAuth mAuth;
    DatabaseReference RootRef;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();


        InitializeFields();

        TanggalLahir.setOnClickListener(v -> showDateTimePicker());

        lihatPassword.setOnClickListener(view -> {
            Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            lihatPassword.setVisibility(View.GONE);
            hidePassword.setVisibility(View.VISIBLE);
        });

        hidePassword.setOnClickListener(view -> {
            Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            lihatPassword.setVisibility(View.VISIBLE);
            hidePassword.setVisibility(View.GONE);
        });


        Masuk.setOnClickListener(v -> CreateNewAccount());
    }

    private void InitializeFields() {
        Masuk = findViewById(R.id.masukRegister);
        Nama = findViewById(R.id.namaRegister);
        Email = findViewById(R.id.emailLogin);
        NoHp = findViewById(R.id.noHPRegister);
        Password = findViewById(R.id.passwordRegister);
        TanggalLahir = findViewById(R.id.tanggalLahirRegister);
        hidePassword = findViewById(R.id.hidePasswordRegister);
        lihatPassword = findViewById(R.id.lihatPasswordRegister);
    }

    private void CreateNewAccount() {
        nama = Nama.getText().toString().trim();
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        noHp = NoHp.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Register.this, "Please enter a Email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(Register.this, "Please enter a Password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar = new ProgressDialog(Register.this);
            loadingBar.setTitle("Membuat akun baru...");
            loadingBar.setMessage("Harap tunggu, Kami sedang membuat akun barumu...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUserID = mAuth.getCurrentUser().getUid();
                                RootRef.child("Users").child(currentUserID).child("nama").setValue(nama);
                                RootRef.child("Users").child(currentUserID).child("email").setValue(email);
                                RootRef.child("Users").child(currentUserID).child("password").setValue(password);
                                RootRef.child("Users").child(currentUserID).child("tanggal").setValue(selectedDate.toString());
                                RootRef.child("Users").child(currentUserID).child("noHp").setValue(noHp);

                                FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                                if (task.isSuccessful()) {
                                                    String token = task.getResult().getToken();
                                                    Log.d("TokenPush", "token: " + token);
                                                    Toast.makeText(getApplicationContext(), "Token Generate", Toast.LENGTH_SHORT).show();
                                                    RootRef.child("Users").child(currentUserID).child("token").setValue(token);
                                                    RootRef.child("Users").child(currentUserID)
                                                            .addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    String LID = dataSnapshot.getKey();
                                                                    registertodatabase(LID, token, nama, selectedDate.toString(), email, noHp, password);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Token Generate failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                //SendUserToMainActivity();
                                Toast.makeText(Register.this, "Selamat! akunmu berhasil dibuat", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(Register.this, "Gagal Register" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void SendUserToMainActivity() {

    }

    private void showDateTimePicker() {
        final Calendar currentdate = Calendar.getInstance();
        date = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            selectedDate = new StringBuilder().append(day)
                    .append("-").append(month + 1).append("-").append(year)
                    .append("");
            SimpleDateFormat format = new SimpleDateFormat("EEEE, dd - MMMM - yyyy");
            SimpleDateFormat currentDateformat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date dates = currentDateformat.parse(String.valueOf(selectedDate));
                String tanggal = format.format(dates);
                TanggalLahir.setText(tanggal);
                TanggalLahir.setTextColor(Color.BLACK);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH), currentdate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void registertodatabase(final String id, final String t, final String n, final String ttl, final String em, final String no, final String p){
        StringRequest request = new StringRequest(Request.Method.POST, REGISTER, response -> {
            try {
                Log.d("registertodatabase", "registertodatabase: " + response);
                if (response.equals("sama")){
                    Toast.makeText(this, "Email Sudah digunakan", Toast.LENGTH_SHORT).show();
                    Email.setText("");
                }else if (response.equals("bisa")){
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    Log.d("akuss", "berhasil");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }catch (Exception e){
                e.getMessage();
                Toast.makeText(Register.this, "isi semua kolom", Toast.LENGTH_SHORT).show();
                Log.d("aku", e.getMessage() + "");
            }
        }, error -> {
            error.printStackTrace();
            error.getMessage();
            Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
            Log.d("aku", error.getMessage() + "");
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_tetap", id);
                params.put("fcm_token", t);
                params.put("email", em);
                params.put("nama", n);
                params.put("ttl", ttl);
                params.put("passwords", p);
                params.put("nohp", no);
                params.put("photo", "photo");
                params.put("cover_foto", "cover");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
