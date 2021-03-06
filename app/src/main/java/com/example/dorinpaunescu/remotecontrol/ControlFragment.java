package com.example.dorinpaunescu.remotecontrol;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dorinpaunescu.remotecontrol.client.RemoteControllerProtocol;
import com.example.dorinpaunescu.remotecontrol.envelope.MovementEnvelope;
import com.example.dorinpaunescu.remotecontrol.factory.RemoteControllerFactory;
import com.example.dorinpaunescu.remotecontrol.factory.ResourceManagerFactory;
import com.example.dorinpaunescu.remotecontrol.factory.ResourceManagerProducer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ControlFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private OnFragmentInteractionListener mListener;

    private MovementEnvelope lastMovement;

    public ControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
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

    /**
     * Receiving speech input
     * */

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_control, container, false);

       /* Button buttonDown = (Button)view.findViewById(R.id.buttonMoveDown);
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clientId = MqttClient.generateClientId();
                MqttAndroidClient client =
                        new MqttAndroidClient(getContext(), "tcp://broker.hivemq.com:1883",
                                clientId);

                try {
                    IMqttToken token = client.connect();
                    token.setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // We are connected
                            System.out.println("We are connected");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Something went wrong e.g. connection timeout or firewall problems
                            System.out.println("Connection failure");

                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });*/

        AsyncTask<String, String, String> asyncImgRecognition = new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {


                return null;
            }
        };

       asyncImgRecognition.execute();

        final TextView tView = (TextView)view.findViewById(R.id.textViewOutput);
        final Button buttonUp = (Button)view.findViewById(R.id.buttonMoveUp);
        final Button buttonLeft = (Button)view.findViewById(R.id.buttonMoveLeft);
        final Button buttonRight = (Button)view.findViewById(R.id.buttonMoveRight);
        final Button buttonDown = (Button)view.findViewById(R.id.buttonMoveDown);
        final Button buttonStop = (Button)view.findViewById(R.id.stopButton);

        final class AsyncExecutor extends AsyncTask<String, String, String> {

            View view;

            public AsyncExecutor(View observer){
                this.view = observer;
            }

            @Override
            protected String doInBackground(String... params) {

                try {

                    ResourceManagerFactory factoryManager = ResourceManagerProducer.getFactoryManager(ResourceManagerProducer.REMOTE_CONTROLLER_TYPE);
                    RemoteControllerProtocol remoteController = factoryManager.createRemoteController(RemoteControllerFactory.REST_BASED_REMOTE_CONTROLLER);

                    if (buttonLeft.equals(view)) {
                        if (lastMovement != null) {
                            int lastLeft = Integer.parseInt(lastMovement.left);
                            int lastRight = Integer.parseInt(lastMovement.right);

                            int newLeft = 0;
                            int newRight = lastRight - 55;

                            if (lastLeft == 0 && lastRight == 0) {
                                newLeft = 0;
                                newRight = 200;
                            }

                            if (lastLeft < 0 && lastRight < 0) {
                                newLeft = lastLeft + 55;
                                newRight = 0;
                            }

                            MovementEnvelope leftEnvelope = new MovementEnvelope("" + newLeft, "" + newRight);
                            remoteController.sendCommand(leftEnvelope, tView);

                            if (lastLeft == 0 && lastRight == 0) {
                                return null;
                            }
                        }

                        try {
                            System.out.println("Sleep for 200 ms");
                            Thread.sleep(200);
                            System.out.println("Resume ...");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        remoteController.sendCommand(lastMovement, tView);

                        return null;
                    }

                    if (buttonRight.equals(view)) {
                        if (lastMovement != null) {
                            int lastLeft = Integer.parseInt(lastMovement.left);
                            int lastRight = Integer.parseInt(lastMovement.right);

                            int newLeft = lastLeft - 55;
                            int newRight = 0;

                            if (lastLeft == 0 && lastRight == 0) {
                                newLeft = 200;
                                newRight = 0;
                            }

                            if (lastLeft < 0 && lastRight < 0) {
                                newLeft = 0;
                                newRight = lastRight + 55;
                            }

                            MovementEnvelope leftEnvelope = new MovementEnvelope("" + newLeft, "" + newRight);
                            remoteController.sendCommand(leftEnvelope, tView);

                            if (lastLeft == 0 && lastRight == 0) {
                                return null;
                            }
                        }

                        try {
                            System.out.println("Sleep for 200 ms");
                            Thread.sleep(200);
                            System.out.println("Resume ...");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        remoteController.sendCommand(lastMovement,tView);

                        return null;
                    }

                    if (buttonUp.equals(view)) {
                        remoteController.sendCommand(new MovementEnvelope("255", "255"), tView);
                        lastMovement = new MovementEnvelope("255", "255");
                    }

                    if (buttonDown.equals(view)) {
                        remoteController.sendCommand(new MovementEnvelope("-255", "-255"), tView);
                        lastMovement = new MovementEnvelope("-255", "-255");
                    }

                    if (buttonStop.equals(view)) {
                        remoteController.sendCommand(new MovementEnvelope("0", "0"), tView);
                        lastMovement = new MovementEnvelope("0", "0");
                    }
                }catch(final Throwable ex) {
                    Activity activity = (Activity) getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tView.setText("URL is blank; Check settings section -> " + ex.getLocalizedMessage());
                        }
                    });
                    ex.printStackTrace();
                }
                return null;
            }
        }

        final class ControllerOnClickListener implements View.OnClickListener{
            public ControllerOnClickListener(){
                lastMovement = new MovementEnvelope("0", "0");
            }

            @Override
            public void onClick(View view) {
                try {
                    AsyncExecutor asynExec = new AsyncExecutor(view);
                    AsyncTask<String, String, String> execute = asynExec.execute();
                }catch (Throwable ex) {
                    ex.printStackTrace();
                }

            }
        }

        ControllerOnClickListener contOnClick = new ControllerOnClickListener();

        buttonUp.setOnClickListener(contOnClick);
        buttonDown.setOnClickListener(contOnClick);
        buttonLeft.setOnClickListener(contOnClick);
        buttonRight.setOnClickListener(contOnClick);
        buttonStop.setOnClickListener(contOnClick);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
