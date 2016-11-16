package com.example.dorinpaunescu.remotecontrol.client;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public interface RemoteControllerProtocol {
    JSONObject sendCommand(Object payload);

    JSONObject sendAccelerometerDate(Object payload);

    String testGet();

    public void close();
}
