package com.acruxingenieria.soserapp.QR;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.acruxingenieria.soserapp.R;
import com.rscja.deviceapi.Barcode2D;
import com.rscja.deviceapi.exception.ConfigurationException;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QrBuiltInActivity extends AppCompatActivity {

    Barcode2DWithSoft barcode2DWithSoft=null;
    Barcode2D barcode2D = null;
    private String seldata="ASCII";

    private int SCAN_BUTTON_ID = 139;
    private int SOUND_DOWN_BUTTON_ID = 25;
    private int SCAN_TRIGGER_HH = 280;

    private static final String RUN_FORMAT = "?RUN=";
    private static final String RUN__END_FORMAT = "&type=";

    private String barCode="";
    private String dataCode = "";

    private Context context = null;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_built_in);

        /**Funcion RFID que permite obtener una instancia de la clase barcode2D**/
        barcode2DWithSoft=Barcode2DWithSoft.getInstance();

        checkAndroidVersion();

    }

    public boolean hasQRLector(){
        boolean aux = false;
        try {
            barcode2D=Barcode2D.getInstance();
            aux = barcode2D.open();

        } catch (AssertionError|ConfigurationException e){
            e.printStackTrace();
        }

        return aux;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(barcode2DWithSoft!=null){
            barcode2DWithSoft.stopScan();
            barcode2DWithSoft.close();
        }
        super.onDestroy();
    }

    protected void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkAndRequestPermissions();
        } else {
            if(barcode2DWithSoft!=null){
                new InitTask().execute();
            }else{
                Log.e("ERROR","barcode2DWithSoft Is Null");
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            Toast.makeText(QrBuiltInActivity.this, "Se necesitan permisos",Toast.LENGTH_LONG).show();
            return false;
        }
        if(barcode2DWithSoft!=null){
            new InitTask().execute();
        }else{
            Log.e("ERROR","barcode2DWithSoft Is Null");
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        if(barcode2DWithSoft!=null){
                            new InitTask().execute();
                        }else{
                            Log.e("ERROR","barcode2DWithSoft Is Null");
                        }
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.e("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Los Permisos de Camara y Almacenamiento son requeridos para realizar la tarea",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Ingrese a Configuración y active los permisos", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    /**
     * Metodo que inicia el lector de codigos
     *
     */
    public void scanBarCode(){

        if(barcode2DWithSoft!=null) {
            barcode2DWithSoft.scan();
            barcode2DWithSoft.setScanCallback(ScanBack);
        }

    }

    /**
     * Metodo que se ejecuta cuando el lector lee un codigo de barras o QR
     *
     * El return de esta funcion es mediante un returnIntent hacia la MainActivity
     */
    public Barcode2DWithSoft.ScanCallback  ScanBack= new Barcode2DWithSoft.ScanCallback(){

        @Override
        public void onScanComplete(int i, int length, byte[] bytes) {

            if (length < 1) {
                //barcode2DWithSoft.close();
                if (length == -1) {
                    Log.e("barCode","Scan cancel");
                } else if (length == 0) {
                    Log.e("barCode","Scan timeout");
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    barcode2DWithSoft.close();
                    finish();
                } else {
                    Log.e("barCode","Scan fail");
                }
            }else{
                barCode="";
                try {
                    barCode = new String(bytes, 0, length, seldata);
                    dataCode = getRutFromQR(barCode);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("ID",dataCode);
                    returnIntent.putExtra("lectorSelected","QR");
                    setResult(RESULT_OK,returnIntent);
                    barcode2DWithSoft.close();
                    finish();

                }
                catch (UnsupportedEncodingException ex)   {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    barcode2DWithSoft.close();
                    finish();
                }
            }

        }
    };

    @SuppressLint("StaticFieldLeak")
    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Log.e("barCode","INIT...");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean reuslt=false;
            if(barcode2DWithSoft!=null) {
                reuslt=  barcode2DWithSoft.open(QrBuiltInActivity.this);
                Log.e("barCode","open="+reuslt);

            }
            return reuslt;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                // interleaved 2 of 5
                barcode2DWithSoft.setParameter(6, 1);
                barcode2DWithSoft.setParameter(22, 0);
                barcode2DWithSoft.setParameter(23, 55);

                scanBarCode();

            }else{
                Toast.makeText(QrBuiltInActivity.this, "Fallo de lectura", Toast.LENGTH_LONG).show();
                Log.e("barCode","FAIL");

            }
        }

    }

    /**
     * Metodo que permite obtener el rut del Codigo QR leído
     *
     * @param rqCode string extraido del codigo QR
     * @return String - rut
     */
    private String getRutFromQR(String rqCode){
        String rut = rqCode;
        if (rqCode.contains("https://")){
            int rutIndex = rqCode.indexOf(RUN_FORMAT);
            int rutEnd = rqCode.indexOf(RUN__END_FORMAT);

            if (  0 > rutIndex || 0 > rutEnd ){
                Toast.makeText(context,"No se escaneo un Carnet válido",Toast.LENGTH_SHORT).show();
                return null;
            }

            rut = rqCode.substring(rutIndex+RUN_FORMAT.length(),rutEnd);
        }else if (rqCode.length()>300){
            rut = getRutOldQR(rqCode);
        }

        return rut;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == SCAN_BUTTON_ID || keyCode == SOUND_DOWN_BUTTON_ID || keyCode == SCAN_TRIGGER_HH) {
            scanBarCode();
        }
        return super.onKeyUp(keyCode, event);
    }


    /**
     * Metodo validador de rut
     *
     * @param rut
     * @return true o false
     */
    public static boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }
            //TODO agregar mansajes o acciones en Exception
        } catch (java.lang.NumberFormatException e) {

        } catch (Exception e) {

        }
        return validacion;
    }


    /**
     * Metodo que permite obtener el rut del Codigo QR leído
     *
     * @param oldCode string extraido del codigo QR
     * @return String - rut
     */
    private String getRutOldQR (String oldCode){
        boolean validate;
        String newCode;

        newCode = oldCode.substring(0,9);
        newCode = newCode.replace(" ","");
        if (newCode.length()==8){
            newCode = (newCode.substring(0,7))+"-"+(newCode.substring(7,8));
            validate=validarRut(newCode);
            if (validate){
                return newCode;
            }
        }
        newCode = (newCode.substring(0,8))+"-"+(newCode.substring(8,9));
        validate=validarRut(newCode);
        if (validate){
            return newCode;
        }else{
            newCode = oldCode.substring(0,8);
            validate=validarRut(newCode);
            if (validate){
                return newCode;
            }
        }

        return newCode;
    }
}
