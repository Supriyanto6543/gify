package app.gify.co.id.publicload;

import android.os.AsyncTask;

import java.util.ArrayList;

import app.gify.co.id.modal.MadolFavorit;
import app.gify.co.id.publicinterface.FavoritInterface;
import app.gify.co.id.publicinterface.KadoInterface;

public class LoadFavorit extends AsyncTask<String, String, String> {

    FavoritInterface favoritInterface;
    ArrayList<MadolFavorit> madolFavorits = new ArrayList<>();

    public LoadFavorit(FavoritInterface favoritInterface) {
        this.favoritInterface = favoritInterface;
    }

    @Override
    protected void onPreExecute() {
        favoritInterface.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        favoritInterface.onStop(s, madolFavorits);
        super.onPostExecute(s);
    }
}
