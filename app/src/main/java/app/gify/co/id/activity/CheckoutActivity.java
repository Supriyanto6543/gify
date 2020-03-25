package app.gify.co.id.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import app.gify.co.id.Fragment.pembelian.PembelianFragment;
import app.gify.co.id.R;
import app.gify.co.id.baseurl.UrlJson;
//import app.gify.co.id.thirdparty.SenderAgent;

import static app.gify.co.id.baseurl.UrlJson.DELETEALLCART;

public class CheckoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button prosescekout, ongkir;
    ImageView back;

    EditText nama, hp, jalan, kelurahan, kecamatan, kota, provinsi, ucapan;
    String currentUserID, Lnama, LNohp, Lalamat;
    ImageView gantiAlamat;

    HintArrayAdapter hintArrayAdapter, hintArrayAdapterKu;

    Spinner Kota, Provinsi;

    DatabaseReference RootRef;
    FirebaseAuth mAuth;
    NotificationManager mNotificationManager;

    String idtetaporder, ttlorder, penerimaorder, alamatorder, kelurahanorder, kecamatanorder, kotaorder, provinsiorder, resiorder, statusorder, namabarangorder, ucapanorder, template, idharga;
    SharedPreferences preferences;
    NumberFormat format;
    Locale id;
    Random random;
    int lastNumber;
    Spanned templateConvert;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        RootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        // initialization
        nama = findViewById(R.id.nama);
        hp = findViewById(R.id.hp);
        jalan = findViewById(R.id.jalan);
        kelurahan = findViewById(R.id.kelurahan);
        kecamatan = findViewById(R.id.kecamatan);
        kota = findViewById(R.id.kota);
        provinsi =findViewById(R.id.provinsi);
        ucapan = findViewById(R.id.ucapan);

//        textViewCheckOutAlamat = findViewById(R.id.textviewAlamatCheckout);
//        NamaPenerima = findViewById(R.id.namaPenerimaCheckout);
//        NoPenerima = findViewById(R.id.noHPPenerimaCheckout);
//        alamatUbah = findViewById(R.id.edittextAlamatCheckout);
//        alamat = findViewById(R.id.textviewAlamatCheckout);
//        kelurahan2 = findViewById(R.id.kelurahanCheckoutText);
//        kecemetan = findViewById(R.id.kecamatanCheckoutText);
//        kecamatan = findViewById(R.id.kecamatanCheckout);
//        kelurahan = findViewById(R.id.kelurahanCheckout);
//        kota = findViewById(R.id.kotaCheckoutText);
//        provinsi = findViewById(R.id.provinsiCheckOutText);
//        gantiAlamat = findViewById(R.id.gantiAlamatCheckout);
//        Kota = findViewById(R.id.kotaCheckout);
//        Provinsi = findViewById(R.id.provinsiCheckout);

        hintArrayAdapter = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintArrayAdapterKu = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintArrayAdapter.add("hint");
        hintArrayAdapterKu.add("hint");

        prosescekout = findViewById(R.id.prorsesCheckout);
        ongkir = findViewById(R.id.ongkir);
        back = findViewById(R.id.backCheckout);
        back.setOnClickListener(v -> finish());

//        gantiAlamat.setOnClickListener(v -> {
//            alamatUbah.setVisibility(View.VISIBLE);
//            alamat.setVisibility(View.GONE);
//            kelurahan.setVisibility(View.VISIBLE);
//            kelurahan2.setVisibility(View.GONE);
//            kecamatan.setVisibility(View.VISIBLE);
//            kecemetan.setVisibility(View.GONE);
//            kota.setVisibility(View.GONE);
//            Kota.setVisibility(View.VISIBLE);
//            provinsi.setVisibility(View.GONE);
//            Provinsi.setVisibility(View.VISIBLE);
//        });

        preferences = PreferenceManager.getDefaultSharedPreferences(CheckoutActivity.this);

        //get value
        idtetaporder = preferences.getString("uid", "");

        ongkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, ActivityRajaOngkir.class);
                startActivity(intent);
            }
        });

        prosescekout.setOnClickListener(view -> {

            //get value form inner class
            penerimaorder = nama.getText().toString();
            alamatorder = jalan.getText().toString();
            kelurahanorder = kelurahan.getText().toString();
            kecamatanorder = kecamatan.getText().toString();
            kotaorder = kota.getText().toString();
            provinsiorder = provinsi.getText().toString();
            ucapanorder = ucapan.getText().toString();
            namabarangorder = getIntent().getStringExtra("title");

            //senderEmail();
//            PembelianFragment myFragments  = new PembelianFragment();
//            androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frameCheckout, myFragment);
//            fragmentTransaction.commit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendCart(getDateTime());
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");

                    NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                    bigText.setBigContentTitle("Pembelian Berhasil");
                    bigText.setSummaryText("silahkan lakukan pembayaran");

                    mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                    mBuilder.setContentTitle("Pembelian Berhasil");
                    mBuilder.setContentText("silahkan lakukan pembayaran");
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

                    mNotificationManager.notify(0, mBuilder.build());
                }
            }, 4000);
        });

        id = new Locale("id", "ID");

        format = NumberFormat.getCurrencyInstance(id);

        random = new Random();
        lastNumber = 0;

        for (int k = 0; k < 3; k++){
            lastNumber+=(random.nextInt(10)*Math.pow(10, k));
        }

        template = "<h2> Gify Transaction </h2> " +
                "<h3> Kamu baru saja melakukan pesanan dengan detail sebagai berikut </h3>"
                + "<p><b> Nama barang: </p></b>"
                + "<p><b> Harga barang" + "format.format(Double.valueOf(replaceNumberOfAmount(idharga, lastNumber)))" + ". Silahkan transfer dengan tiga digit terakhir yaitu :" + lastNumber + "</p></b>"
                + "<p><b> Jika sudah melakukan pembayaran, silahkan konfirmasi disini </p></b>"
                + "https://api.whatsapp.com/send?phone=082325328732&text=Confirmation%20Text"
                + "<h2>Salam, Gify Team</h2>";

        templateConvert = Html.fromHtml(template);

    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit(); // save the changes
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

    private void cobaOngkir2() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(UrlJson.PROVINCE, null, response -> {
            try {
                JSONObject jsonObject = response.getJSONObject("rajaongkir");
                JSONArray array = jsonObject.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {

                    JSONObject object = array.getJSONObject(i);
                    int province_id = object.getInt("province_id");
                    String province = object.getString("province");

                    hintArrayAdapter.add(province);

                    Provinsi.setAdapter(hintArrayAdapter);



                    Provinsi.setSelection(0, false);

                    Provinsi.setOnItemSelectedListener(CheckoutActivity.this);


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

                    hintArrayAdapterKu.add(city_name);

                    Kota.setAdapter(hintArrayAdapterKu);

                    Kota.setSelection(0, false);

                    Kota.setOnItemSelectedListener(CheckoutActivity.this);


                }

            } catch (JSONException e) {
                Log.d("On   ger", "OnResponse: ");
                e.printStackTrace();
            }
        }, error -> Log.d("error7", "Error: " + error.getMessage()));
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(objectRequest);
    }

    private void sendCart(String date){
        StringRequest request = new StringRequest(Request.Method.POST, UrlJson.ORDER, response -> {
            Log.d("bahrus", response + "");
            try {

                if (response.equals("bisa")){
                    deleteallcart();
                    PembelianFragment myFragment  = new PembelianFragment();
                    androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameCheckout, myFragment);
                    fragmentTransaction.commit();
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }, error -> {

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("id_tetap", idtetaporder);
                param.put("ttl", date);
                param.put("penerima", penerimaorder);
                param.put("alamat", alamatorder);
                param.put("kelurahan", kelurahanorder);
                param.put("kecamatan", kecamatanorder);
                param.put("kota", kotaorder);
                param.put("provinsi", provinsiorder);
                param.put("resi", "");
                param.put("status", String.valueOf(1));
                param.put("nama_barang", namabarangorder);
                param.put("ucapan", ucapanorder);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void deleteallcart(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DELETEALLCART+"?idtetap="+currentUserID, response -> {
        }, error ->  {

        });
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
        queue.add(stringRequest);
    }

    private String replaceNumberOfAmount(String original, int replace){
        return original.substring(0, original.length() - 3) + replace;
    }

//    private void senderEmail(){
//        SenderAgent senderAgent = new SenderAgent("gify.firebase@gmail.com", "Confirmation Transaction Gify", templateConvert, CheckoutActivity.this);
//        senderAgent.execute();
//    }
}
