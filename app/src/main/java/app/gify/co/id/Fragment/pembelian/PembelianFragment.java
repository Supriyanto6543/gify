package app.gify.co.id.Fragment.pembelian;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.gify.co.id.R;
import app.gify.co.id.activity.MainActivity;
import app.gify.co.id.activity.Pembelian;


public class PembelianFragment extends Fragment {

    ImageView navView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pembelian, container, false);

        navView = view.findViewById(R.id.backDetailKado);
        navView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        return view;
    }
}
