package com.example.dorinpaunescu.remotecontrol;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dorinpaunescu.remotecontrol.sensors.SensorActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TabAccelerometru.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TabAccelerometru#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabAccelerometru extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TabAccelerometru() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabAccelerometru.
     */
    // TODO: Rename and change types and number of parameters
    public static TabAccelerometru newInstance(String param1, String param2) {
        TabAccelerometru fragment = new TabAccelerometru();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private SensorManager mSensorManager;
    private SensorActivity sensorListener;
    private Sensor accelerometerSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("On create view");

        View view = inflater.inflate(R.layout.fragment_tab_accelerometru, container, false);

        if(sensorListener != null) {
            System.out.println("Recreate text boxes");
            sensorListener.setTxX((TextView) view.findViewById(R.id.textViewX));
            sensorListener.setTxY((TextView) view.findViewById(R.id.textViewY));
            sensorListener.setTxZ((TextView) view.findViewById(R.id.textViewZ));
            sensorListener.setStatus((TextView) view.findViewById(R.id.textStatus));
        }

                // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("On attach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (mSensorManager == null) {
            mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        }

        if (accelerometerSensor == null) {
            accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if(sensorListener == null) {
            sensorListener = new SensorActivity(null, null, null);
        }
    }

    @Override
    public void onDetach() {
        System.out.println("On detachs");
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("Is visible " + isVisibleToUser);



        try {
            if (isVisibleToUser) {

                System.out.println("Register sensor listener");
                if(mSensorManager!= null) {
                    mSensorManager.registerListener(sensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                }
            } else {
                System.out.println("Deregister sensor listener");
                if(mSensorManager!=null) {
                    mSensorManager.unregisterListener(sensorListener);
                }
            }
        }catch (Throwable e) {
            System.out.println("Critical exception in accelerometer view");
            e.printStackTrace();
        }
    }
}
