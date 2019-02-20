package com.acruxingenieria.soserapp.Marcaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

public class MarcajeBorrarTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_borrar_tag);

        TextView tv_msg = (TextView) findViewById(R.id.tvMarcajeBorrarTagError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        configureButtonAtras();
    }

    @Override
    public void onBackPressed(){
        finish();
    }


    private void configureButtonAtras() {
        Button btn_atras = (Button) findViewById(R.id.btnMarcajeBorrarTagAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        int SCAN_BUTTON_ID = 139;
        int SOUND_DOWN_BUTTON_ID = 25;
        int SCAN_TRIGGER_HH = 280;

        if ((keyCode == SCAN_BUTTON_ID || keyCode == SOUND_DOWN_BUTTON_ID || keyCode == SCAN_TRIGGER_HH)) {
            Intent intent =new Intent(MarcajeBorrarTagActivity.this,MarcajeBorrarTagConfirmacionActivity.class);
            startActivity(intent);

        }

        return super.onKeyUp(keyCode, event);
    }

}
