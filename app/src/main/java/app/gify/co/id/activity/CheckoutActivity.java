package app.gify.co.id.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.LineNumberReader;

import app.gify.co.id.Fragment.pembelian.PembelianFragment;
import app.gify.co.id.R;
import app.gify.co.id.baseurl.UrlJson;

public class CheckoutActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button prosescekout;
    ImageView back;

    EditText NamaPenerima, NoPenerima, alamatUbah, kelurahan, kecamatan;
    String currentUserID, Lnama, LNohp, Lalamat;
    ImageView gantiAlamat;
    TextView textViewCheckOutAlamat, alamat, kelurahan2, kecemetan, kota, provinsi;

    HintArrayAdapter hintArrayAdapter, hintArrayAdapterKu;

    Spinner Kota, Provinsi;

    DatabaseReference RootRef;
    FirebaseAuth mAuth;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        RootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        textViewCheckOutAlamat = findViewById(R.id.textviewAlamatCheckout);
        NamaPenerima = findViewById(R.id.namaPenerimaCheckout);
        NoPenerima = findViewById(R.id.noHPPenerimaCheckout);
        alamatUbah = findViewById(R.id.edittextAlamatCheckout);
        alamat = findViewById(R.id.textviewAlamatCheckout);
        kelurahan2 = findViewById(R.id.kelurahanCheckoutText);
        kecemetan = findViewById(R.id.kecamatanCheckoutText);
        kecamatan = findViewById(R.id.kecamatanCheckout);
        kelurahan = findViewById(R.id.kelurahanCheckout);
        kota = findViewById(R.id.kotaCheckoutText);
        provinsi = findViewById(R.id.provinsiCheckOutText);
        gantiAlamat = findViewById(R.id.gantiAlamatCheckout);
        Kota = findViewById(R.id.kotaCheckout);
        Provinsi = findViewById(R.id.provinsiCheckout);

        hintArrayAdapter = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintArrayAdapterKu = new HintArrayAdapter<String>(getApplicationContext(), 0);
        hintArrayAdapter.add("hint");
        hintArrayAdapterKu.add("hint");

        prosescekout = findViewById(R.id.prorsesCheckout);
        back = findViewById(R.id.backCheckout);
        back.setOnClickListener(v -> finish());

        gantiAlamat.setOnClickListener(v -> {
            alamatUbah.setVisibility(View.VISIBLE);
            alamat.setVisibility(View.GONE);
            kelurahan.setVisibility(View.VISIBLE);
            kelurahan2.setVisibility(View.GONE);
            kecamatan.setVisibility(View.VISIBLE);
            kecemetan.setVisibility(View.GONE);
            kota.setVisibility(View.GONE);
            Kota.setVisibility(View.VISIBLE);
            provinsi.setVisibility(View.GONE);
            Provinsi.setVisibility(View.VISIBLE);
        });

        prosescekout.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, PembelianFragment.class);
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
            startActivity(intent);
        });

        cobaOngkir1();
        cobaOngkir2();

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

    private void sendCart(){

    }
}
