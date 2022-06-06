package com.tanobel.it_yoga.tis_mobile;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

class SplashScreen extends AppCompatActivity {
    private int waktu_loading=4000;
    TextView txtVersi;
    String versi;

    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        txtVersi = findViewById(R.id.txtVersiApp);

        versi = "Versi " + BuildConfig.VERSION_NAME;
        txtVersi.setText(versi);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent login=new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(login);
                finish();

            }
        },waktu_loading);
    }
}