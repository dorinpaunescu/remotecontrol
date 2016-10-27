package com.example.dorinpaunescu.remotecontrol.factory;

import com.example.dorinpaunescu.remotecontrol.client.RemoteControllerProtocol;
import com.example.dorinpaunescu.remotecontrol.client.RestBasedRemoteController;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public class RemoteControllerFactory implements ResourceManagerFactory{

    public static String REST_BASED_REMOTE_CONTROLLER = "REST_BASED_REMOTE_CONTROLLER";

    @Override
    public RemoteControllerProtocol createRemoteController(String remoteControllerType) {

        if(remoteControllerType.equalsIgnoreCase(REST_BASED_REMOTE_CONTROLLER)) {
            return new RestBasedRemoteController();
        }

        return null;
    }
}
