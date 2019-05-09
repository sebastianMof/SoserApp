package com.acruxingenieria.soserapp.Marcaje;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.acruxingenieria.soserapp.BodegaActivity;
import com.acruxingenieria.soserapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MarcajeMaterialFragment extends Fragment {

    View savedView;
    private String readedBIN;
    public String unitSelected;

    private Calendar endCalendar;
    private EditText et_end_date;

    private ArrayList<String> positionsList;

    public MarcajeMaterialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        savedView =inflater.inflate(R.layout.fragment_marcaje_material, container, false);
        setSavedView(savedView);
        configureButtonLeerBin();

        endCalendar = Calendar.getInstance();
        EditText et_end_date = (EditText) savedView.findViewById(R.id.etMarcajeMaterialFechaVenc);

        DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartLabel();
            }
        };

        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(v.getContext(), startDate, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        configurePositionList();
        configureSpinnerPositions();


        return savedView;
    }

    private void updateStartLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_end_date = savedView.findViewById(R.id.etMarcajeMaterialFechaVenc);
        et_end_date.setText(sdf.format(endCalendar.getTime()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText et_bin = (EditText) getSavedView().findViewById(R.id.etMarcajeMaterialBin);
        et_bin.setText(readedBIN);
    }

    private void configureButtonLeerBin() {

        Button btn_leer_bin = (Button) getSavedView().findViewById(R.id.btnMarcajeMaterialLeerBin);
        btn_leer_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),MarcajeLeerBinActivity.class);
                Objects.requireNonNull(getActivity()).startActivityForResult(intent,2);

            }
        });
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    protected View getSavedView(){
        return savedView;
    }

    public void setReadedBIN(String readedBIN){
        this.readedBIN=readedBIN;
    }

    private void configurePositionList() {
        positionsList = new ArrayList<>();
        //positionsList.add("POSICION_EJEMPLO");
        positionsList.add("kg");
        positionsList.add("lt");
        positionsList.add("oz");
    }

    private void configureSpinnerPositions() {
        Spinner spn_pos = (Spinner) savedView.findViewById(R.id.spinnerMarcajeMaterialUnidad);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, positionsList){

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
                unitSelected =  (String) adapterView.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public String getUnitSelected(){
        return unitSelected;
    }

}
