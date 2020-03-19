package app.gify.co.id.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.gify.co.id.R;

public class CheckoutActivity extends AppCompatActivity {

    Button prosescekout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        prosescekout = findViewById(R.id.prorsesCheckout);

        prosescekout.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, Pembelian.class);
            startActivity(intent);
        });
    }
}
