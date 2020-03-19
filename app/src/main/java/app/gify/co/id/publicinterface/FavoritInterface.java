package app.gify.co.id.publicinterface;

import java.util.ArrayList;

import app.gify.co.id.modal.MadolFavorit;

public interface FavoritInterface {

    void onStart();
    void onStop(String success, ArrayList<MadolFavorit> madolFavorits);

}
