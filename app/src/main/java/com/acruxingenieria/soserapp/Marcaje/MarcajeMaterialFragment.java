package com.acruxingenieria.soserapp.Marcaje;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.acruxingenieria.soserapp.R;

public class MarcajeMaterialFragment extends Fragment {

    View savedView;

    public MarcajeMaterialFragment() {
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
        savedView =inflater.inflate(R.layout.fragment_marcaje_material, container, false);
        setSavedView(savedView);
        configureButtonLeerBin();

        return savedView;
    }

    private void configureButtonLeerBin() {

        Button btn_leer_bin = (Button) getSavedView().findViewById(R.id.btnMarcajeMaterialLeerBin);
        btn_leer_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),MarcajeLeerBinActivity.class);
                startActivity(intent);

            }
        });
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    protected View getSavedView(){
        return savedView;
    }


}
