package app.gify.co.id.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app.gify.co.id.R;
import app.gify.co.id.baseurl.UrlJson;

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

        cobaOngkir();

        NamaDepan = findViewById(R.id.namaDepanPengaturan);
        NamaBelakang = findViewById(R.id.namaBelakangPengaturan);
        NoHp = findViewById(R.id.noHpPengaturan);
        Email = findViewById(R.id.emailPengaturan);
        CheckList = findViewById(R.id.checklistPengaturan);
        Back = findViewById(R.id.backPengaturan);
        changePicture = findViewById(R.id.changePicturePengaturan);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        CheckList.setOnClickListener(v -> {
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
                        .addOnCompleteListener(task -> {
                            NamaDepan.setText("");
                            NamaBelakang.setText("");
                            loadingBar.dismiss();
                        });
                RootRef.child("Users").child(currentUserID).child("noHp").setValue(noHp)
                        .addOnCompleteListener(task -> {
                            NoHp.setText("");
                            loadingBar.dismiss();
                        });
                RootRef.child("Users").child(currentUserID).child("email").setValue(email)
                        .addOnCompleteListener(task -> {
                            Email.setText("");
                            loadingBar.dismiss();
                        });
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Back.setOnClickListener(v -> {
            finish();
        });

    }

    private void cobaOngkir() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, UrlJson.CITY, null, response -> {
            try {
                JSONObject jsonObject = response.getJSONObject("rajaongkir");
                JSONArray array = jsonObject.getJSONArray("results");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int city_id = object.getInt("city_id");
                    int province_id = object.getInt("province_id");
                    String province = object.getString("province");
                    String type = object.getString("type");
                    String city_name = object.getString("city_name");
                    int postal_code = object.getInt("postal_code");

                    Log.d("cobaOngkir", "city_id: " + city_id + " " + "province_id: " + province_id + " " +
                            "province: " + province + " " + "type: " + type + " " + "city_name: " + city_name + " " +
                            "postal_code: " + postal_code);
                }

            } catch (JSONException e) {
                Log.d("On   ger", "OnResponse: ");
                e.printStackTrace();
            }
        }, error -> Log.d("error7", "Error: " + error.getMessage()));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(objectRequest);
    }
}
