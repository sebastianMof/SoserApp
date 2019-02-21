package com.acruxingenieria.soserapp.Marcaje;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.acruxingenieria.soserapp.BodegaActivity;
import com.acruxingenieria.soserapp.R;

import java.util.ArrayList;

public class MarcajeGrabarTagActivity extends AppCompatActivity {

    private String mUser;
    private String positionSelected;
    private String bodegaSelected;
    private String tipoMarcaje;
    //marcaje=material
    private String marcajeMaterialNombre;
    private String marcajeMaterialStockcode;
    private String marcajeMaterialBin;
    private String marcajeMaterialFechavenc;
    private String marcajeMaterialCantidad;
    //marcaje=bin
    private String marcajeBinBin;

    private ArrayList<String> lectorList;
    private String lectorSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_grabar_tag);

        TextView tv_msg = (TextView) findViewById(R.id.tvMarcajeGrabarTagError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        receiveDataFromIntent();

        configureLectorList();
        configureSpinnerLector();

        configureButtonAtras();

    }

    private void configureLectorList() {
        lectorList = new ArrayList<>();
        lectorList.add("RFID");
        lectorList.add("QR");
        lectorList.add("NFC");
    }

    private void configureSpinnerLector() {
        Spinner spn_pos = (Spinner) findViewById(R.id.spinnerMarcajeGrabarTag);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(MarcajeGrabarTagActivity.this, R.layout.spinner_item, lectorList){

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

    @Override
    public void onBackPressed(){
        finish();
    }

    private void configureButtonAtras() {
        Button btn_atras = (Button) findViewById(R.id.btnMarcajeGrabarTagAtras);
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
            finish();
        }

        return super.onKeyUp(keyCode, event);
    }

    private void receiveDataFromIntent() {
        mUser = getIntent().getStringExtra("mUser");
        positionSelected = getIntent().getStringExtra("positionSelected");
        bodegaSelected = getIntent().getStringExtra("bodegaSelected");
        tipoMarcaje = getIntent().getStringExtra("tipoMarcaje");
        if (tipoMarcaje.equals("material")){
            marcajeMaterialNombre = getIntent().getStringExtra("marcajeMaterialNombre");
            marcajeMaterialStockcode = getIntent().getStringExtra("marcajeMaterialStockcode");
            marcajeMaterialBin = getIntent().getStringExtra("marcajeMaterialBin");
            marcajeMaterialFechavenc = getIntent().getStringExtra("marcajeMaterialFechavenc");
            marcajeMaterialCantidad = getIntent().getStringExtra("marcajeMaterialCantidad");

        } else if (tipoMarcaje.equals("bin")){
            marcajeBinBin = getIntent().getStringExtra("marcajeBinBin");
        }


    }
}