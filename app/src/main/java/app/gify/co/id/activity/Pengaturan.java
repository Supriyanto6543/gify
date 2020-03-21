package app.gify.co.id.activity;

import android.app.Activity;
import android.app.Dialog;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.util.FileUtils;

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
    TextView Kelurahan, Kecamatan;
    String namadepan, namabelakang, noHp, email, currentUserID, nama, alamat, kelurahan, kecamatan, gAlamat, kota, provinsi, namaUser, emailnama, idku, namanama;
    ImageView CheckList, ganti,profileImage, coverImage;
    ImageView Back;
    TextView gantiAlamat;
    ProgressDialog loadingBar;
    HintArrayAdapter hintAdapter, hintadapterku;

    private static final int GALLERY_PHOTO = 1;
    private static final int GALLERY_COVER = 2;
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
    int bitmap_size = 60;
    int max_resolution_image = 800;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pengaturan);



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
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        cobaOngkir1();
        cobaOngkir2();
        cekprofile();

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

        changePicture.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY_PHOTO);
        });

        changeCover.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY_COVER);
        });

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

            AkuGantengBanget();
            dialog.show();


            if (namadepan.isEmpty() || namabelakang.isEmpty() || noHp.isEmpty() || email.isEmpty() || gAlamat.isEmpty() || kelurahan.isEmpty() || kecamatan.isEmpty() || kota.isEmpty() || provinsi.isEmpty()) {
                /*Toast.makeText(Pengaturan.this, "Isi yang kosong terlebih dahulu", Toast.LENGTH_SHORT).show();
                dialog.dismiss();*/
            }
            else {
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


            }
        });


        Back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });

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

    /*String currentPhotoPath = "";
    private File getImageFile() {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File file = null;
        try {
            file = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }*/

    /*private void showImage(Uri imageUri) {
        File file = new File(FileUtils.getPath(getApplicationContext(), imageUri));
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        profileImage.setImageBitmap(bitmap);
    }*/

    /*private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri).withMaxResultSize(16, 9).withAspectRatio(5f, 5f).start(Pengaturan.this);
    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        /*if(requestCode == GALLERY_PHOTO && resultCode == RESULT_OK) {
            Uri uri = Uri.parse(currentPhotoPath);
            openCropActivity(uri, uri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri uri = UCrop.getOutput(data);
            showImage(uri);
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri sourceUri = data.getData(); // 1
            File file = getImageFile(); // 2
            Uri destinationUri = Uri.fromFile(file);  // 3
            openCropActivity(sourceUri, destinationUri);  // 4
        }*/

        if (data != null){
            if (requestCode == GALLERY_PHOTO && resultCode == Activity.RESULT_OK) {
                profile = data.getData();
                try {
                    Photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), profile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profileImage.setImageBitmap(Photo);
            }
        }

        if (data != null){
            if (requestCode == GALLERY_COVER && resultCode == Activity.RESULT_OK) {
                cover = data.getData();
                try {
                    Cover = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), profile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                coverImage.setImageBitmap(Cover);
            }
        }
    }



    /*private File getImageFile() {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = null;
        String fullName = imageFileName + ".jpg";
        File fileku = new File (fullName);
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

    private void showImage(Uri imageUri) {
        File file = new File(FileUtils.getPath(getApplication(), imageUri));
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        coverImage.setImageBitmap(bitmap);
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(100, 100)
                .withAspectRatio(5f, 5f)
                .start(this);
    }*/


    public void cekprofile(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlJson.AMBIL_NAMA +"?nama=" + "firdaus", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("YukNgaji");
                    for (int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        idku = object.getString("id_tetap");
                        Log.d("namalagi", idku);
                        emailnama = object.getString("email");
                        dialog.dismiss();


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void AkuGantengBanget(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL +"?id_tetap = " + idku, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("bisa")){
                        Intent intentku = new Intent(getApplication(), MainActivity.class);
                        startActivity(intentku);
                        finish();
                    }
                }catch (Exception e){
                    e.getMessage();
                    Toast.makeText(Pengaturan.this, "isi semua kolom", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("photo", Photo.toString());
                params.put("cover_foto", Cover.toString());
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
                    int province_id = object.getInt("province_id");
                    String province = object.getString("province");

                    hintadapterku.add(province);

                    ProvinsiS.setAdapter(hintadapterku);

                    ProvinsiS.setSelection(0, false);

                    ProvinsiS.setOnItemSelectedListener(Pengaturan.this);


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
                view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                TextView texview = (TextView) view.findViewById(android.R.id.text1);
                texview.setText(getItem(position).toString());
            }

            return view;
        }

    }


}
