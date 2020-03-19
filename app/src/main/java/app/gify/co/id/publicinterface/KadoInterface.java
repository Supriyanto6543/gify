package app.gify.co.id.publicinterface;

import java.util.ArrayList;

import app.gify.co.id.modal.MadolKado;

public interface KadoInterface {

    void onStart();
    void onStop(String success, ArrayList<MadolKado> madolKados);

}
