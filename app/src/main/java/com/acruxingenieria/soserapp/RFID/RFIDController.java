package com.acruxingenieria.soserapp.RFID;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import com.acruxingenieria.soserapp.R;
import com.rscja.deviceapi.RFIDWithUHF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class RFIDController {

    /**
     *
     * Se instancia la libreria RFIDWithUHF, previamente implementada en build.gradle
     *
     */
    private RFIDWithUHF mReader = null;
    private static Context context;
    long timeOutCount = 2000000000;
    public double filterValue = 15;
    public String toggleCount;
    private ArrayList<UUID> UUIDList = new ArrayList<>();

    public boolean beepEnable = false;

    public RFIDController (Context context){

        this.context=context;

        try {
            /**
             * Funcion RFID que permite obtener una instancia de la clase RFIDWithUHF
             */
            mReader = RFIDWithUHF.getInstance();

        } catch (Exception ex) {
            Log.e("ERROR","mReader getInstance Fail "+ex.toString());
        }

        if (mReader != null) {
            /**Se hace una nueva inicialización de la Clase RFID con el Hardware del dispositivo**/
            new InitTask().execute();

        }

    }

    public RFIDController (){

        try {
            /**Funcion RFID que permite obtener una instancia de la clase RFIDWithUHF**/
            mReader = RFIDWithUHF.getInstance();

        } catch (Exception ex) {
            Log.e("ERROR","mReader getInstance Fail "+ex.toString());
        }

        if (mReader != null) {
            /**Se hace una nueva inicialización de la Clase RFID con el Hardware del dispositivo**/
            new InitTask().execute();

        }

    }

    /**
     * Metodo que permite leer uuid de un tag
     *
     * @param power int que representa la potencia de la antena RFID
     * @return String - uuid del tag
     */
    public String readSingleUiid (int power){
        String dataRFID;
        /**Funcion que permite establecer la potencia de la antera RFID (Queda guardada en Hardware)**/
        if  (mReader.setPower(power)){
            Log.e("INFO","power setted to: "+power);
        }
        dataRFID=readSingleUiid();
        return dataRFID;
    }

    /**
     * Metodo que permite leer uuid de un tag
     *
     * @return String - uuid del tag
     */
    public String readSingleUiid(){
        Log.e("INFO","INIT readSingleUiid ");
        String uiid = null;
        String dataRFID = null;
        List<String> ListRead = null;

        /**Metodo que lee uiid de los tag y los entrega en una lista ordenados según Rssi (intensidad de señal de los tag)**/
        ListRead = readMultipleUiidRssi();

        /**Se setea la variable uuid con el objeto con indice 0**/
        try {
            uiid = ListRead.get(0);
        }catch (Exception ex){
            Log.e("EXCEPTION","uuid = ListRead.get(0); "+ex.toString());
        }

        return uiid;
    }

    /**
     * Metodo que permite leer masivamente los uuid de los tags
     *
     * @param power int que representa la potencia de la antena RFID
     * @return List <String> - uuid de los tag
     */
    public List<String> readMultipleUiid(int power){
        List<String> ListRFID = null;

        /**Funcion que permite establecer la potencia de la antera RFID (Queda guardada en Hardware)**/
        if  (mReader.setPower(power)){
            Log.e("INFO","power setted to: "+power);
        }

        ListRFID = readMultipleUiid();

        return ListRFID;
    }

    /**
     * Metodo que permite leer masivamente los uuid de los tags
     *
     * @return List <String> - uuid de los tag
     */
    public List<String> readMultipleUiid (){

        Log.e("INFO","INIT readMultipleUiid ");
        List <String> ListRead = null;
        List <Object> RFIDList = null;
        String dataRFID = null;

        /**Metodo que lee uiid de los tag y los entrega en una lista ordenados según Rssi (intensidad de señal de los tag)**/
        ListRead = readMultipleUiidRssi();

        return ListRead;
    }


    private class InitTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Modificar para mostrar por pantala
            Log.e("RFID","Init...");

        }

        @Override
        protected Boolean doInBackground(String... params) {
            /**Se abre y conecta los servicios con hardware**/
            return mReader.init();

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            /**Si la inicialización no se realiza correctamente, se ejecutan los comandos de onPostExecute**/
            /**Tambien entra al onPostExecute cuando ya hay una instancia de RFID valida funcionando**/
            if (!result){
                Log.e("RFID","Init fail");

            }
        }
    }

    public ArrayList<UUID> getArrayList() {
        return this.UUIDList;
    }


    public void setTimeOutCount(long timeOutCount) {
        this.timeOutCount = timeOutCount*1000000000;
    }


    /**
     * Metodo que lee los uuid de los tags y los ordena segun nro de conteo y rssi
     *
     * @param
     * @return List <String> - uuid de los tag
     */
    private List<String> readMultipleUiidRssi(){
        Log.i("INFO","Inicio metodo readMultipleUuidRssi");
        long st = System.nanoTime();
        //ArrayList<UUID> UUIDList = new ArrayList<>();
        List<String> UiidList = new ArrayList<String>();
        String [] res ;
        UUID uuid;
        /**Funcion que abre el recurso RFID para comenzar a recibir informacion de los tag**/
        if (mReader.startInventoryTag((byte) 0, (byte) 0)) {
            while( (System.nanoTime() - st ) < timeOutCount){
                /**Funcion que devuelve el uuid y la intensidad de señal del tag en un String []**/
                res = mReader.readTagFromBuffer();

                if (res != null) {
                    res[2]= res[2].replace(",",".");
                    res[1] = mReader.convertUiiToEPC(res[1]);

                    uuid = findUsingIterator( res[1], UUIDList);
                    if (uuid != null) {
                        uuid.setCount(uuid.getCount()+1);
                        uuid.setRssi(Double.parseDouble(res[2]));
                        uuid.setRssiSum(Double.parseDouble(res[2]));
                        Log.i("TAG RECONTADO",uuid.toString() );

                    } else {
                        /**Si el uuid no existe en la lista, se agrega**/
                        UUIDList.add(new UUID(res[1], Double.parseDouble(res[2])));
                        Log.i("TAG LEIDO",res[1]+"   "+res[2]);
                    }
                }else{
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("Excepcion:", " " + e.toString());
                    }
                }
            }
        } else {
            Log.e("ERROR","Error en InventoryTag");
        }
        /**Funcion que detiene el recurso RFID previamente iniciado con mReader.startInventoryTag**/
        mReader.stopInventory();
        if (UUIDList.size() > 0) {
            //Log.i("Lista Desordenada",UUIDList.toString());
            /**Metodo que ordena la lista de uiid segun la intenssidad de señal de los tags**/
            Collections.sort(UUIDList, new Comparator<UUID>() {
                @Override
                public int compare(UUID p1, UUID p2) {
                    // Aqui esta el truco, ahora comparamos p2 con p1 y no al reves como antes
                    return new Double(p2.getRssiProm()).compareTo(new Double(p1.getRssiProm()));
                }
            });

            if (toggleCount.equalsIgnoreCase("Sí") || toggleCount.equalsIgnoreCase("Yes")) {
                Log.i("INFO", "Ordenado por Count");
                Collections.sort(UUIDList, new Comparator<UUID>() {
                    @Override
                    public int compare(UUID p1, UUID p2) {
                        // Aqui esta el truco, ahora comparamos p2 con p1 y no al reves como antes
                        //return new Double(p2.getRssi()).compareTo(new Double(p1.getRssi()));
                        return new Double(p2.getCount()).compareTo(new Double(p1.getCount()));
                    }
                });
            }

            double filtroCount = (UUIDList.get(0).getCount() * filterValue / 100);

            Iterator<UUID> itrArrayList = UUIDList.iterator();
            UUID next;
            while (itrArrayList.hasNext()) {
                next = itrArrayList.next();
                if (next.getCount() >= filtroCount) {
                    UiidList.add(next.getUuid());
                    Log.e("TAG filtrado", next.toString());
                } else {
                    itrArrayList.remove();
                    Log.e("TAG NO filtrado", next.toString());
                }
            }

        }else
            UiidList = null;

        return UiidList;
    }

    private UUID findUsingIterator( String uuidName, List<UUID> UUIDList) {
        for (UUID uuid : UUIDList) {
            if (uuid.getUuid().equals(uuidName)) {
                return uuid;
            }
        }
        return null;
    }

    public class UUID {

        private String uuid;
        private double rssi = 0;
        private int count = 0;
        private double rssiSum;


        public UUID(String uuid, double rssi) {
            this.uuid = uuid;
            this.rssi = rssi;
            this.count = 1;
            this.rssiSum = rssi;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public double getRssi() {
            return rssi;
        }

        public void setRssi(double rssi) {
            this.rssi = rssi;
        }

        public void setRssiSum(double rssi) {
            this.rssiSum += rssi;
        }

        public double getRssiProm(){
            return this.rssiSum/this.count;
        }

        public int getCount() { return count; }

        public void setCount(int count) { this.count = count; }

        @Override
        public String toString() {
            //return this.getUuid() + "  -  " + this.getRssi() + "  -  " + this.getRssiProm() + "  -  " + this.getCount() ;
            return this.getUuid();
        }
    }

    /**
     * Metodo que permite establecer el poder de antena de la Handheld
     *
     * @param power int que representa la potencia de la antena RFID
     * @return boolean , true indicando que fue establecida correctamente
     */
    public boolean setPower (int power){

        boolean isSetted = false;

        isSetted = mReader.setPower(power);

        if  (isSetted){
            Log.e("INFO","power setted to: "+power);
        }

        return isSetted;
    }

    /**
     * Metodo que permite obtener el valor de la potencia que posee la Handheld
     *
     * @return int - valor de la potencia
     */
    public int getPower (){

        return mReader.getPower();

    }

    public String readSingleuiid (String filter){
        String uiidData = null;

        try{
            uiidData = mReader.readData("00000000", RFIDWithUHF.BankEnum.UII,0,1,filter, RFIDWithUHF.BankEnum.UII,1,7);
        }catch (Exception ex){
            Log.e("EXCEPTION",ex.toString());
        }

        Log.e("DATA",uiidData);

        return uiidData;
    }

    /**
     * Metodo que establece la frecuencia de la HandHeld
     *
     * @param workingMode int que representa el numero de Standard de la Frecuencia
     *
     * @return boolean , true indicando que fue establecida correctamente
     */
    public boolean setFrequency(int workingMode){
        /**China Standard plus(920~925MHz) = 0**/
        /**ETSI Standard(865~868MHz) = 1**/
        /**United States Standard(902~928MHz) = 2**/
        /**Fixed Frequency(915MHz) = 3**/
        /**Korea = 4**/
        /**Japan = 5**/
        /**Morocco = 6**/
        /**China Standard(840~845MHz) = 7**/
        /**China Standard(920~925MHz) = 8**/
        boolean isSetted=false;

        isSetted = mReader.setFrequencyMode((byte)workingMode);

        if (isSetted){
            Log.e("INFO","Frequency setted on: "+workingMode+ " - "+getStringMode(workingMode));
        }else{
            Log.e("INFO","Error on setting FrequencyMode");
        }

        return isSetted;
    }

    /**
     * Metodo que permite obtener la Frecuancia en la que está funcionando la Handheld
     *
     * @return int - valor de la Frecuencia
     */
    public String getFrequency (){
        String workingMode;
        int frequencyValue;

        frequencyValue = mReader.getFrequencyMode();

        workingMode = getStringMode(frequencyValue);

        return workingMode;
    }

    private String getStringMode (int workingMode){
        /**China Standard plus(920~925MHz) = 0**/
        /**ETSI Standard(865~868MHz) = 1**/
        /**United States Standard(902~928MHz) = 2**/
        /**Fixed Frequency(915MHz) = 3**/
        /**Korea = 4**/
        /**Japan = 5**/
        /**Morocco = 6**/
        /**China Standard(840~845MHz) = 7**/
        /**China Standard(920~925MHz) = 8**/
        String infoMode = null;

        switch (workingMode){
            case 0 :
                infoMode = "China Standard plus(920~925MHz)";
                break;
            case 1:
                infoMode = "ETSI Standard(865~868MHz)";
                break;
            case 2:
                infoMode = "United States Standard(902~928MHz)";
                break;
            case 3:
                infoMode = "Fixed Frequency(915MHz)";
                break;
            case 4:
                infoMode = "Korea";
                break;
            case 5:
                infoMode = "Japan";
                break;
            case 6:
                infoMode = "Morocco";
                break;
            case 7:
                infoMode = "China Standard(840~845MHz)";
                break;
            case 8:
                infoMode = "China Standard(920~925MHz)";
                break;
            default:
                infoMode = "ERROR";

        }

        return infoMode;
    }

    public void doBeep (){
        if(beepEnable){
            MediaPlayer.create(context, R.raw.beep).start();
        }else{
            Log.e("INFO","Beep sound is not enabled "+beepEnable);
        }
    }

}
