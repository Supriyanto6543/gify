package app.gify.co.id.publicload;

import android.os.AsyncTask;

import java.util.ArrayList;

import app.gify.co.id.modal.MadolKado;
import app.gify.co.id.publicinterface.KadoInterface;

public class LoadKado extends AsyncTask<String, String, String> {

    KadoInterface kadoInterface;
    ArrayList<MadolKado> madolKados = new ArrayList<>();

    public LoadKado(KadoInterface kadoInterface) {
        this.kadoInterface = kadoInterface;
    }

    @Override
    protected void onPreExecute() {
        kadoInterface.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        kadoInterface.onStop(s, madolKados);
        super.onPostExecute(s);
    }
}
