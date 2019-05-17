package com.acruxingenieria.soserapp.Marcaje;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.acruxingenieria.soserapp.QR.QrBuiltInActivity;
import com.acruxingenieria.soserapp.QR.QrCamActivity;
import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.RFID.RFIDController;
import com.acruxingenieria.soserapp.Sesion;

import java.util.ArrayList;

public class MarcajeBorrarTagActivity extends AppCompatActivity {

    private Sesion session;
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
    //NFC
    private NfcAdapter mNfcAdapter;
    //RFID
    ArrayList<String> RFID_IDs = new ArrayList<>();
    RFIDController rfidController;

    TextView tv_msg;

    private ArrayList<String> lectorList;
    private String lectorSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_borrar_tag);

        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

        tv_msg = (TextView) findViewById(R.id.tvMarcajeBorrarTagError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        //NFC
        configureNFCAdapter();
        //RFID
        initRFIDcontroller();

        configureLectorList();
        configureSpinnerLector();

        receiveDataFromIntent();

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

            switch (lectorSelected) {
                case "RFID": {
                    tv_msg.setText(R.string.leyendo);

                    testRFID(12, 2, 15, "Yes");
                    if (RFID_IDs.size()>0) {
                        String result = RFID_IDs.get(0);

                        Intent intent = new Intent(MarcajeBorrarTagActivity.this, MarcajeBorrarTagConfirmacionActivity.class);
                        intent.putExtra("code", result);

                        intent.putExtra("mUser", mUser);
                        intent.putExtra("positionSelected", positionSelected);
                        intent.putExtra("bodegaSelected", bodegaSelected);

                        intent.putExtra("lectorSelected", lectorSelected);

                        startActivityForResult(intent, 3);
                    } else {
                        tv_msg.setText(R.string.tags_no_encontrados);
                    }
                    break;
                }
                case "QR": {
                    tv_msg.setText(R.string.leyendo);
                    openQRreading();
                    break;
                }
                case "NFC": {
                    tv_msg.setText(R.string.leer_nfc);
                    break;
                }
            }

        }

        return super.onKeyUp(keyCode, event);
    }

    private void receiveDataFromIntent() {

        mUser = getIntent().getStringExtra("mUser");
        positionSelected = getIntent().getStringExtra("positionSelected");
        bodegaSelected = getIntent().getStringExtra("bodegaSelected");
        tipoMarcaje = getIntent().getStringExtra("tipoMarcaje");

    }

    private void configureLectorList() {
        lectorList = new ArrayList<>();
        lectorList.add("RFID");
        lectorList.add("QR");
        lectorList.add("NFC");
    }

    private void configureSpinnerLector() {
        Spinner spn_pos = (Spinner) findViewById(R.id.spinnerMarcajeBorrarTag);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(MarcajeBorrarTagActivity.this, R.layout.spinner_item, lectorList){

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
                tv_msg.setText(R.string.esperando_lectura);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //RFID METHODS
    private void initRFIDcontroller(){
        rfidController = new RFIDController(MarcajeBorrarTagActivity.this);

    }
    public void testBeep(){
        rfidController.beepEnable=true;
        rfidController.doBeep();
    }
    public void testRFID (int pwr, int time, int filter, String toggleCount){

        rfidController.filterValue = filter;
        rfidController.toggleCount = toggleCount;
        rfidController.setTimeOutCount(time);
        rfidController.setPower(pwr);

        if (rfidController.readSingleUiid() != null){

            //CANTIDAD TAGS ENCONTRADOS: rfidController.getArrayList().size()
            RFID_IDs= new ArrayList<>();

            for (RFIDController.UUID n : rfidController.getArrayList()){
                RFID_IDs.add(n.getUuid());//LISTA CON LOS ENCONTRADOS
            }

            initRFIDcontroller();//clear the last time
            testBeep();
        }else {
            // PRINT NO SE ENCONTRARON TAGS
        }
    }

    //QR
    protected void openQRreading(){
        openCamQR();
        /*
        if (hasQRbuiltIn){
            openQRLector();
        } else {
            openCamQR();
        }*/

    }
    //QR
    protected void openCamQR(){
        Intent intent = new Intent(MarcajeBorrarTagActivity.this, QrCamActivity.class);
        startActivityForResult(intent, 3);
    }
    //QR HH
    protected void openQRLector(){
        Intent intent = new Intent(MarcajeBorrarTagActivity.this, QrBuiltInActivity.class);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 3) {//3 for DELETE
            if(resultCode == Activity.RESULT_OK){

                Intent confirmIntent = new Intent(MarcajeBorrarTagActivity.this,MarcajeBorrarTagConfirmacionActivity.class);
                confirmIntent.putExtra("code", data.getStringExtra("ID"));

                confirmIntent.putExtra("mUser", mUser);
                confirmIntent.putExtra("positionSelected", positionSelected);
                confirmIntent.putExtra("bodegaSelected", bodegaSelected);
                confirmIntent.putExtra("tipoMarcaje", tipoMarcaje);
                confirmIntent.putExtra("session", session);

                if (data.getStringExtra("lectorSelected")!=null)
                    if (data.getStringExtra("lectorSelected").equals("QR")){
                        startActivity(confirmIntent);
                    }
                finish();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    //NFC
    @Override
    public void onResume() {
        super.onResume();
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
        if (lectorSelected.equals("NFC")){
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(tag != null) {
                String id = bytesToHexString(tag.getId());
                if (id != null){
                    Intent newIntent = new Intent(MarcajeBorrarTagActivity.this,MarcajeBorrarTagConfirmacionActivity.class);
                    newIntent.putExtra("code",id);

                    newIntent.putExtra("mUser", mUser);
                    newIntent.putExtra("positionSelected", positionSelected);
                    newIntent.putExtra("bodegaSelected", bodegaSelected);

                    startActivityForResult(newIntent,3);

                } else {
                    tv_msg.setText(R.string.lectura_fallida);
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
            mNfcAdapter.disableForegroundDispatch(MarcajeBorrarTagActivity.this);
        }
    }

}
