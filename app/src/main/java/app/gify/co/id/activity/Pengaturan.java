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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app.gify.co.id.R;
import app.gify.co.id.baseurl.UrlJson;
import de.hdodenhof.circleimageview.CircleImageView;

public class Pengaturan extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText NamaDepan, NamaBelakang, NoHp, Email, GantiAlamat, editTextKecamatan, editTextKelurahan;
    LinearLayout changePicture, changeCover;
    TextView Kelurahan, Kecamatan, nama_depan, nama_belakang, No_hp, E_mail, textAlamat;
    String  cobaAgar, province, namadepan, namabelakang, noHp, email, currentUserID, nama, alamat, kelurahan, kecamatan, gAlamat2, gAlamat, kota, provinsi, Lemail, LID, namaUser, emailnama, idku, namanama,
    LNama, LEmail2, Lalamat, LNoHp, Ltanggal, fotoProfil, fotoCover;
    ImageView CheckList, ganti,profileImage, coverImage;
    ImageView Back;
    String province_id;
    int province_idku;
    TextView gantiAlamat;
    ProgressDialog loadingBar;
    HintArrayAdapter hintAdapter, hintadapterku;

    private static final int GALLERY_PHOTO = 1;
    private static final int GALLERY_COVER = 2;
    private static final int NOTIF = 3;
    private static final String NOTIFICATION_ID = "notif";
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 3;
    public static final String UPLOAD_URL = UrlJson.IMAGE;

    Bitmap Photo, Cover, decoded, decoded1;

    View viewTerserah, viewKecamatan, viewKelurahan;

    Spinner KotaS, ProvinsiS;

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
        gantiAlamat = findViewById(R.id.textviewAlamatPengaturan);
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);
        Glide.with(Pengaturan.this)
                .load(R.drawable.gifygif)
                .placeholder(R.drawable.gifygif)
                .centerCrop()
                .into(imageViewTarget);
        dialog.show();




        callMethos();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namaUser = sharedPreferences.getString("nama", "");
        Log.d("nama", namaUser);




        hintAdapter = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintadapterku = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintAdapter.add("hint");
        hintadapterku.add("hint");

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser().getUid());


        cobaOngkir1();
        cobaOngkir2();
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
            kota = KotaS.getSelectedItem().toString();
            provinsi = ProvinsiS.getSelectedItem().toString();
            if (Lalamat == null) {
                alamat = gAlamat + "," + " " + kelurahan + "," + " " + kecamatan + "," + " " + kota + "," + " " + provinsi;
            }
            else if (!gAlamat.isEmpty()) {
                alamat = gAlamat;
            }
            else if (!provinsi.isEmpty() && !kota.isEmpty() && !kecamatan.isEmpty() && !kelurahan.isEmpty()){
                alamat = gAlamat + "," + " " + kelurahan + "," + " " + kecamatan + "," + " " + kota + "," + " " + provinsi;
            }
            else {
                Toast.makeText(Pengaturan.this, "Isi yang kosong terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
            dialog.show();


            if (namadepan.isEmpty() || noHp.isEmpty() || email.isEmpty() || gAlamat.isEmpty()) {
                Toast.makeText(Pengaturan.this, "Isi yang kosong terlebih dahulu", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
                AkuGantengBanget();
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

            }
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
        KotaS = findViewById(R.id.kota);
        ProvinsiS = findViewById(R.id.provinsi);
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



    private void AkuGantengBanget(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlJson.IMAGE +"?id_tetap=" + LID, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Log.d("mmakan bang", response + "");
                try {
                    if (response.equals("bisa")) {
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
                if(getStringImage(Photo) == null){
                    params.put("foto", "photo");
                    params.put("cover", getStringImage(Cover));
                    params.put("id_tetap", LID);
                }else if (getStringImage(Cover) == null){
                    params.put("foto", getStringImage(Photo));
                    params.put("cover", "cover");
                    params.put("id_tetap", LID);
                }else {
                    params.put("foto", getStringImage(Photo));
                    params.put("cover", getStringImage(Cover));
                    params.put("id_tetap", LID);
                }
                return params;
            }
        };
        queue.add(stringRequest);
    }




    private void cobaOngkir2() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(UrlJson.PROVINCE, null, response -> {
            try {
                JSONObject jsonObject = response.getJSONObject("rajaongkir");
                JSONArray array = jsonObject.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    province_id = object.getString("province_id");
                    province = object.getString("province");

                    hintadapterku.add(province);

                    ProvinsiS.setAdapter(hintadapterku);

                    ProvinsiS.setSelection(0,  false);

                    ProvinsiS.setOnItemSelectedListener(Pengaturan.this);

                    cobaAgar = province + province_id;
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
                    province_idku = object.getInt("province_id");
                    String province = object.getString("province");
                    String type = object.getString("type");
                    String city_name = object.getString("city_name");
                    int postal_code = object.getInt("postal_code");

                    hintAdapter.add(city_name);

                    KotaS.setAdapter(hintAdapter);

                    KotaS.setSelection(0, false);

                    KotaS.setOnItemSelectedListener(Pengaturan.this);


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

    class HintArrayAdapter<T> extends ArrayAdapter<T> {

        Context mContext;

        public HintArrayAdapter(Context context, int resource) {
            super(context, resource);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.spinnner, parent, false);
            TextView texview = view.findViewById(android.R.id.text1);

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
                view = inflater.inflate(R.layout.spinner_text, parent, false);
                TextView texview = (TextView) view.findViewById(R.id.goku);
                texview.setText(getItem(position).toString());
            }

            return view;
        }

    }




}
