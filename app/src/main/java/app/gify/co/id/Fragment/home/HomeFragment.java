package app.gify.co.id.Fragment.home;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import app.gify.co.id.R;
import app.gify.co.id.activity.List_Kado;
import app.gify.co.id.activity.MainActivity;

import static app.gify.co.id.baseurl.UrlJson.GETACARA;
import static app.gify.co.id.baseurl.UrlJson.GETKATEGORI;
import static app.gify.co.id.baseurl.UrlJson.GETRANGE;


public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private String kadobuatsiapaku, acaraapaku, bulanku, namas;
    NumberPicker numberpicker;
    private int hariku, tahunku, bulanserver, acaraint, kadoint;
    private Spinner kadobuatsiapa, acarapa;
    private Calendar date;
    private TextView tahun,hari, bulan;
    private ImageView navFragmentHome;
    private HintArrayAdapter hintAdapter, hintadapterku;
    String[] kadolist;
    private Boolean bulanbool=false, haribool=false, tahunbool=false;
    private Button carikado;
    Dialog alertadd;
    private Dialog dialog;
    private Boolean kategoris = false, acaras = false;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_home, container, false);
        kadobuatsiapa=root.findViewById(R.id.buatSiapaCari);
        acarapa=root.findViewById(R.id.acaraApaCari);
        hari=root.findViewById(R.id.tanggalCari);
        bulan=root.findViewById(R.id.bulanCari);
        tahun=root.findViewById(R.id.tahunCari);
        carikado=root.findViewById(R.id.cariKado);

        /*mDialog = new ProgressDialog(getContext());
        LayoutInflater inflaterku = getLayoutInflater();
        View dialogLayout = inflaterku.inflate(R.layout.loading, null);
        *//*LayoutInflater factory = LayoutInflater.from(getActivity());
        final View viewku = factory.inflate(R.layout.loading, null);
        alertadd.setView(viewku);*//*
        mDialog.setView(dialogLayout);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();*/
        dialog  = new Dialog(getActivity());
        inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.loading, null);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(layout);
        dialog.show();

        hintAdapter = new HintArrayAdapter<String>(getContext(), 0);
        hintadapterku = new HintArrayAdapter<String>(getContext(), 0);
        hintAdapter.add("hint");
        hintadapterku.add("hint");
        hari.setOnClickListener(view1 -> {
            haribool = true;
            showdatedaypicker();
        });


        navFragmentHome = root.findViewById(R.id.navFragmentHome);
        navFragmentHome.setOnClickListener(v -> ((MainActivity) getActivity()).openDrawer());

        getkategori();
        getAcara();

        tahun.setOnClickListener(view1 -> showdateyearpicker());

        bulan.setOnClickListener(view -> {
            bulanbool = true;
            showdatedaypicker();
        });

        carikado.setOnClickListener(view1 ->  {
            kadobuatsiapaku = String.valueOf(kadobuatsiapa.getSelectedItem());
            acaraapaku = String.valueOf(acarapa.getSelectedItem());
            acaraint = acarapa.getSelectedItemPosition();
            kadoint = kadobuatsiapa.getSelectedItemPosition();
            if (hariku == 0 || bulanku == null || tahunku == 0 || kadobuatsiapaku.equals("hint") || acaraapaku.equals("hint")){
                Toast.makeText(getContext(), "Isi Terlebih dahulu yang kosong", Toast.LENGTH_SHORT).show();
            }else {
                getRange();

            }
        });
        return root;
    }

    private void showdatedaypicker() {
        final Calendar currentdate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            if (bulanbool){
                bulanserver = month;
                switch (month) {
                    case 0:
                        bulan.setText("Januari");
                        bulanku = "Januari";
                        break;
                    case 1:
                        bulan.setText("Februari");
                        bulanku = "Februari";
                        break;
                    case 2:
                        bulan.setText("Maret");
                        bulanku = "Maret";
                        break;
                    case 3:
                        bulan.setText("April");
                        bulanku = "April";
                        break;
                    case 4:
                        bulan.setText("Mei");
                        bulanku = "Mei";
                        break;
                    case 5:
                        bulan.setText("Juni");
                        bulanku = "Juni";
                        break;
                    case 6:
                        bulan.setText("Juli");
                        bulanku = "Juli";
                        break;
                    case 7:
                        bulan.setText("Agustus");
                        bulanku = "Agustus";
                        break;
                    case 8:
                        bulan.setText("September");
                        bulanku = "September";
                        break;
                    case 9:
                        bulan.setText("October");
                        bulanku = "October";
                        break;
                    case 10:
                        bulan.setText("November");
                        bulanku = "November";
                        break;
                    case 11:
                        bulan.setText("Desember");
                        bulanku = "Desember";
                        break;
                }
            }else if (haribool){
                hari.setText(String.valueOf(day));
                hariku = day;
            }
            haribool=false;
            bulanbool=false;
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog, dateSetListener, currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH), currentdate.get(Calendar.DAY_OF_MONTH));
        if (bulanbool){
            (datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("month", "id", "android")).setVisibility(View.VISIBLE);
            (datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        }else if (haribool){
            (datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
            (datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.VISIBLE);
        }
        (datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.show();
    }

    private void showdateyearpicker() {
        final Calendar currentdate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            tahun.setText(String.valueOf(year));
            tahunku = year;
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Dialog, dateSetListener, currentdate.get(Calendar.YEAR), currentdate.get(Calendar.MONTH), currentdate.get(Calendar.DAY_OF_MONTH));
        (datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        (datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.show();
    }

    private void akuGanteng(){
        View view = getLayoutInflater().inflate(R.layout.activity_main, acarapa, false);


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class HintArrayAdapter<T> extends ArrayAdapter<T> {

        Context mContext;

        public HintArrayAdapter(Context context, int resource) {
            super(context, resource);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.spinnner, parent, false);
            TextView texview = (TextView) view.findViewById(android.R.id.text1);

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

    public void getkategori(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETKATEGORI, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0;a < array.length() ; a++){

                    JSONObject object = array.getJSONObject(a);
                    String tes = object.getString("id");
                    String ku = object.getString("sub_category");
                    hintAdapter.add(ku);
                    kadobuatsiapa.setAdapter(hintAdapter);

                    //spinner1.setSelection(0); //display hint. Actually you can ignore it, because the default is already 0
                    kadobuatsiapa.setSelection(0, false); //use this if don't want to onItemClick called for the hint

                    kadobuatsiapa.setOnItemSelectedListener(HomeFragment.this);
                    kategoris = true;
                    dismissdialog();
                    Log.d("makansamaale", "onResponse: "+tes+" "+ku);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.getMessage();
        });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(objectRequest);
    }

    private void getAcara(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETACARA, null, response -> {
            try {
                JSONArray array = response.getJSONArray("YukNgaji");
                for (int a = 0; a < array.length(); a++){
                    JSONObject object = array.getJSONObject(a);
                    String acara = object.getString("untuk_acara");
                    hintadapterku.add(acara);

                    acarapa.setAdapter(hintadapterku);

                    acaras = true;
                    acarapa.setSelection(0, false);
                    dismissdialog();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> error.getMessage());
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(objectRequest);
    }

    private void getRange(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, GETRANGE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("YukNgaji");
                    for (int a = 0; a < array.length(); a++){
                        JSONObject object = array.getJSONObject(a);
                        int hari = object.getInt("hari");
                        int bulan = object.getInt("bulan");
                        int hariend = object.getInt("hariend");
                        int bulanend = object.getInt("bulanend");
                        if (bulanserver == 11 ){
                            if (bulan == 11){
                                if (hariku >= hari){
                                    if (0 <= bulanend){
                                        //YANG DI PUT STRING JANGAN DI UBAH, SEKALI LAGI BERUBAH
                                        namas = object.getString("nama");
                                        Intent intent = new Intent(getContext(), List_Kado.class);
                                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                        editor = preferences.edit();
                                        editor.putString("range", namas);
                                        editor.putString("acara", acaraapaku);
                                        editor.putString("buat", kadobuatsiapaku);
                                        editor.apply();
                                        intent.putExtra("range", namas);
                                        intent.putExtra("acara", acaraapaku);
                                        intent.putExtra("buat", kadobuatsiapaku);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }else if (bulanserver >= 0){
                            if (bulanserver == bulan){
                                if (hariku >= hari){
                                    namas = object.getString("nama");
                                    Intent intent = new Intent(getContext(), List_Kado.class);
                                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                    editor = preferences.edit();
                                    //YANG DI PUT STRING JANGAN DI UBAH, SEKALI LAGI BERUBAH
                                    editor.putString("range", namas);
                                    editor.putString("acara", acaraapaku);
                                    editor.putString("buat", kadobuatsiapaku);
                                    editor.apply();
                                    intent.putExtra("range", namas);
                                    intent.putExtra("acara", acaraapaku);
                                    intent.putExtra("buat", kadobuatsiapaku);
                                    startActivity(intent);
                                }
                            }else if (bulanserver == bulanend){
                                if (hariku <= hariend){
                                    namas = object.getString("nama");
                                    Intent intent = new Intent(getContext(), List_Kado.class);
                                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                    editor = preferences.edit();
                                    //YANG DI PUT STRING JANGAN DI UBAH, SEKALI LAGI BERUBAH
                                    editor.putString("range", namas);
                                    editor.putString("acara", acaraapaku);
                                    editor.putString("buat", kadobuatsiapaku);
                                    editor.apply();
                                    intent.putExtra("range", namas);
                                    intent.putExtra("acara", acaraapaku);
                                    intent.putExtra("buat", kadobuatsiapaku);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Throwable::printStackTrace);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(objectRequest);
    }

    private void dismissdialog(){
        if (kategoris&&acaras){
            dialog.dismiss();
        }
    }
}
