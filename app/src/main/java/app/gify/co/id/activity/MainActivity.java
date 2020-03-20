package app.gify.co.id.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.navigation.NavigationView;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import app.gify.co.id.R;
import app.gify.co.id.Fragment.favorit.FavoritFragment;
import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.Fragment.keluar.KeluarFragment;
import app.gify.co.id.Fragment.pembelian.PembelianFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;

    private AppBarConfiguration mAppBarConfiguration;
    ImageView navFragmentHome;
    private long bakPressedTime;
    TextView navigationheademail;
    TextView nama;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationheademail = headerLayout.findViewById(R.id.textViewNavigationDrawer);
        nama = headerLayout.findViewById(R.id.namaNavigationDrawer);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = sharedPreferences.getString("email", "");
        navigationheademail.setText(email);
        nama.setText(sharedPreferences.getString("nama", ""));
        loadFragment (new HomeFragment());
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