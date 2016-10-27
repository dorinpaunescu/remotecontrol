package com.example.dorinpaunescu.remotecontrol.factory;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public class ResourceManagerProducer {
    public static String REMOTE_CONTROLLER_TYPE = "REMOTE_CONTROLLER_TYPE";

    public static ResourceManagerFactory getFactoryManager(String resourceManager){

        if(resourceManager.equals(REMOTE_CONTROLLER_TYPE)){
            return new RemoteControllerFactory();
        }
        return null;
    }
}
