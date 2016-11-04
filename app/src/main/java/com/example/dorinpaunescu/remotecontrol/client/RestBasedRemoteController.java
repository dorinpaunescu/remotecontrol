package com.example.dorinpaunescu.remotecontrol.client;

import android.view.View;
import android.widget.TextView;

import com.example.dorinpaunescu.remotecontrol.adapters.Constants;
import com.example.dorinpaunescu.remotecontrol.client.rest.RobotControlRestProtocol;
import com.example.dorinpaunescu.remotecontrol.properties.PropConfigHolder;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    TextView observer;

    public RestBasedRemoteController(TextView observer){
        this.observer = observer;
        initRestService();
    }

    @Override
    public JSONObject sendCommand(final JSONObject payload) {

        Callback<Response> callback = new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                int status = response.getStatus();

                System.out.println("Status: " + status);

                TypedByteArray body = (TypedByteArray) response.getBody();
                String outputStr = new String(body.getBytes());
                System.out.print(outputStr);

                JSONObject resp = new JSONObject();
                try {
                    resp.put("status", response.getStatus());
                    resp.put("payload", payload);
                    if(observer != null) {
                        observer.setText(resp.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("Error");
                JSONObject resp = new JSONObject();
                try {
                    resp.put("status", error.getResponse().getStatus());
                    resp.put("error", error.getMessage());
                    resp.put("payload", payload);
                    if(observer != null) {
                        observer.setText(resp.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        communicatorInterface.sendCommand(payload, callback);

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

        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new ApiRequestInterceptor(user))
                .setEndpoint(properties.get(Constants.URL))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .build();
        communicatorInterface = restAdapter.create(RobotControlRestProtocol.class);

    }
}
