package app.gify.co.id.publicinterface;

import java.util.ArrayList;

import app.gify.co.id.modal.MadolPembelian;

public interface PembelianInterface {

    void onStart();
    void onStop(String success, ArrayList<MadolPembelian> madolPembelians);

}
