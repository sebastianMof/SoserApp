package com.acruxingenieria.soserapp.Marcaje;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acruxingenieria.soserapp.R;

public class MarcajeBinFragment extends Fragment {

    private View savedView;

    public MarcajeBinFragment() {
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
        savedView =inflater.inflate(R.layout.fragment_marcaje_bin, container, false);
        return savedView;

    }

    public View getSavedView() {
        return savedView;
    }
}
