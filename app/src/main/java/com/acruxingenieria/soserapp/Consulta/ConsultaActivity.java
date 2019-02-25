package com.acruxingenieria.soserapp.Consulta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;

import java.util.ArrayList;

public class ConsultaActivity extends AppCompatActivity {

    private String mUser;
    private String positionSelected;
    private String bodegaSelected;

    private String idLecturaUnitaria;
    private ArrayList<String> idLecturaMasiva;

    private Fragment consultaUnitariaFragment = new ConsultaUnitariaFragment();
    private Fragment consultaMasivaFragment = new ConsultaMasivaFragment();
    private Fragment consultaUnitariaLecturaFragment = new ConsultaUnitariaLecturaFragment();
    private Fragment consultaMasivaLecturaFragment = new ConsultaMasivaLecturaFragment();
    private boolean consultaUnitariaSelected = false;
    private boolean consultaMasivaSelected = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_consulta_unitaria:
                    fragment = consultaUnitariaFragment;
                    consultaUnitariaSelected = true;
                    consultaMasivaSelected = false;
                    break;
                case R.id.navigation_consulta_masiva:
                    fragment = consultaMasivaFragment;
                    consultaUnitariaSelected = false;
                    consultaMasivaSelected = true;
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        //navBar
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationConsulta);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        receiveDataFromIntent();

        //default Fragment
        loadFragment(consultaUnitariaFragment);
        consultaUnitariaSelected = true;
        consultaMasivaSelected = false;

        configureButtonAtras();
    }

    //REPLACE FRAGMENT METHOD
    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.consulta_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        int SCAN_BUTTON_ID = 139;
        int SOUND_DOWN_BUTTON_ID = 25;
        int SCAN_TRIGGER_HH = 280;

        if ((keyCode == SCAN_BUTTON_ID || keyCode == SOUND_DOWN_BUTTON_ID || keyCode == SCAN_TRIGGER_HH)) {
            //AGREGAR TAG
            if (consultaUnitariaSelected){
                if ( ((ConsultaUnitariaFragment)consultaUnitariaFragment).read()){
                    idLecturaUnitaria = ((ConsultaUnitariaFragment)consultaUnitariaFragment).getIdLecturaUnitaria();

                    loadFragment(consultaUnitariaLecturaFragment);
                } else {
                    TextView tv_msg = (TextView) ((ConsultaUnitariaFragment)consultaUnitariaFragment).getSavedView().findViewById(R.id.tvConsultaUnitariaError);
                    tv_msg.setText(R.string.lectura_fallida);
                }

            } else if (consultaMasivaSelected){
                if ( ((ConsultaMasivaFragment)consultaMasivaFragment).read()){
                    idLecturaMasiva = ((ConsultaMasivaFragment)consultaMasivaFragment).getIdLecturaMasiva();

                    loadFragment(consultaMasivaLecturaFragment);
                } else {
                    TextView tv_msg = (TextView) ((ConsultaMasivaFragment)consultaMasivaFragment).getSavedView().findViewById(R.id.tvConsultaMasivaError);
                    tv_msg.setText(R.string.lectura_fallida);
                }

            }

        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private void configureButtonAtras() {
        Button btn_atras = (Button) findViewById(R.id.btnConsultaAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void receiveDataFromIntent() {
        mUser= getIntent().getStringExtra("mUser");
        positionSelected= getIntent().getStringExtra("positionSelected");
        bodegaSelected= getIntent().getStringExtra("bodegaSelected");
    }

    protected String getIdLecturaUnitaria(){
        return idLecturaUnitaria;
    }

    protected ArrayList<String> getIdLecturaMasiva(){
        return idLecturaMasiva;
    }

}