package com.acruxingenieria.soserapp.Marcaje;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.acruxingenieria.soserapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MarcajeMaterialFragment extends Fragment {

    View savedView;
    private String readedBIN;

    private Calendar startCalendar;
    private EditText et_end_date;

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

        startCalendar = Calendar.getInstance();
        EditText et_end_date = (EditText) savedView.findViewById(R.id.etMarcajeMaterialFechaVenc);

        DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartLabel();
            }
        };

        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(v.getContext(), startDate, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return savedView;
    }

    private void updateStartLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_end_date = savedView.findViewById(R.id.etMarcajeMaterialFechaVenc);
        et_end_date.setText(sdf.format(startCalendar.getTime()));
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

}
