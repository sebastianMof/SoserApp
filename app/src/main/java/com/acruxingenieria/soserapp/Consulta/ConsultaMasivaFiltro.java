package com.acruxingenieria.soserapp.Consulta;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.Sesion;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ConsultaMasivaFiltro extends AppCompatActivity {

    private Sesion session;

    private CrystalSeekbar rangeSeekbar;

    private TextView tvSeekBarMin;
    private TextView tvSeekBarMax;

    private Calendar startCalendar;
    private Calendar endCalendar;

    private EditText et_start_date;
    private EditText et_end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_masiva_filtro);

        Bundle data = getIntent().getExtras();
        session = (Sesion) data.getParcelable("session");

        rangeSeekbar = (CrystalSeekbar) findViewById(R.id.rangeSeekbar);

        tvSeekBarMin = (TextView) findViewById(R.id.tvConsultaMasivaFiltroSeekbarMin);
        tvSeekBarMax = (TextView) findViewById(R.id.tvConsultaMasivaFiltroSeekbarMax);

        tvSeekBarMax.setText("100");
        rangeSeekbar.setMaxValue((float) 100);
        tvSeekBarMin.setText("0");
        rangeSeekbar.setMinValue((float) 0);
        rangeSeekbar.setMinStartValue((float) 100).apply();

        // set listener
        rangeSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                tvSeekBarMin.setText(String.valueOf(minValue));
            }
        });

        // set final value listener
        rangeSeekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                Log.d("CRS=>", String.valueOf(value));
            }
        });

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        EditText et_start_date = (EditText) findViewById(R.id.etConsultaMasivaFiltroStartDate);
        EditText et_end_date = (EditText) findViewById(R.id.etConsultaMasivaFiltroEndDate);

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

        DatePickerDialog.OnDateSetListener endDate= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndLabel();
            }
        };


        et_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(v.getContext(), startDate, startCalendar
                        .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(v.getContext(), endDate, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateStartLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_start_date = findViewById(R.id.etConsultaMasivaFiltroStartDate);
        et_start_date.setText(sdf.format(startCalendar.getTime()));
    }

    private void updateEndLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_end_date = findViewById(R.id.etConsultaMasivaFiltroEndDate);
        et_end_date.setText(sdf.format(endCalendar.getTime()));
    }



}
