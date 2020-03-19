package app.gify.co.id.Fragment.keluar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import app.gify.co.id.activity.Login;
import app.gify.co.id.sessions.SessionManager;

public class KeluarFragment extends Fragment {

    SharedPreferences sharedPreferences;
    private SessionManager sessionManager;
    FirebaseAuth mAuth;
    Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        Intent intent = new Intent(getActivity(), Login.class);
        editor.clear();
        editor.apply();
        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin(false);
        startActivity(intent);
        getActivity().finish();
    }
}
