package com.example.dorinpaunescu.remotecontrol.client;

import com.example.dorinpaunescu.remotecontrol.adapters.Constants;
import com.example.dorinpaunescu.remotecontrol.client.rest.RobotControlRestProtocol;
import com.example.dorinpaunescu.remotecontrol.properties.PropConfigHolder;

import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public class RestBasedRemoteController implements RemoteControllerProtocol {

    RobotControlRestProtocol communicatorInterface;

    public RestBasedRemoteController(){
        initRestService();
    }

    @Override
    public JSONObject sendCommand(JSONObject payload) {

        Callback<Response> callback = new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                int status = response.getStatus();

                System.out.println("Status: " + status);

                TypedByteArray body = (TypedByteArray) response.getBody();
                String outputStr = new String(body.getBytes());
                System.out.print(outputStr);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
        communicatorInterface.sendCommand(null, callback);

        return null;
    }

    @Override
    public String testGet() {
        Callback<Response> callback = new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                int status = response.getStatus();

                System.out.println("Status: " + status);

                TypedByteArray body = (TypedByteArray) response.getBody();
                String outputStr = new String(body.getBytes());
                System.out.print(outputStr);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
        communicatorInterface.testGet(callback);

        return null;
    }

    public void initRestService(){

        Map<String, String> properties = PropConfigHolder.getInstance().getProperties();

        User user = new User(properties.get(Constants.USERNAME), properties.get(Constants.PASSWORD));

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new ApiRequestInterceptor(user))
                .setEndpoint(properties.get(Constants.URL))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        communicatorInterface = restAdapter.create(RobotControlRestProtocol.class);
    }
}
