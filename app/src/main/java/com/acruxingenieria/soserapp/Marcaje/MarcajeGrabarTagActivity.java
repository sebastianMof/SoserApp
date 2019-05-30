package com.acruxingenieria.soserapp.Marcaje;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
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

import com.acruxingenieria.soserapp.QR.QrBuiltInActivity;
import com.acruxingenieria.soserapp.QR.QrCamActivity;
import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.RFID.RFIDController;
import com.acruxingenieria.soserapp.Sesion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MarcajeGrabarTagActivity extends AppCompatActivity {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    private GrabarTask grabarTask = null;
    private GrabarBinTask grabarBinTask = null;
    private View mProgressView;
    private View mLayoutView;

    private Sesion session;
    private String tipoMarcaje;
    //NFC
    private NfcAdapter mNfcAdapter;
    //RFID
    ArrayList<String> RFID_IDs = new ArrayList<>();
    RFIDController rfidController;
    //marcaje=material
    private String marcajeMaterialNombre;
    private String marcajeMaterialStockcode;
    private String marcajeMaterialSerialcode;
    private String marcajeMaterialBin;
    private String marcajeMaterialFechavenc;
    private String marcajeMaterialCantidad;
    private String marcajeMaterialUnidad;
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

        mProgressView = findViewById(R.id.marcaje_grabar_tag_progress);
        mLayoutView = findViewById(R.id.ll_marcaje_grabar_tag);

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

            switch (lectorSelected) {
                case "RFID": {
                    tv_msg.setText(R.string.leyendo);
                    testRFID(12, 2, 15, "Yes");
                    if (RFID_IDs.size()>0){
                        if (tipoMarcaje.equals("material")){
                            attemp(RFID_IDs);
                        }else {
                            attempBin(RFID_IDs);
                        }
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

        tipoMarcaje = getIntent().getStringExtra("tipoMarcaje");
        if (tipoMarcaje.equals("material")){
            marcajeMaterialNombre = getIntent().getStringExtra("marcajeMaterialNombre");
            marcajeMaterialStockcode = getIntent().getStringExtra("marcajeMaterialStockcode");
            marcajeMaterialSerialcode = getIntent().getStringExtra("marcajeMaterialSerialcode");
            marcajeMaterialBin = getIntent().getStringExtra("marcajeMaterialBin");
            marcajeMaterialFechavenc = getIntent().getStringExtra("marcajeMaterialFechavenc");
            marcajeMaterialCantidad = getIntent().getStringExtra("marcajeMaterialCantidad");
            marcajeMaterialUnidad = getIntent().getStringExtra("marcajeMaterialUnidad");

        } else if (tipoMarcaje.equals("bin")){
            marcajeBinBin = getIntent().getStringExtra("marcajeBinBin");
        }
        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

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
        boolean hasQrBuiltIn = true;

        if (hasQrBuiltIn){
            openQRLector();
        } else {
            openCamQR();
        }

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

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<String> aux = new ArrayList<String>();
                aux.add(data.getStringExtra("ID"));

                if (tipoMarcaje.equals("material")){
                    attemp(aux);
                    Log.e("TEST","attemp material");
                }else {
                    attempBin(aux);
                }

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
                    ArrayList<String> aux = new ArrayList<String>();
                    aux.add(id);
                    if (tipoMarcaje.equals("material")){
                        attemp(aux);
                    }else {
                        attempBin(aux);
                    }
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


    public class GrabarTask extends AsyncTask<Void, Void, Boolean> {

        JSONArray IDs;

        GrabarTask(ArrayList<String> id) {
            IDs = new JSONArray();
            for (int i=0;i<id.size();i++){
                IDs.put(id.get(i));
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            JSONObject postdata = new JSONObject();

            try {
                postdata.put("serial_code",marcajeMaterialSerialcode);
                postdata.put("bin_code",marcajeMaterialBin);
                postdata.put("tags_id",IDs);
                postdata.put("name",marcajeMaterialNombre);
                postdata.put("stock_code",marcajeMaterialStockcode);
                postdata.put("expire_date",marcajeMaterialFechavenc);
                postdata.put("quantity", Integer.valueOf(marcajeMaterialCantidad));
                postdata.put("measure_unit",marcajeMaterialUnidad);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
            Log.e("BODY",postdata.toString());

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/equipment/")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {

                if (response.body() != null) {

                    String jsonResponse = response.body().string();
                    Log.e("TEST", jsonResponse);

                    try {

                        JSONObject obj = new JSONObject(jsonResponse);

                    } catch (Throwable tx) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + jsonResponse + "\"");
                    }

                }

                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            grabarTask = null;
            showProgress(false);

            if (success){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("tipoMarcaje","material");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            if (!success) {
                TextView error = findViewById(R.id.tvMarcajeGrabarTagError);
                error.setText("Error en la solicitud, revisar datos y tag leído.");
            }

        }

        @Override
        protected void onCancelled() {
            grabarTask = null;
            showProgress(false);
        }
    }

    public class GrabarBinTask extends AsyncTask<Void, Void, Boolean> {

        JSONArray IDs;

        GrabarBinTask(ArrayList<String> id) {
            IDs = new JSONArray();
            for (int i=0;i<id.size();i++){
                IDs.put(id.get(i));
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            OkHttpClient client = new OkHttpClient();

            JSONObject postdata = new JSONObject();

            try {
                postdata.put("bin_code",marcajeBinBin);
                postdata.put("warehouse_code",session.getBodegaSelected());
                postdata.put("tags_id",IDs);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
            Log.e("BODY",postdata.toString());

            final Request request = new Request.Builder()
                    .url("https://node-red-soser-api.mybluemix.net/bins/")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization",session.getToken())
                    .build();

            try (Response response = client.newCall(request).execute()) {

                try {
                    JSONObject aux = new JSONObject(response.body().string());
                    if (aux.getString("ok").equals("true")){
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
                //return response.isSuccessful();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            grabarBinTask = null;
            showProgress(false);

            if (success){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("tipoMarcaje","bin");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
            if (!success) {
                TextView error = findViewById(R.id.tvMarcajeGrabarTagError);
                error.setText("Error en la solicitud, revisar datos y tag leído.");
            }


        }

        @Override
        protected void onCancelled() {
            grabarBinTask = null;
            showProgress(false);
        }
    }

    private void attempBin(ArrayList<String> id){
        if (grabarBinTask != null) {
            return;
        }

        showProgress(true);
        grabarBinTask = new MarcajeGrabarTagActivity.GrabarBinTask(id);
        grabarBinTask.execute((Void) null);
    }

    private void attemp(ArrayList<String> id){
        if (grabarTask != null) {
            return;
        }

        showProgress(true);
        grabarTask = new MarcajeGrabarTagActivity.GrabarTask(id);
        grabarTask.execute((Void) null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLayoutView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLayoutView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



}