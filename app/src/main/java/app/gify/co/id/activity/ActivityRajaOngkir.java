package app.gify.co.id.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.gify.co.id.R;
import app.gify.co.id.rajaongkir.adapterongkir.AdapterCheckCity;
import app.gify.co.id.rajaongkir.adapterongkir.AdapterKurir;
import app.gify.co.id.rajaongkir.adapterongkir.AdapterProvinsi;
import app.gify.co.id.rajaongkir.apiongkir.ApiRaja;
import app.gify.co.id.rajaongkir.apiongkir.BaseApi;
import app.gify.co.id.rajaongkir.modelongkir.biaya.ItemCost;
import app.gify.co.id.rajaongkir.modelongkir.kota.ItemCity;
import app.gify.co.id.rajaongkir.modelongkir.kota.Result;
import app.gify.co.id.rajaongkir.modelongkir.kurir.ItemExpedisi;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.Province;
import app.gify.co.id.rajaongkir.modelongkir.provinsi.ResultOngkir;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by SUPRIYANTO on 03/25/2020.
 */

public class ActivityRajaOngkir extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etFromProvince, etToProvince;
    private EditText etFromCity, etToCity;
    private EditText etWeight, etCourier;

    private AlertDialog.Builder alert;
    private AlertDialog ad;
    private EditText searchList;
    private ListView mListView;

    private AdapterProvinsi adapter_province;
    private List<ResultOngkir> ListProvince = new ArrayList<ResultOngkir>();

    private AdapterCheckCity adapter_city;
    private List<Result> ListCity = new ArrayList<Result>();

    private AdapterKurir adapter_expedisi;
    private List<ItemExpedisi> listItemExpedisi = new ArrayList<ItemExpedisi>();

    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rajaongkir_activity);

        toolbar = findViewById(R.id.toolbar);
        etFromProvince = (EditText) findViewById(R.id.etFromProvince);
        etFromCity = (EditText) findViewById(R.id.etFromCity);
        etToProvince = (EditText) findViewById(R.id.etToProvince);
        etToCity = (EditText) findViewById(R.id.etToCity);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etCourier = (EditText) findViewById(R.id.etCourier);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etFromProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpProvince(etFromProvince, etFromCity);
            }
        });

        etFromCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (etFromProvince.getTag().equals("")) {
                        etFromProvince.setError("Please chooise your form province");
                    } else {
                        popUpCity(etFromCity, etFromProvince);
                    }

                } catch (NullPointerException e) {
                    etFromProvince.setError("Please chooise your form province");
                }

            }
        });

        etToProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpProvince(etToProvince, etToCity);

            }
        });

        etToCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (etToProvince.getTag().equals("")) {
                        etToProvince.setError("Please chooise your to province");
                    } else {
                        popUpCity(etToCity, etToProvince);
                    }

                } catch (NullPointerException e) {
                    etToProvince.setError("Please chooise your to province");
                }

            }
        });

        etCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpExpedisi(etCourier);
            }
        });

        Button btnProcess = (Button) findViewById(R.id.btnProcess);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Origin = etFromCity.getText().toString();
                String destination = etToCity.getText().toString();
                String Weight = etWeight.getText().toString();
                String expedisi = etCourier.getText().toString();

                if (Origin.equals("")){
                    etFromCity.setError("Please input your origin");
                } else if (destination.equals("")){
                    etToCity.setError("Please input your destination");
                } else if (Weight.equals("")){
                    etWeight.setError("Please input your Weight");
                } else if (expedisi.equals("")){
                    etCourier.setError("Please input your ItemExpedisi");
                } else {

                    progressDialog = new ProgressDialog(ActivityRajaOngkir.this);
                    progressDialog.setMessage("Please wait..");
                    progressDialog.show();

                    getCoast(
                            etFromCity.getTag().toString(),
                            etToCity.getTag().toString(),
                            etWeight.getText().toString(),
                            etCourier.getText().toString()
                    );
                }

            }
        });


    }

    public void popUpProvince(final EditText etProvince, final EditText etCity ) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.rajaongkir_popup_search, null);

        alert = new AlertDialog.Builder(this);
        alert.setTitle("List ListProvince");
        alert.setMessage("select your province");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new ActivityRajaOngkir.MyTextWatcherProvince(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListProvince.clear();
        adapter_province = new AdapterProvinsi(this, ListProvince);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                ResultOngkir cn = (ResultOngkir) o;

                etProvince.setError(null);
                etProvince.setText(cn.getProvince());
                etProvince.setTag(cn.getProvinceId());

                etCity.setText("");
                etCity.setTag("");

                ad.dismiss();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        getProvince();

    }

    public void popUpCity(final EditText etCity, final EditText etProvince) {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.rajaongkir_popup_search, null);

        alert = new AlertDialog.Builder(this);
        alert.setTitle("List City");
        alert.setMessage("select your city");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new ActivityRajaOngkir.MyTextWatcherCity(searchList));
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

                etCity.setError(null);
                etCity.setText(cn.getCityName());
                etCity.setTag(cn.getCityId());

                ad.dismiss();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        getCity(etProvince.getTag().toString());

    }

    public void popUpExpedisi(final EditText etExpedisi) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.rajaongkir_popup_search, null);

        alert = new AlertDialog.Builder(this);
        alert.setTitle("List Expedisi");
        alert.setMessage("select your Expedisi");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new ActivityRajaOngkir.MyTextWatcherCity(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        listItemExpedisi.clear();
        adapter_expedisi = new AdapterKurir(this, listItemExpedisi);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                ItemExpedisi cn = (ItemExpedisi) o;

                etExpedisi.setError(null);
                etExpedisi.setText(cn.getName());
                etExpedisi.setTag(cn.getId());

                ad.dismiss();
            }
        });

        getExpedisi();

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
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Province> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCity> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getExpedisi() {

        ItemExpedisi itemItemExpedisi = new ItemExpedisi();

        itemItemExpedisi = new ItemExpedisi("1", "pos");
        listItemExpedisi.add(itemItemExpedisi);
        itemItemExpedisi = new ItemExpedisi("1", "tiki");
        listItemExpedisi.add(itemItemExpedisi);
        itemItemExpedisi = new ItemExpedisi("1", "jne");
        listItemExpedisi.add(itemItemExpedisi);

        mListView.setAdapter(adapter_expedisi);

        adapter_expedisi.setList(listItemExpedisi);
        adapter_expedisi.filter("");

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
                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View alertLayout = inflater.inflate(R.layout.rajaongkir_popup_cost, null);
                        alert = new AlertDialog.Builder(ActivityRajaOngkir.this);
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

                        tv_coast.setText("Rp. "+response.body().getRajaongkir().getResults().get(0).getCosts().get(0).getCost().get(0).getValue().toString());

                        tv_time.setText(response.body().getRajaongkir().getResults().get(0).getCosts().get(0).getCost().get(0).getEtd()+" (Days)");

                        etFromProvince.setText("");
                        etFromCity.setText("");
                        etToProvince.setText("");
                        etToCity.setText("");
                        etWeight.setText("");
                        etCourier.setText("");

                        ((Button) alertLayout.findViewById(R.id.add_destination)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "You click" + tv_origin, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {

                        String message = response.body().getRajaongkir().getStatus().getDescription();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCost> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ids = item.getItemId();

        if (ids == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
