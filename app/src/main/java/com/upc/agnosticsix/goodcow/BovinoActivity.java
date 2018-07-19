package com.upc.agnosticsix.goodcow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BovinoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bovino);
    }

    @Override
    public void onBackPressed() {
            finish();
    }
}
