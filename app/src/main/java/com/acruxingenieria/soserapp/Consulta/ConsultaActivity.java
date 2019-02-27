package com.acruxingenieria.soserapp.Consulta;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.acruxingenieria.soserapp.QR.QrBuiltInActivity;
import com.acruxingenieria.soserapp.R;

import java.util.ArrayList;

public class ConsultaActivity extends AppCompatActivity {

    private String mUser;
    private String positionSelected;
    private String bodegaSelected;

    private String idLecturaUnitaria;
    private ArrayList<String> idLecturaMasiva;
    private String idLecturaMasivaSelected;

    private boolean qrReadingDone = false;

    //QR
    private boolean hasQRbuiltIn;

    //NFC
    private NfcAdapter mNfcAdapter;

    private Fragment consultaUnitariaFragment = new ConsultaUnitariaFragment();
    private Fragment consultaUnitariaLecturaFragment = new ConsultaUnitariaLecturaFragment();
    private Fragment consultaMasivaFragment = new ConsultaMasivaFragment();
    private Fragment consultaMasivaLecturaFragment = new ConsultaMasivaLecturaFragment();
    private Fragment consultaMasivaLecturaInfoFragment = new ConsultaMasivaLecturaInfoFragment();
    private boolean consultaUnitariaSelected = false;
    private boolean consultaMasivaSelected = false;
    private boolean consultaUnitariaFragmentLoaded = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_consulta_unitaria:
                    fragment = consultaUnitariaFragment;
                    consultaUnitariaSelected = true;
                    consultaUnitariaFragmentLoaded = true;
                    consultaMasivaSelected = false;
                    break;
                case R.id.navigation_consulta_masiva:
                    fragment = consultaMasivaFragment;
                    consultaUnitariaSelected = false;
                    consultaUnitariaFragmentLoaded = false;
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

        //hasQRbuiltIn = qrBuiltInActivity.hasQRLector();

        receiveDataFromIntent();

        //NFC
        configureNFCAdapter();

        //default Fragment
        loadFragment(consultaUnitariaFragment);
        consultaUnitariaSelected = true;
        consultaUnitariaFragmentLoaded = true;
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
    public boolean loadConsultaMasivaInfoFragment(String idSelected) {
        idLecturaMasivaSelected = idSelected;
        if (consultaMasivaLecturaInfoFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.consulta_fragment_container, consultaMasivaLecturaInfoFragment)
                    .commit();
            return true;
        }
        return false;
    }

    public String getIdLecturaMasivaSelected(){
        return idLecturaMasivaSelected;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        int SCAN_BUTTON_ID = 139;
        int SOUND_DOWN_BUTTON_ID = 25;
        int SCAN_TRIGGER_HH = 280;

        if ((keyCode == SCAN_BUTTON_ID || keyCode == SOUND_DOWN_BUTTON_ID || keyCode == SCAN_TRIGGER_HH)) {

            if (consultaUnitariaSelected){
                String lector = ((ConsultaUnitariaFragment)consultaUnitariaFragment).getLectorSelected();
                boolean readed = false;
                try {
                    readed = ((ConsultaUnitariaFragment) consultaUnitariaFragment).read();
                } catch (Exception e){
                    e.printStackTrace();
                }
                if ( readed && lector.equals("RFID")){
                    idLecturaUnitaria = ((ConsultaUnitariaFragment)consultaUnitariaFragment).getIdLecturaUnitaria();
                    if (idLecturaUnitaria !=null)
                        loadFragment(consultaUnitariaLecturaFragment);
                    else {
                        ((ConsultaUnitariaFragment)consultaUnitariaFragment).showError();
                        loadFragment(consultaUnitariaFragment);
                        consultaUnitariaSelected = true;
                        consultaMasivaSelected = false;
                    }


                } if (readed && lector.equals("QR")){
                    //ACTIVITY QR_CAM
                    Log.i("QR","QR Cam Activity open");


                } else {
                    loadFragment(consultaUnitariaFragment);
                    consultaUnitariaSelected = true;
                    consultaMasivaSelected = false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4) {//4 for QR unitaria qr cam
            if(resultCode == Activity.RESULT_OK){
                idLecturaUnitaria = data.getStringExtra("QR_ID");
                qrReadingDone = true;

            }
        }
        if (requestCode == 5) {//5 for QR unitaria built in
            if(resultCode == Activity.RESULT_OK){
                idLecturaUnitaria = data.getStringExtra("QR_ID");
                qrReadingDone = true;

            }
        }
    }

    public void displayQrInfo(){
        if (idLecturaUnitaria !=null)
            loadFragment(consultaUnitariaLecturaFragment);

        else {
            ((ConsultaUnitariaFragment)consultaUnitariaFragment).showError();
            loadFragment(consultaUnitariaFragment);
            consultaUnitariaSelected = true;
            consultaMasivaSelected = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //QR
        if (consultaUnitariaSelected && qrReadingDone){
            displayQrInfo();
            qrReadingDone = false;
        }

        //NFC
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                    this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
    }


    //NFC
    @Override
    protected void onNewIntent(Intent intent) {

        if ( ((ConsultaUnitariaFragment)consultaUnitariaFragment).getLectorSelected().equals("NFC")){
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if(tag != null) {
                String id = bytesToHexString(tag.getId());
                if (id != null){
                    idLecturaUnitaria = id;
                    loadFragment(consultaUnitariaLecturaFragment);
                } else {
                    loadFragment(consultaUnitariaFragment);
                    consultaUnitariaSelected = true;
                    consultaMasivaSelected = false;
                }
            }
        }
    }
    //NFC
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString().toUpperCase();
    }
    //NFC
    private void configureNFCAdapter(){
        //Init NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }
    //NFC
    @Override
    public void onPause() {
        super.onPause();

        if(mNfcAdapter!= null){
            mNfcAdapter.disableForegroundDispatch(ConsultaActivity.this);
        }
    }

    public boolean hasQRbuiltIn() {
        return hasQRbuiltIn;
    }

}