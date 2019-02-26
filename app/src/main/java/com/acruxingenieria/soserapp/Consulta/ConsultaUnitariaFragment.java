package com.acruxingenieria.soserapp.Consulta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.acruxingenieria.soserapp.QR.QrCamActivity;
import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.RFID.RFIDController;

import java.util.ArrayList;
import java.util.Objects;

public class ConsultaUnitariaFragment extends Fragment {

    View savedView;

    private String idLecturaUnitaria;

    //RFID
    ArrayList<String> RFID_IDs = new ArrayList<>();
    RFIDController rfidController;

    private ArrayList<String> lectorList;
    private String lectorSelected;

    public ConsultaUnitariaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        savedView = inflater.inflate(R.layout.fragment_consulta_unitaria, container, false);
        setSavedView(savedView);

        TextView tv_msg = (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        initRFIDcontroller();

        configureLectorList();
        configureSpinnerLector();

        return savedView;
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    public View getSavedView(){
        return savedView;
    }

    public void showError() {
        TextView tv_msg = (TextView) getSavedView().findViewById(R.id.tvConsultaUnitariaError);
        tv_msg.setText(R.string.lectura_fallida);
    }

    private void configureLectorList() {
        lectorList = new ArrayList<>();
        lectorList.add("RFID");
        lectorList.add("QR");
        lectorList.add("NFC");

    }

    private void configureSpinnerLector() {
        Spinner spn_pos = (Spinner) getSavedView().findViewById(R.id.spinnerConsultaUnitaria);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, lectorList){

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

    //METODO QUE RETORNA SI LA LECTURA ES EXITOSA, EN CASO DE EXITO GUARDA EL ID LEIDO
    public boolean read(){
        switch (lectorSelected) {
            case "RFID":
                testRFID(12, 2, 15, "Yes");
                if (RFID_IDs.size()>=1)
                    idLecturaUnitaria = RFID_IDs.get(0);
                else idLecturaUnitaria = null;
                return true;
            case "QR":
                idLecturaUnitaria = "ID_readed_by_QR";
                Intent intent = new Intent(getActivity(), QrCamActivity.class);
                Objects.requireNonNull(getActivity()).startActivityForResult(intent, 4);

                return true;
            case "NFC":
                return true;
        }

        return false;

    }

    public String getIdLecturaUnitaria(){
        return idLecturaUnitaria;
    }

    public void setIdLecturaUnitaria(String idLecturaUnitaria){
        this.idLecturaUnitaria=idLecturaUnitaria;
    }

    //RFID METHODS
    private void initRFIDcontroller(){
        rfidController = new RFIDController(getActivity());

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

            testBeep();
        }else {
            // PRINT NO SE ENCONTRARON TAGS
        }
    }

    public String getLectorSelected(){
        return lectorSelected;
    }

}
