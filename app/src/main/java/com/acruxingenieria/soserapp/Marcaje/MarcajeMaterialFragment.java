package com.acruxingenieria.soserapp.Marcaje;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.acruxingenieria.soserapp.R;

import java.util.Objects;

public class MarcajeMaterialFragment extends Fragment {

    View savedView;
    private String readedBIN;

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
        return savedView;
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
