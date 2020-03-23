package app.gify.co.id.Fragment.pembelian;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import app.gify.co.id.Fragment.home.HomeFragment;
import app.gify.co.id.R;
import app.gify.co.id.activity.CartActivity;
import app.gify.co.id.activity.MainActivity;


public class PembelianFragment extends Fragment {

    ImageView navView, toChart;
    Button cariKadoPembelian;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pembelian, container, false);
        cariKadoPembelian = view.findViewById(R.id.cariKadoPembelians);
        cariKadoPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                assert fm != null;
                FragmentTransaction ft = fm.beginTransaction();
                HomeFragment llf = new HomeFragment();
                ft.replace(R.id.frame, llf);
                ft.commit();
            }
        });

        toChart = view.findViewById(R.id.cartPembelian);
        toChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        navView = view.findViewById(R.id.backPembelian);
        navView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        return view;
    }



    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();

            return true;
        }

        return false;
    }




}
