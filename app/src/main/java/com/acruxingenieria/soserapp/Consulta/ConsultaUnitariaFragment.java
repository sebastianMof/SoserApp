package com.acruxingenieria.soserapp.Consulta;

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

import com.acruxingenieria.soserapp.R;

import java.util.ArrayList;

public class ConsultaUnitariaFragment extends Fragment {

    View savedView;

    private String idLecturaUnitaria;

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
                idLecturaUnitaria = "ID_readed_by_RFID";
                return true;
            case "QR":
                idLecturaUnitaria = "ID_readed_by_QR";
                return true;
            case "NFC":
                idLecturaUnitaria = "ID_readed_by_NFC";
                return true;
        }

        return false;

    }

    public String getIdLecturaUnitaria(){
        return idLecturaUnitaria;
    }

}
