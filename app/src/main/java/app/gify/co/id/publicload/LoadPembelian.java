package app.gify.co.id.publicload;

import android.os.AsyncTask;

import java.util.ArrayList;

import app.gify.co.id.modal.MadolPembelian;
import app.gify.co.id.publicinterface.PembelianInterface;

public class LoadPembelian extends AsyncTask<String, String, String> {

    PembelianInterface pembelianInterface;
    ArrayList<MadolPembelian> madolPembelians = new ArrayList<>();

    public LoadPembelian(PembelianInterface pembelianInterface) {
        this.pembelianInterface = pembelianInterface;
    }

    @Override
    protected void onPreExecute() {
        pembelianInterface.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        pembelianInterface.onStop(s, madolPembelians);
        super.onPostExecute(s);
    }
}
