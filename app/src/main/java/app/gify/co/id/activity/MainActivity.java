package app.gify.co.id.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import app.gify.co.id.R;
import app.gify.co.id.Fragment.favorit.FavoritFragment;
import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.Fragment.keluar.KeluarFragment;
import app.gify.co.id.Fragment.pengaturan.PengaturanFragment;
import app.gify.co.id.Fragment.pembelian.PembelianFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;

    private AppBarConfiguration mAppBarConfiguration;
    private long bakPressedTime;
    TextView navigationheademail;
    TextView nama;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int userId = item.getItemId();
        Fragment f = null;
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();

        switch (userId) {

            case R.id.nav_belikado:
                f = new HomeFragment();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_favorit:
                f = new FavoritFragment();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_pembelian:
                f = new PembelianFragment();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_pengaturan:
                f = new PengaturanFragment();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_keluar:
                f = new KeluarFragment();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;


        }
        return loadFragment(f);
    }


}