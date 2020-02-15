package com.yx.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import yx.ui.LinearLayout;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        LinearLayout linearLayout = findViewById(R.id.ration_layout);

        linearLayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.bottom));
        linearLayout.setAdjustViewBounds(true);
    }
}
