package app.gify.co.id.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app.gify.co.id.R;
import app.gify.co.id.baseurl.UrlJson;
import app.gify.co.id.rajaongkir.adapterongkir.AdapterCheckCity;
import app.gify.co.id.rajaongkir.adapterongkir.AdapterProvinsi;
import app.gify.co.id.rajaongkir.apiongkir.ApiRaja;
import app.gify.co.id.rajaongkir.apiongkir.BaseApi;
import app.gify.co.id.rajaongkir.modelongkir.kota.ItemCity;
import app.gify.co.id.rajaongkir.modelongkir.kota.Result;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.Province;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.ResultOngkir;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class Pengaturan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private AdapterProvinsi adapter_province;
    private List<ResultOngkir> ListProvince = new ArrayList<ResultOngkir>();

    private AdapterCheckCity adapter_city;
    private List<Result> ListCity = new ArrayList<Result>();

    private ProgressDialog progressDialog;
    private Context context;
    private EditText searchList;

    private ListView mListView;

    private AlertDialog.Builder alert;
    private AlertDialog ad;

    EditText NamaDepan, NamaBelakang, NoHp,  GantiAlamat, editTextKecamatan, editTextKelurahan;
    LinearLayout changePicture, changeCover;
    TextView Kelurahan, Kecamatan, Email,nama_depan, nama_belakang, No_hp, E_mail, textAlamat;
    String  cobaAgar, province, namadepan, namabelakang, noHp, email, currentUserID, nama, alamat, kelurahan, kecamatan, gAlamat2, gAlamat, kota, provinsi, Lemail, LID, namaUser, emailnama, idku, namanama,
    LNama, LEmail2, Lalamat, LNoHp, Ltanggal, fotoProfil, fotoCover;
    ImageView CheckList, ganti,profileImage, coverImage;
    ImageView Back;
    String province_id;
    int province_idku;
    TextView gantiAlamat;
    ProgressDialog loadingBar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static final int GALLERY_PHOTO = 1;
    private static final int GALLERY_COVER = 2;
    private static final int NOTIF = 3;
    private static final String NOTIFICATION_ID = "notif";
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 3;
    public static final String UPLOAD_URL = UrlJson.IMAGE;

    Bitmap Photo, Cover, decoded, decoded1;

    View viewTerserah, viewKecamatan, viewKelurahan;

    EditText KotaS, ProvinsiS;

    FirebaseAuth mAuth;
    DatabaseReference RootRef;

    Dialog dialog;
    SharedPreferences sharedPreferences;
    Uri cover, profile;
    RequestQueue queue;
    private NotificationManager mNotificationManager;
    int bitmap_size = 60;
    int max_resolution_image = 800;
    String belomAdaAlamat = "Belum Memasukkan Alamat";
    LayoutInflater inflater;

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
        GantiAlamat = findViewById(R.id.edittextAlamatPengaturan);
        ganti = findViewById(R.id.gantiAlamatPengaturan);
        viewTerserah = findViewById(R.id.viewTerserah);
        textAlamat = findViewById(R.id.textviewAlamatPengaturan);
        viewKecamatan = findViewById(R.id.Viewkecamatan);
        viewKelurahan = findViewById(R.id.Viewkelurahan);
        editTextKelurahan = findViewById(R.id.edittextkelurahan);
        editTextKecamatan = findViewById(R.id.edittextkecamatan);
        profileImage = findViewById(R.id.profileimage);
        coverImage = findViewById(R.id.photo);
        changeCover = findViewById(R.id.changeCoverPengaturan);

        dialog  = new Dialog(Pengaturan.this);
        inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.loading, null);
        ImageView goku = layout.findViewById(R.id.custom_loading_imageView);
        goku.animate().rotationBy(3600).setDuration(10000).setInterpolator(new LinearInterpolator()).start();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(layout);
        dialog.show();


        callMethos();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        /*namaUser = sharedPreferences.getString("nama", "");*/
        /*editor = sharedPreferences.edit();

        editor.apply();*/

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser().getUid());

        ProvinsiS.setOnClickListener(v -> {
            popUpProvince(ProvinsiS, KotaS);
        });

        KotaS.setOnClickListener(v -> {
            try {
                if (ProvinsiS.getTag().equals("")) {
                    ProvinsiS.setError("Please choose Your Province");
                } else {
                    popUpCity(KotaS, ProvinsiS);
                }
            } catch (NullPointerException e) {
                ProvinsiS.setError("Please choose Your Province");
            }
        });

        cekprofile();

        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LID = dataSnapshot.getKey();
                Lemail = dataSnapshot.child("email").getValue().toString();
                String LNoHP = dataSnapshot.child("noHp").getValue().toString();
                String nama = dataSnapshot.child("nama").getValue().toString();
                if (dataSnapshot.child("alamat").exists()){
                    Lalamat = dataSnapshot.child("alamat").getValue().toString();
                }
                else {
                    Lalamat = null;
                }
                Log.d("cobaL", "email: " + Lemail + " " + "LID: " + LID);
                Email.setText(Lemail);
                NoHp.setText(LNoHP);
                NamaDepan.setText(nama);
                GantiAlamat.setText(Lalamat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changePicture.setOnClickListener(v -> {
            Intent intent = new Intent();

            intent.setType("image/*");

            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), GALLERY_PHOTO);
        });

        changeCover.setOnClickListener(v -> {
            Intent intent = new Intent();

            intent.setType("image/*");

            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), GALLERY_COVER);
        });

        ganti.setOnClickListener(v -> {
            Kelurahan.setVisibility(View.GONE);
            Kecamatan.setVisibility(View.GONE);

            editTextKecamatan.setVisibility(View.VISIBLE);
            editTextKelurahan.setVisibility(View.VISIBLE);
        });

        CheckList.setOnClickListener(v -> {
            namadepan = NamaDepan.getText().toString().trim();
            namabelakang = NamaBelakang.getText().toString().trim();
            nama = namadepan + " " + namabelakang;
            noHp = NoHp.getText().toString().trim();
            email = Email.getText().toString().trim();
            kelurahan = editTextKelurahan.getText().toString().trim();
            kecamatan = editTextKecamatan.getText().toString().trim();
            gAlamat = GantiAlamat.getText().toString().trim();
            kota = KotaS.getText().toString();
            provinsi = ProvinsiS.getText().toString();
            editor = sharedPreferences.edit();
            editor.remove("nama");
            editor.remove("email");
            editor.putString("nama", nama);
            editor.putString("email", email);
            /*editor.putString("ln", ln);*/
            editor.apply();


            dialog.show();
            AkuGantengBanget(email,noHp,namadepan, namabelakang,gAlamat + ", " + kelurahan + ", " + kecamatan + ", " + kota + ", " + provinsi);
            RootRef.child("Users").child(currentUserID).child("nama").setValue(nama)
                    .addOnCompleteListener(task -> {
                        NamaDepan.setText("");
                        NamaBelakang.setText("");
                        dialog.dismiss();
                    });
            RootRef.child("Users").child(currentUserID).child("noHp").setValue(noHp)
                    .addOnCompleteListener(task -> {
                        NoHp.setText("");
                        dialog.dismiss();
                    });
            RootRef.child("Users").child(currentUserID).child("email").setValue(email)
                    .addOnCompleteListener(task -> {
                        Email.setText("");
                        dialog.dismiss();
                    });
            RootRef.child("Users").child(currentUserID).child("alamat").setValue(alamat)
                    .addOnCompleteListener(task -> {
                        editTextKelurahan.setText("");
                        editTextKecamatan.setText("");

                        editTextKelurahan.setVisibility(View.GONE);
                        editTextKecamatan.setVisibility(View.GONE);

                        viewKecamatan.setVisibility(View.VISIBLE);
                        viewKelurahan.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                    });

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);



        });


        Back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });

        lemparMysql();

    }

    private void callMethos(){
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
        profileImage = findViewById(R.id.profileimage);
        coverImage = findViewById(R.id.photo);
        changeCover = findViewById(R.id.changeCoverPengaturan);
        queue = Volley.newRequestQueue(getApplication());
        KotaS = findViewById(R.id.kotaPengaturan);
        ProvinsiS = findViewById(R.id.provinsiPengaturan);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PHOTO && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            profile = data.getData();
            try {
                Photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), profile);
                profileImage.setImageBitmap(Photo);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == GALLERY_COVER && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            cover = data.getData();
            try {
                Cover = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), cover);
                coverImage.setImageBitmap(Cover);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void lemparMysql(){
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LID = dataSnapshot.getKey();
                        LNama = dataSnapshot.child("nama").getValue().toString();
                        LNoHp = dataSnapshot.child("noHp").getValue().toString();
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void cekprofile(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlJson.AMBIL_NAMA + "?id_tetap=" + LID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("GIFY");
                    for (int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        fotoProfil = object.getString("photo");
                        fotoCover = object.getString("cover_foto");
                        emailnama = object.getString("email");
                        dialog.dismiss();


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void AkuGantengBanget(String e, String no, String n, String ln, String a){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlJson.IMAGE +"?id_tetap=" + LID, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Log.d("mmakan bang", response + "");
                try {
                    if (response.equals("bisa")) {
                        Log.d("Test", "onResponse: " + ln + n);

                        Intent intentku = new Intent(getApplication(), MainActivity.class);
                        startActivity(intentku);
                        finish();

                    } else if (response.equals("gagal")) {
                        Toast.makeText(getApplicationContext(), "Gagal Coba Lagi", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(Pengaturan.this, "isi semua kolom", Toast.LENGTH_SHORT).show();
                }

            }

        }, error -> {


        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (getStringImage(Cover) == null){
                    params.put("foto", getStringImage(Photo));
                    params.put("cover", "cover");
                    params.put("email", e);
                    params.put("nama", n);
                    params.put("last_name", ln);
                    params.put("nohp", no);
                    params.put("alamat", a);
                    params.put("id_tetap", LID);
                }else if (getStringImage(Photo) == null){
                    params.put("foto", "foto");
                    params.put("cover", getStringImage(Cover));
                    params.put("email", e);
                    params.put("nama", n);
                    params.put("last_name", ln);
                    params.put("nohp", no);
                    params.put("alamat", a);
                    params.put("id_tetap", LID);
                }else {
                    params.put("foto", getStringImage(Photo));
                    params.put("cover", getStringImage(Cover));
                    params.put("email", e);
                    params.put("nama", n);
                    params.put("last_name", ln);
                    params.put("nohp", no);
                    params.put("alamat", a);
                    params.put("id_tetap", LID);
                }

                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void popUpProvince(final TextView provinsi, final TextView kota ) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.rajaongkir_popup_search, null);

        alert = new AlertDialog.Builder(this);
        alert.setTitle("List ListProvince");
        alert.setMessage("select your province");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new Pengaturan.MyTextWatcherProvince(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListProvince.clear();
        adapter_province = new AdapterProvinsi(this, ListProvince);
        mListView.setClickable(true);

        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Object o = mListView.getItemAtPosition(i);
            ResultOngkir cn = (ResultOngkir) o;

            provinsi.setError(null);
            provinsi.setText(cn.getProvince());
            provinsi.setTag(cn.getProvinceId());

            kota.setText("");
            kota.setTag("");

            ad.dismiss();
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        getProvince();

    }

    public void popUpCity(final TextView kota, final TextView provinsi) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.rajaongkir_popup_search, null);

        alert = new AlertDialog.Builder(this);
        alert.setTitle("List City");
        alert.setMessage("select your city");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new Pengaturan.MyTextWatcherCity(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListCity.clear();
        adapter_city = new AdapterCheckCity(this, ListCity);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                Result cn = (Result) o;

                kota.setError(null);
                kota.setText(cn.getCityName());
                kota.setTag(cn.getCityId());

                ad.dismiss();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        getCity(provinsi.getTag().toString());

    }

    private class MyTextWatcherProvince implements TextWatcher {

        private View view;

        private MyTextWatcherProvince(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchItem:
                    adapter_province.filter(editable.toString());
                    break;
            }
        }
    }

    private class MyTextWatcherCity implements TextWatcher {

        private View view;

        private MyTextWatcherCity(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchItem:
                    adapter_city.filter(editable.toString());
                    break;
            }
        }
    }

    public void getProvince() {
        ApiRaja api = BaseApi.callJson();
        Call<Province> provinceCall = api.getProvince();
        provinceCall.enqueue(new Callback<Province>() {
            @Override
            public void onResponse(Call<Province> call, retrofit2.Response<Province> response) {

                progressDialog.dismiss();
                Log.v("wow", "json : " + new Gson().toJson(response));

                if (response.isSuccessful()) {

                    int count_data = response.body().getSourceOngkir().getResults().size();
                    for (int a = 0; a <= count_data - 1; a++) {
                        ResultOngkir itemProvince = new ResultOngkir(
                                response.body().getSourceOngkir().getResults().get(a).getProvinceId(),
                                response.body().getSourceOngkir().getResults().get(a).getProvince()
                        );

                        ListProvince.add(itemProvince);
                        mListView.setAdapter(adapter_province);
                    }

                    adapter_province.setList(ListProvince);
                    adapter_province.filter("");

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Province> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getCity(String id_province) {

        ApiRaja apiRaja = BaseApi.callJson();
        Call<ItemCity> itemCityCall = apiRaja.getCity(id_province);
        itemCityCall.enqueue(new Callback<ItemCity>() {
            @Override
            public void onResponse(Call<ItemCity> call, retrofit2.Response<ItemCity> response) {

                progressDialog.dismiss();
                Log.v("wow", "json : " + new Gson().toJson(response));

                if (response.isSuccessful()) {

                    int count_data = response.body().getRajaongkir().getResults().size();
                    for (int a = 0; a <= count_data - 1; a++) {
                        Result itemProvince = new Result(
                                response.body().getRajaongkir().getResults().get(a).getCityId(),
                                response.body().getRajaongkir().getResults().get(a).getProvinceId(),
                                response.body().getRajaongkir().getResults().get(a).getProvince(),
                                response.body().getRajaongkir().getResults().get(a).getType(),
                                response.body().getRajaongkir().getResults().get(a).getCityName(),
                                response.body().getRajaongkir().getResults().get(a).getPostalCode()
                        );

                        ListCity.add(itemProvince);
                        mListView.setAdapter(adapter_city);
                    }

                    adapter_city.setList(ListCity);
                    adapter_city.filter("");

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCity> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
