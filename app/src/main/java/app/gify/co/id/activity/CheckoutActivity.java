package app.gify.co.id.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import app.gify.co.id.Fragment.pembelian.PembelianFragment;
import app.gify.co.id.R;
import app.gify.co.id.baseurl.UrlJson;
import app.gify.co.id.rajaongkir.adapterongkir.AdapterCheckCity;
import app.gify.co.id.rajaongkir.adapterongkir.AdapterProvinsi;
import app.gify.co.id.rajaongkir.apiongkir.ApiRaja;
import app.gify.co.id.rajaongkir.apiongkir.BaseApi;
import app.gify.co.id.rajaongkir.modelongkir.biaya.ItemCost;
import app.gify.co.id.rajaongkir.modelongkir.kota.ItemCity;
import app.gify.co.id.rajaongkir.modelongkir.kota.Result;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.Province;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.ResultOngkir;
import retrofit2.Call;
import retrofit2.Callback;
//import app.gify.co.id.thirdparty.SenderAgent;

import static app.gify.co.id.baseurl.UrlJson.DELETEALLCART;
import static app.gify.co.id.baseurl.UrlJson.ORDER;

public class CheckoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button prosescekout;
    ImageView back;

    private AdapterProvinsi adapter_province;
    private List<ResultOngkir> ListProvince = new ArrayList<ResultOngkir>();

    private AdapterCheckCity adapter_city;
    private List<Result> ListCity = new ArrayList<Result>();

    private ProgressDialog progressDialog;
    private Context context;
    private EditText searchList;

    private ListView mListView;

    EditText nama, hp, jalan, kelurahan, kecamatan,  ucapan;
    String currentUserID, Lnama, LNohp, Lalamat;
    ImageView gantiAlamat;
    TextView kota, provinsi;

    private AlertDialog.Builder alert;
    private AlertDialog ad;

    HintArrayAdapter hintArrayAdapter, hintArrayAdapterKu;

    Spinner Kota, Provinsi;

    DatabaseReference RootRef;
    FirebaseAuth mAuth;
    NotificationManager mNotificationManager;

    String idtetaporder,hahaha, ttlorder,hpku, penerimaorder, alamatorder, kelurahanorder, kecamatanorder, kotaorder, provinsiorder, resiorder, statusorder, namabarangorder, ucapanorder, template, idharga,qtyku;
    SharedPreferences preferences;
    NumberFormat format;
    Locale id;
    Random random;
    int lastNumber, quantity;
    String berat;
    Spanned templateConvert;
    Dialog dialog;
    String ongkir;

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
        provinsi = findViewById(R.id.provinsi);
        ucapan = findViewById(R.id.ucapan);
        berat = getIntent().getStringExtra("berat");

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
        idharga = getIntent().getStringExtra("idharga");
        namabarangorder = getIntent().getStringExtra("name");
        Log.d("kotainantes", "onCreate: " + namabarangorder);
        qtyku = getIntent().getStringExtra("qtyku");
        berat = getIntent().getStringExtra("berat");
        Log.d("cekstatus", idharga + namabarangorder + " s " + qtyku);

        provinsi.setOnClickListener(v -> {
            popUpProvince(provinsi, kota);
        });

        kota.setOnClickListener(v -> {
            try {
                if (provinsi.getTag().equals("")) {
                    provinsi.setError("Please choose your form province");
                } else {
                    popUpCity(kota, provinsi);
                }

            } catch (NullPointerException e) {
                provinsi.setError("Please choose your form province");
            }
        });

        /*ongkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, ActivityRajaOngkir.class);
                startActivity(intent);
            }
        });*/

        prosescekout.setOnClickListener(view -> {

            if (nama.getText().toString().isEmpty() || hp.getText().toString().isEmpty() || jalan.getText().toString().isEmpty() || kelurahan.getText().toString().isEmpty() || kota.getText().toString().isEmpty() || provinsi.getText().toString().isEmpty() || ucapan.getText().toString().isEmpty()){
                Toast.makeText(CheckoutActivity.this, "isi semua kolom yang kosong", Toast.LENGTH_SHORT).show();
            }else {
                String Kota = kota.getText().toString();
                String Provinsi = provinsi.getText().toString();

                if (Kota.equals("")) {
                    kota.setError("Please input your City");
                } else if (Provinsi.equals("")) {
                    provinsi.setError("Please input your Province");
                } else {
                    getCoast(
                            "23",
                            kota.getTag().toString(),
                            berat,
                            "jne"
                    );
                    penerimaorder = nama.getText().toString();
                    alamatorder = jalan.getText().toString();
                    kelurahanorder = kelurahan.getText().toString();
                    kecamatanorder = kecamatan.getText().toString();
                    kotaorder = kota.getText().toString();
                    provinsiorder = provinsi.getText().toString();
                    ucapanorder = ucapan.getText().toString();
                    hpku = hp.getText().toString();
                    new SenderOrder("gify.firebase@gmail.com", "Confirmation Transaction Gify", templateConvert, CheckoutActivity.this,idtetaporder,getDateTime(), penerimaorder,hpku, alamatorder, kelurahanorder, kecamatanorder, kotaorder, provinsiorder, namabarangorder,qtyku, berat, ucapanorder, Integer.parseInt(idharga)  + lastNumber ).execute();
                }



            }

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
                + "<p><b> Nama barang: " + namabarangorder + "</p></b>"
                + "<p><b> Harga barang: " + format.format(Double.valueOf(replaceNumberOfAmount(idharga, lastNumber))) + ". Silahkan transfer dengan tiga digit terakhir yaitu :" + lastNumber + "</p></b>"
                + "<p><b> Jika sudah melakukan pembayaran, silahkan konfirmasi disini </p></b>"
                + "https://api.whatsapp.com/send?phone=082325328732&text=Confirmation%20Text"
                + "<h2>Salam, Gify Team</h2>";

        templateConvert = Html.fromHtml(template);

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
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
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
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
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
        queue.add(objectRequest);
    }

    public void sendCart(Context context, String idtetap, String date, String penerima,String noHp, String alamat, String kelurahan, String kecamatan, String kota, String provinsi, String namabarang,String jumlah, String berat, String ucapan, int harga){
        StringRequest request = new StringRequest(Request.Method.POST, ORDER, response -> {
            Log.d("bahrus", response + "");
            try {
                Log.d("bahruss", response + "");
                if (response.equals("bisa")){
                    Log.d("jumajumlah", "sendCart: " + jumlah);
                    deleteallcart();
                    context.startActivity(new Intent(context, MainActivity.class));
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("erorcas", "sendCart: " + e.getMessage());
            }
        }, error -> {
            Log.d("erorca", "sendCart: " + error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("id_tetap", idtetap);
                param.put("ttl", date);
                param.put("penerima", penerima);
                param.put("nohp", noHp);
                param.put("alamat", alamat);
                param.put("kelurahan", kelurahan);
                param.put("kecamatan", kecamatan);
                param.put("kota", kota);
                param.put("provinsi", provinsi);
                param.put("resi", "-");
                param.put("status", String.valueOf(1));
                param.put("nama_barang", namabarang);
                param.put("jumlah", jumlah);
                param.put("berat", berat);
                param.put("ucapan", ucapan);
                param.put("harga", String.valueOf(harga));
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void deleteallcart(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DELETEALLCART+"?idtetap="+currentUserID, response -> {
            if (response.equals("bisa")){
                Toast.makeText(CheckoutActivity.this, "cart kosong", Toast.LENGTH_SHORT).show();
            }
        }, error ->  {

        });
        RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
        queue.add(stringRequest);
    }

    private String replaceNumberOfAmount(String original, int replace){
        return original.substring(0, original.length() - 3) + replace;
    }

    public void pushNotify(Context context){
        Intent intent = new Intent(this, PembelianFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle("Pembelian Berhasil");
        bigText.setSummaryText("tekan notifikasi ini untuk melanjutkan, dan silahkan lakukan pembayaran dengan invoice yang kami kirim ke emailmu");

        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Pembelian Berhasil");
        mBuilder.setContentText("tekan notifikasi ini untuk melanjutkan, dan silahkan lakukan pembayaran dengan invoice yang kami kirim ke emailmu");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setStyle(bigText);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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

    private class SenderOrder extends AsyncTask<Void, Void, Void>{
        private String mail, idtetap, date, penerima,nohp, alamat, kelurahan, kecamatan, kota, provinsi, namabarang,jumlah,berat, ucapan, jumlahbrng;
        private String subject;
        private Spanned message;
        private int harga;
        Dialog dialog;

        private Context context;
//        private Session session;

        private ProgressDialog progressDialog;

        public SenderOrder(String mail, String subject, Spanned message, Context context, String idtetap, String date, String penerima,String nohp, String alamat, String kelurahan, String kecamatan, String kota, String provinsi, String namabarang,String jumlah,String berat, String ucapan, int harga) {
            this.mail = mail;
            this.subject = subject;
            this.message = message;
            this.context = context;
            this.idtetap = idtetap;
            this.date = date;
            this.penerima = penerima;
            this.nohp = nohp;
            this.alamat = alamat;
            this.kelurahan = kelurahan;
            this.kecamatan = kecamatan;
            this.kota = kota;
            this.provinsi = provinsi;
            this.namabarang = namabarang;
            this.jumlah = jumlah;
            this.berat = berat;
            this.ucapan = ucapan;
            this.harga = harga;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog  = new Dialog(context);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.loading, null);
            ImageView goku = layout.findViewById(R.id.custom_loading_imageView);
            goku.animate().rotationBy(3600).setDuration(10000).setInterpolator(new LinearInterpolator()).start();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(false);
            dialog.setContentView(layout);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Properties properties = new Properties();

            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");

//            session = Session.getDefaultInstance(properties, new Authenticator() {
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication("gify.firebase@gmail.com", "Gifyapp01");
//                }
//            });
//
//            try{
//                MimeMessage mimeMessage = new MimeMessage(session);
//
//                mimeMessage.setFrom(new InternetAddress("gify.firebase@gmail.com"));
//                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("supriyanto150@gmail.com"));
//                mimeMessage.setSubject(subject);
//                mimeMessage.setText(String.valueOf(message));
//                Transport.send(mimeMessage);
//            }catch (MessagingException m){
//                m.printStackTrace();
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Log.d("dismisskupost", "onPostExecute: " + berat + " s " + jumlah);
            context.startActivity(new Intent(context, MainActivity.class));
            new CheckoutActivity().sendCart(CheckoutActivity.this, idtetap, date, penerima,nohp, alamat, kelurahan, kecamatan, kota, provinsi, namabarang, jumlah, berat, ucapan, harga);
            new CheckoutActivity().pushNotify(context);
        }
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
        searchList.addTextChangedListener(new CheckoutActivity.MyTextWatcherProvince(searchList));
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
        searchList.addTextChangedListener(new CheckoutActivity.MyTextWatcherCity(searchList));
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
                    Toast.makeText(CheckoutActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Province> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CheckoutActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CheckoutActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCity> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CheckoutActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getCoast(String origin,
                         String destination,
                         String weight,
                         String courier) {
        ApiRaja apiRaja = BaseApi.callJson();
        Call<ItemCost> call = apiRaja.getCost(
                "cfc2d1eac754c1b41f383bbfa6fe45b6",
                origin,
                destination,
                weight,
                courier
        );

        call.enqueue(new Callback<ItemCost>() {
            @Override
            public void onResponse(Call<ItemCost> call, retrofit2.Response<ItemCost> response) {

                Log.v("wow", "json : " + new Gson().toJson(response));
                progressDialog.dismiss();

                if (response.isSuccessful()) {

                    int statusCode = response.body().getRajaongkir().getStatus().getCode();

                    if (statusCode == 200){
                        LayoutInflater inflater = (LayoutInflater) CheckoutActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View alertLayout = inflater.inflate(R.layout.rajaongkir_popup_cost, null);
                        alert = new AlertDialog.Builder(CheckoutActivity.this);
                        alert.setTitle("Result Cost");
                        alert.setMessage("this result your search");
                        alert.setView(alertLayout);
                        alert.setCancelable(true);

                        ad = alert.show();

                        final TextView tv_origin = (TextView) alertLayout.findViewById(R.id.tv_origin);
                        TextView tv_destination = (TextView) alertLayout.findViewById(R.id.tv_destination);
                        TextView tv_expedisi = (TextView) alertLayout.findViewById(R.id.tv_expedisi);
                        TextView tv_coast = (TextView) alertLayout.findViewById(R.id.tv_coast);
                        TextView tv_time = (TextView) alertLayout.findViewById(R.id.tv_time);

                        tv_origin.setText(response.body().getRajaongkir().getOriginDetails().getCityName()+" (Postal Code : "+
                                response.body().getRajaongkir().getOriginDetails().getPostalCode()+")");

                        tv_destination.setText(response.body().getRajaongkir().getDestinationDetails().getCityName()+" (Postal Code : "+
                                response.body().getRajaongkir().getDestinationDetails().getPostalCode()+")");

                        tv_expedisi.setText(response.body().getRajaongkir().getResults().get(0).getCosts().get(0).getDescription()+" ("+
                                response.body().getRajaongkir().getResults().get(0).getName()+") ");

                        tv_coast.setText("Rp. "+response.body().getRajaongkir().getResults().get(0).getCosts().get(1).getCost().get(0).getValue().toString());

                        tv_time.setText(response.body().getRajaongkir().getResults().get(0).getCosts().get(0).getCost().get(0).getEtd()+" (Days)");

                        String cost = response.body().getRajaongkir().getResults().get(0).getCosts().get(1).getCost().get(0).getValue().toString();
                        ongkir = cost;
                        provinsi.setText("");
                        kota.setText("");
                        Toast.makeText(CheckoutActivity.this, "Cost: " + "Rp." + cost, Toast.LENGTH_SHORT).show();

                        ((Button) alertLayout.findViewById(R.id.add_destination)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(CheckoutActivity.this, PembelianFragment.class);
                                startActivity(intent);
                            }
                        });
                    } else {

                        String message = response.body().getRajaongkir().getStatus().getDescription();
                        Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(CheckoutActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCost> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(CheckoutActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
