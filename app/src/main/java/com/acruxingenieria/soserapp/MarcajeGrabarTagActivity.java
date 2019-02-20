package com.acruxingenieria.soserapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MarcajeGrabarTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_grabar_tag);

        TextView tv_msg = (TextView) findViewById(R.id.tvMarcajeGrabarTagError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());
    }
}
