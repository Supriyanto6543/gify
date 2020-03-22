package app.gify.co.id.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import app.gify.co.id.R;
import app.gify.co.id.Fragment.favorit.FavoritFragment;
import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.Fragment.keluar.KeluarFragment;
import app.gify.co.id.Fragment.pembelian.PembelianFragment;
import app.gify.co.id.baseurl.UrlJson;
import app.gify.co.id.sessions.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;

    private AppBarConfiguration mAppBarConfiguration;
    ImageView navFragmentHome;
    private long bakPressedTime;
    CircleImageView profile;
    LinearLayout cover;
    String Lemail, LID, photoprofile, coverku, LNama, LEmail2, Lalamat, LNoHp, currentUserID, Ltanggal;
    TextView navigationheademail;
    TextView nama;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    DatabaseReference RootRef;
    View headerLayout;
    Dialog dialog;
    SessionManager sessionManager;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog  = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);
        Glide.with(MainActivity.this)
                .load(R.drawable.gifygif)
                .placeholder(R.drawable.gifygif)
                .centerCrop()
                .into(imageViewTarget);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.removeHeaderView(navigationView.getHeaderView(0));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationheademail = headerLayout.findViewById(R.id.textViewNavigationDrawer);
        nama = headerLayout.findViewById(R.id.namaNavigationDrawer);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        profile = headerLayout.findViewById(R.id.imageViewNavigationDrawer);
        cover = headerLayout.findViewById(R.id.backgroundHeader);
        lemparMysql();
        cekprofile();
        String email = sharedPreferences.getString("email", "");
        Log.d("easd", "onCreate: " + email + " s " + sharedPreferences.getString("nama", ""));
        navigationheademail.setText(email);
        nama.setText(sharedPreferences.getString("nama", ""));
        loadFragment (new HomeFragment());

    }


    private void lemparMysql(){
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LID = dataSnapshot.getKey();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void cekprofile(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlJson.AMBIL_NAMA +"?id_tetap=" + LID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("YukNgaji");
                    for (int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        coverku = object.getString("cover_foto");
                        photoprofile = object.getString("photo");
                        dialog.dismiss();
                        Log.d("gambar 1", coverku + "");
                        byte[] imageBytes = Base64.decode(coverku, Base64.DEFAULT);
                        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        Drawable mDrawable = new BitmapDrawable(getResources(), decodedImage);
                        cover.setBackground(mDrawable);
                        byte[] imageBytesku = Base64.decode(photoprofile, Base64.DEFAULT);
                        Bitmap decodedImageku = BitmapFactory.decodeByteArray(imageBytesku, 0, imageBytesku.length);
                        profile.setImageBitmap(decodedImageku);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();

            return true;
        }

        return false;
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


    @SuppressLint("WrongConstant")
    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(Gravity.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int userId = item.getItemId();
        Fragment f = null;
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.closeDrawers();

        switch (userId) {

            case R.id.nav_belikado:
                f = new HomeFragment();
                break;

            case R.id.nav_favorit:
                f = new FavoritFragment();
                break;

            case R.id.nav_pembelian:
                f = new PembelianFragment();
                break;

            case R.id.nav_pengaturan:
                Intent intent = new Intent(getApplicationContext(), Pengaturan.class);
                startActivity(intent);
                break;

            case R.id.nav_keluar:
                f = new KeluarFragment();
                break;


        }
        return loadFragment(f);
    }


}