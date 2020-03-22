package app.gify.co.id.activity;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Random;

import app.gify.co.id.R;
import app.gify.co.id.adapter.AdapterCart;
import app.gify.co.id.modal.MadolCart;
import app.gify.co.id.thirdparty.GMailSender;
import app.gify.co.id.thirdparty.SenderAgent;
import app.gify.co.id.widgets.RecyclerTouchDelete;

import static app.gify.co.id.baseurl.UrlJson.GETBARANG;
import static app.gify.co.id.baseurl.UrlJson.GETCART;

public class CartActivity extends AppCompatActivity implements RecyclerTouchDelete.RecyclerTouchListener{

    Button Checkout, lanjutBelanja;
    ImageView backCart;
    TextView totalbelanjar, totalberat;
    AdapterCart adapterCart;
    ArrayList<MadolCart> madolCarts;
    String namacart, gambarcart, uidku;
    int kuantitas;
    GridLayoutManager glm;
    RecyclerView recyclerView;
    MainActivity mainActivity;
    NavigationView navigationView;
    public int hargaku, beratku;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String harga, berat;
    String template;
    Spanned templateConvert;
    NumberFormat format;
    Locale id;
    Random random;
    int lastNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        lanjutBelanja = findViewById(R.id.lanjutBelanjaChart);
        lanjutBelanja.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), List_Kado.class);
            startActivity(intent);
        });

        id = new Locale("id", "ID");

        format = NumberFormat.getCurrencyInstance(id);

        random = new Random();
        lastNumber = 0;

        for (int k = 0; k < 3; k++){
            lastNumber+=(random.nextInt(10)*Math.pow(10, k));
        }

        backCart = findViewById(R.id.backCartNav);
        backCart.setOnClickListener(v -> finish());

        Checkout = findViewById(R.id.checkoutChart);
        totalbelanjar = findViewById(R.id.totalBelanjaChart);
        totalberat = findViewById(R.id.totalBeratChart);
        recyclerView = findViewById(R.id.rvChart);

        preferences = PreferenceManager.getDefaultSharedPreferences(CartActivity.this);
        uidku = preferences.getString("uid", "");
        madolCarts = new ArrayList<>();
        getCart();
        glm = new GridLayoutManager(CartActivity.this, 1);
        recyclerView.setLayoutManager(glm);

        Checkout.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = preferences.edit();
            editor.remove("namaRange");
            editor.remove("namaAcara");
            editor.remove("buatAcara");
            editor.apply();
            startActivity(intent);

        });

        LocalBroadcastManager.getInstance(this).registerReceiver(passValue, new IntentFilter("message_subject_intent"));

        ItemTouchHelper.SimpleCallback callback = new RecyclerTouchDelete(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    public BroadcastReceiver passValue = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            harga = intent.getStringExtra("name");
            totalbelanjar.setText(harga + "");
            totalberat.setText(berat + "");
            template = "<h2> Gify Transaction </h2> " +
                    "<h3> Kamu baru saja melakukan pesanan dengan detaik sebagai berikut </h3>"
                    + "<p><b> Nama barang: </p></b>"
                    + "<p><b> Harga barang: Rp: " + format.format(Double.valueOf(replaceNumberOfAmount(harga, lastNumber))) + ". Silahkan transfer dengan tiga digit terakhir yaitu :" + lastNumber + "</p></b>"
                    + "<p><b> Jika sudah melakukan pembayaran, silahkan konfirmasi disini </p></b>"
                    + "https://api.whatsapp.com/send?phone=082325328732&text=Confirmation%20Text"
                    + "<h2>Salam, Gify Team</h2>";

            templateConvert = Html.fromHtml(template);
            Toast.makeText(getApplicationContext(), format.format(Double.valueOf(replaceNumberOfAmount(harga, lastNumber))), Toast.LENGTH_LONG).show();
        }
    };

    /*private void senderEmail(){
        SenderAgent senderAgent = new SenderAgent("gify.firebase@gmail.com", "Confirmation Transaction Gify", templateConvert, CartActivity.this);
        senderAgent.execute();
    }*/

    public String LoadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }

    private String replaceNumberOfAmount(String original, int replace){
        return original.substring(0, original.length() - 3) + replace;
    }

    private void getCart(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETCART, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String id_tetap = object.getString("id_tetap");
                    if (id_tetap.equalsIgnoreCase(uidku)){
                        kuantitas = object.getInt("jumlah");
                        int idbarang = object.getInt("id_barang");
                        getBerat(idbarang);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("getcart", "getCart: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(CartActivity.this);
        queue.add(objectRequest);
    }

    private void getBerat(int idbarang){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETBARANG, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    int id_barang = object.getInt("id");
                    if (idbarang==id_barang){
                        String gambar = object.getString("photo");
                        int harga = object.getInt("harga");
                        String namacart = object.getString("nama");
                        int berat = object.getInt("berat");
                        MadolCart madolCart = new MadolCart(gambar, harga, namacart, idbarang, kuantitas, berat);
                        madolCarts.add(madolCart);
                        adapterCart = new AdapterCart(madolCarts, CartActivity.this);
                        recyclerView.setAdapter(adapterCart);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.d("jsoner", "getBerat: " + error.getMessage());
        });
        RequestQueue queue = Volley.newRequestQueue(CartActivity.this);
        queue.add(objectRequest);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int dir, int pos) {
        if (viewHolder instanceof AdapterCart.MyCart){
            String name = madolCarts.get(viewHolder.getAdapterPosition()).getNamacart();

            MadolCart madolCart = madolCarts.get(viewHolder.getAdapterPosition());
            int deleteIndex =  viewHolder.getAdapterPosition();

            adapterCart.removeItem(viewHolder.getAdapterPosition());

        }
    }
}
