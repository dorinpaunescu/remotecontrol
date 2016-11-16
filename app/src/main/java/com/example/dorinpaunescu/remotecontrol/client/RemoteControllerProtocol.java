package com.example.dorinpaunescu.remotecontrol.client;

import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public interface RemoteControllerProtocol {
    JSONObject sendCommand(Object payload, View observer);

    JSONObject sendAccelerometerDate(Object payload, View observer);

    String testGet();

    public void close();
}
