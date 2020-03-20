package app.gify.co.id.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.gify.co.id.R;

public class CheckoutActivity extends AppCompatActivity {

    Button prosescekout;
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        prosescekout = findViewById(R.id.prorsesCheckout);
        back = findViewById(R.id.backCheckout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prosescekout.setOnClickListener(view -> {
            Intent intent = new Intent(CheckoutActivity.this, Pembelian.class);
            startActivity(intent);
        });
    }
}
