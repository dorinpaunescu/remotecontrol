package com.example.dorinpaunescu.remotecontrol.client;

import org.json.JSONObject;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public interface RemoteControllerProtocol {
    JSONObject sendCommand(Object payload);

    String testGet();
}
