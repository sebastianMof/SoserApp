package com.acruxingenieria.soserapp.Consulta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acruxingenieria.soserapp.R;
import com.acruxingenieria.soserapp.RFID.RFIDController;

import java.util.ArrayList;


public class ConsultaMasivaFragment extends Fragment {

    private View savedView;

    private ArrayList<String> idLecturaMasiva = new ArrayList<>();

    //RFID
    ArrayList<String> RFID_IDs = new ArrayList<>();
    RFIDController rfidController;

    public ConsultaMasivaFragment() {
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
        savedView = inflater.inflate(R.layout.fragment_consulta_masiva, container, false);
        setSavedView(savedView);

        initRFIDcontroller();

        TextView tv_msg = (TextView) getSavedView().findViewById(R.id.tvConsultaMasivaError);
        tv_msg.setMovementMethod(new ScrollingMovementMethod());

        return savedView;
    }


    public View getSavedView() {
        return savedView;
    }

    private void setSavedView(View savedView) {
        this.savedView=savedView;
    }

    public boolean read() {
        idLecturaMasiva = new ArrayList<>();
        RFID_IDs = new ArrayList<>();
        try {
            testRFID(12, 2, 15, "Yes");
            idLecturaMasiva = RFID_IDs;
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;

    }

    public ArrayList<String> getIdLecturaMasiva() {
        return idLecturaMasiva;
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

        if (rfidController.readMultipleUiid() != null){

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
}
