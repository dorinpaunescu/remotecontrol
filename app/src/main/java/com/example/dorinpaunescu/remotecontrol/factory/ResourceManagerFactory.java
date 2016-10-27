package com.example.dorinpaunescu.remotecontrol.factory;

import com.example.dorinpaunescu.remotecontrol.client.RemoteControllerProtocol;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public interface ResourceManagerFactory {
    public RemoteControllerProtocol createRemoteController(String remoteControllerType);
}
