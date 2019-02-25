package com.acruxingenieria.soserapp.Marcaje;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarcajeLeerBinActivity extends AppCompatActivity {

    private ArrayList<String> lectorList;
    private String lectorSelected;

    private String readedBIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_leer_bin);

        TextView tv_msg = (TextView) findViewById(R.id.tvMarcajeLeerBinError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        configureLectorList();
        configureSpinnerLector();

        configureButtonAtras();

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private void configureButtonAtras() {
        Button btn_atras = (Button) findViewById(R.id.btnMarcajeLeerBinAtras);
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
            //AGREGAR TAG
            readedBIN = "readedBIN";

            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", readedBIN);
            setResult(Activity.RESULT_OK, returnIntent);

            finish();
        }

        return super.onKeyUp(keyCode, event);
    }

    private void configureLectorList() {
        lectorList = new ArrayList<>();
        lectorList.add("RFID");
        lectorList.add("QR");
        lectorList.add("NFC");
    }

    private void configureSpinnerLector() {
        Spinner spn_pos = (Spinner) findViewById(R.id.spinnerMarcajeLeerBin);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(MarcajeLeerBinActivity.this, R.layout.spinner_item, lectorList){

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.color1));

                return view;
            }
        };
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn_pos.setAdapter(spnAdapter);

        spn_pos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                lectorSelected =  (String) adapterView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
