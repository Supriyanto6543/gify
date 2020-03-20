package app.gify.co.id.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

public class Pengaturan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText NamaDepan, NamaBelakang, NoHp, Email, GantiAlamat, editTextKecamatan, editTextKelurahan;
    LinearLayout changePicture;
    TextView Kelurahan, Kecamatan;
    String namadepan, namabelakang, noHp, email, currentUserID, nama, alamat, kelurahan, kecamatan, gAlamat, kota, provinsi;
    ImageView CheckList, ganti;
    ImageView Back;
    TextView gantiAlamat;
    ProgressDialog loadingBar;
    HintArrayAdapter hintAdapter, hintadapterku;

    View viewTerserah, viewKecamatan, viewKelurahan;

    Spinner KotaS, ProvinsiS;

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
        Kelurahan = findViewById(R.id.kelurahan);
        Kecamatan = findViewById(R.id.kecamatan);
        gantiAlamat = (TextView) findViewById(R.id.textviewAlamatPengaturan);
        GantiAlamat = findViewById(R.id.edittextAlamatPengaturan);
        ganti = findViewById(R.id.gantiAlamatPengaturan);
        viewTerserah = findViewById(R.id.viewTerserah);
        viewKecamatan = findViewById(R.id.Viewkecamatan);
        viewKelurahan = findViewById(R.id.Viewkelurahan);
        editTextKelurahan = findViewById(R.id.edittextkelurahan);
        editTextKecamatan = findViewById(R.id.edittextkecamatan);

        KotaS = findViewById(R.id.kota);
        ProvinsiS = findViewById(R.id.provinsi);

        hintAdapter = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintadapterku = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintAdapter.add("hint");
        hintadapterku.add("hint");

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        cobaOngkir1();
        cobaOngkir2();

        ganti.setOnClickListener(v -> {
            gantiAlamat.setVisibility(View.GONE);
            Kelurahan.setVisibility(View.GONE);
            Kecamatan.setVisibility(View.GONE);

            editTextKecamatan.setVisibility(View.VISIBLE);
            editTextKelurahan.setVisibility(View.VISIBLE);
            GantiAlamat.setVisibility(View.VISIBLE);

            viewKecamatan.setVisibility(View.GONE);
            viewKelurahan.setVisibility(View.GONE);
            viewTerserah.setVisibility(View.GONE);
        });

        CheckList.setOnClickListener(v -> {
            namadepan = NamaDepan.getText().toString().trim();
            namabelakang = NamaBelakang.getText().toString().trim();
            noHp = NoHp.getText().toString().trim();
            email = Email.getText().toString().trim();
            kelurahan = editTextKelurahan.getText().toString().trim();
            kecamatan = editTextKecamatan.getText().toString().trim();
            gAlamat = GantiAlamat.getText().toString().trim();
            kota = String.valueOf(KotaS.getSelectedItem());
            provinsi = String.valueOf(ProvinsiS.getSelectedItem());
            nama = namadepan + " " + namabelakang;
            alamat = gAlamat + "," + " " + kelurahan + "," + " " + kecamatan + "," + " " + kota + "," + " " + provinsi;
            loadingBar = new ProgressDialog(Pengaturan.this);
            loadingBar.setTitle("Mengubah Data...");
            loadingBar.setMessage("Harap Tunggu...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            if (namadepan.isEmpty() || namabelakang.isEmpty() || noHp.isEmpty() || email.isEmpty() || gAlamat.isEmpty() || kelurahan.isEmpty() || kecamatan.isEmpty() || kota.isEmpty() || provinsi.isEmpty()) {
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
                RootRef.child("Users").child(currentUserID).child("alamat").setValue(alamat)
                        .addOnCompleteListener(task -> {
                            editTextKelurahan.setText("");
                            editTextKecamatan.setText("");

                            editTextKelurahan.setVisibility(View.GONE);
                            editTextKecamatan.setVisibility(View.GONE);

                            viewKecamatan.setVisibility(View.VISIBLE);
                            viewKelurahan.setVisibility(View.VISIBLE);

                            loadingBar.dismiss();
                        });
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });

    }

    private void cobaOngkir2() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(UrlJson.PROVINCE, null, response -> {
            try {
                JSONObject jsonObject = response.getJSONObject("rajaongkir");
                JSONArray array = jsonObject.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    int province_id = object.getInt("province_id");
                    String province = object.getString("province");

                    hintadapterku.add(province);

                    ProvinsiS.setAdapter(hintadapterku);

                    ProvinsiS.setSelection(0, false);

                    ProvinsiS.setOnItemSelectedListener(Pengaturan.this);

                    Log.d("cobaProvince", "province_id: " + province_id + " " + "province: " + province);

                }
            } catch (JSONException e) {
                Log.d("err10", "Response: ");
                e.printStackTrace();
            }
        }, error -> Log.d("err2", "Error: " + error.getMessage()));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(objectRequest);
    }

    private void cobaOngkir1() {
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

                    hintAdapter.add(city_name);

                    KotaS.setAdapter(hintAdapter);

                    KotaS.setSelection(0, false);

                    KotaS.setOnItemSelectedListener(Pengaturan.this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class HintArrayAdapter<T> extends ArrayAdapter<T> {

        Context mContext;

        public HintArrayAdapter(Context context, int resource) {
            super(context, resource);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.spinnner, parent, false);
            TextView texview = (TextView) view.findViewById(android.R.id.text1);

            if(position == 0) {
                texview.setText("-- pilih --");
                texview.setTextColor(Color.parseColor("#b4b3b3"));
                texview.setHint(getItem(position).toString()); //"Hint to be displayed"
            } else {
                texview.setText(getItem(position).toString());
            }

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view;

            if(position == 0){
                view = inflater.inflate(R.layout.spinner_hint_list_item_layout, parent, false); // Hide first row
            } else {
                view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                TextView texview = (TextView) view.findViewById(android.R.id.text1);
                texview.setText(getItem(position).toString());
            }

            return view;
        }

    }


}
