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
import android.widget.Toast;

import com.acruxingenieria.soserapp.Consulta.ConsultaActivity;
import com.acruxingenieria.soserapp.QR.QrBuiltInActivity;
import com.acruxingenieria.soserapp.QR.QrCamActivity;
import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.RFID.RFIDController;

import java.util.ArrayList;
import java.util.Objects;

public class MarcajeGrabarTagActivity extends AppCompatActivity {

    private String mUser;
    private String positionSelected;
    private String bodegaSelected;
    private String tipoMarcaje;
    //NFC
    private NfcAdapter mNfcAdapter;
    //RFID
    ArrayList<String> RFID_IDs = new ArrayList<>();
    RFIDController rfidController;
    //marcaje=material
    private String marcajeMaterialNombre;
    private String marcajeMaterialStockcode;
    private String marcajeMaterialBin;
    private String marcajeMaterialFechavenc;
    private String marcajeMaterialCantidad;
    //marcaje=bin
    private String marcajeBinBin;
    //spinner
    private ArrayList<String> lectorList;
    private String lectorSelected;

    private TextView tv_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcaje_grabar_tag);

        tv_msg = (TextView) findViewById(R.id.tvMarcajeGrabarTagError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        receiveDataFromIntent();

        //NFC
        configureNFCAdapter();
        //RFID
        initRFIDcontroller();

        configureLectorList();
        configureSpinnerLector();

        configureButtonAtras();

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        int SCAN_BUTTON_ID = 139;
        int SOUND_DOWN_BUTTON_ID = 25;
        int SCAN_TRIGGER_HH = 280;

        if ((keyCode == SCAN_BUTTON_ID || keyCode == SOUND_DOWN_BUTTON_ID || keyCode == SCAN_TRIGGER_HH)) {
            //AGREGAR TAG
            switch (lectorSelected) {
                case "RFID": {
                    tv_msg.setText(R.string.leyendo);

                    testRFID(12, 2, 15, "Yes");

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("tipoMarcaje", tipoMarcaje);
                    setResult(Activity.RESULT_OK, returnIntent);

                    //enviar lectura pot http(diferenciar bin/marcaje)

                    finish();

                    break;
                }
                case "QR": {
                    tv_msg.setText(R.string.leyendo);

                    openQRreading();

                    //enviar lectura pot http(diferenciar bin/marcaje)

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

    private void configureButtonAtras() {
        Button btn_atras = (Button) findViewById(R.id.btnMarcajeGrabarTagAtras);
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                tv_msg.setText(R.string.esperando_lectura);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    //RFID METHODS
    private void initRFIDcontroller(){
        rfidController = new RFIDController(MarcajeGrabarTagActivity.this);

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
        openQRLector();
        /*
        if (hasQRbuiltIn){
            openQRLector();
        } else {
            openCamQR();
        }*/

    }
    //QR
    protected void openCamQR(){
        Intent intent = new Intent(MarcajeGrabarTagActivity.this, QrCamActivity.class);
        startActivityForResult(intent, 1);
    }
    //QR HH
    protected void openQRLector(){
        Intent intent = new Intent(MarcajeGrabarTagActivity.this, QrBuiltInActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {//4 for QR unitaria qr cam
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("tipoMarcaje","material");
                //MARCAR data.getStringExtra("ID") y si hay exito retornar con result_ok
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

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
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("tipoMarcaje","material");
                    returnIntent.putExtra("ID",id);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(MarcajeGrabarTagActivity.this,"Error al leer TAG NFC",Toast.LENGTH_LONG).show();
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
            mNfcAdapter.disableForegroundDispatch(MarcajeGrabarTagActivity.this);
        }
    }
}