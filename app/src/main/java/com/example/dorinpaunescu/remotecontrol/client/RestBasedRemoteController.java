package com.example.dorinpaunescu.remotecontrol.client;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dorinpaunescu.remotecontrol.MainActivity;
import com.example.dorinpaunescu.remotecontrol.adapters.Constants;
import com.example.dorinpaunescu.remotecontrol.client.rest.RobotControlRestProtocol;
import com.example.dorinpaunescu.remotecontrol.properties.PropConfigHolder;
import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    Activity activity;

    public RestBasedRemoteController(TextView observer){
        this.observer = observer;
        if(observer != null) {
            activity = getActivity(observer);
        }
        initRestService();

    }

    public Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    @Override
    public JSONObject sendCommand(final Object payload) {
        try {
            Response response = communicatorInterface.sendCommand(payload);
            int status = response.getStatus();

            if(status == 200) {

                System.out.println("Status: " + status);

                TypedByteArray body = (TypedByteArray) response.getBody();
                String outputStr = new String(body.getBytes());
                System.out.print(outputStr);

                final JSONObject resp = new JSONObject();
                try {
                    Gson gson = new Gson();
                    resp.put("status", response.getStatus());
                    resp.put("payload", gson.toJson(payload));
                    if (observer != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                observer.setText(resp.toString());
                            }
                        });
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }


                return resp;
            } else {
                System.out.println("Error");
                if(observer != null) {
                    final String localizedMessage = response.getReason();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            observer.setText("Connectivity Error: " + localizedMessage);
                        }
                    });

                }
                JSONObject resp = new JSONObject();
                try {
                    resp.put("status", response.getBody());
                    resp.put("error", response.getReason());
                    resp.put("payload", payload);

                } catch (Throwable e) {
                    e.printStackTrace();
                }

                return resp;
            }
        }catch(Throwable ex){
            if(observer != null) {
                final String localizedMessage = ex.getLocalizedMessage();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        observer.setText("Connectivity Error: " + localizedMessage);
                    }
                });

            }
        }
        return null;
    }

    @Override
    public JSONObject sendAccelerometerDate(final Object payload) {
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
                System.out.println("Error");
                if(observer != null && error != null) {
                    String localizedMessage = error.getLocalizedMessage();
                    observer.setText("Connectivity Error: " + localizedMessage);
                }
                JSONObject resp = new JSONObject();
                try {
                    resp.put("status", error.getBody());
                    resp.put("error", error.getMessage());
                    resp.put("payload", payload);

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("------------------ sendAccelerometer ---------------");
        communicatorInterface.sendAccelerometer(payload, callback);

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
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Response response = onOnIntercept(chain);
                response.body().string();
                response.body().close();
                return response;
            }
        });

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new ApiRequestInterceptor(user))
                .setEndpoint(properties.get(Constants.URL))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(okHttpClient))
                .build();
        communicatorInterface = restAdapter.create(RobotControlRestProtocol.class);



    }

    private com.squareup.okhttp.Response onOnIntercept(Interceptor.Chain chain) throws IOException {
        try {
            com.squareup.okhttp.Response response = chain.proceed(chain.request());
            String content = "Successfull";
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), content)).build();
        }
        catch (Throwable exception) {
            System.out.println("---------------------FATAL ERROR------------------");
            System.out.println(exception.getMessage());
            System.out.println(exception.getLocalizedMessage());
            System.out.println("---------------------FATAL ERROR END------------------");
            throw exception;
        }
    }
}
