package com.example.dorinpaunescu.remotecontrol.client.rest;

import org.json.JSONObject;

import java.util.Objects;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public interface RobotControlRestProtocol {
    // This method is used for "POST"
    //this is synchronous operation
    @Headers({
            "Content-Type: application/json"
    })
    @POST("/send/command/robot")
    Response sendCommand(@Body Object payload);

    @Headers({
            "Content-Type: application/json"
    })
    @POST("/send/accelerometer")
    void sendAccelerometer(@Body Object payload, Callback<Response> serverResponseCallback);

    @GET("/headers")
    void testGet(Callback<Response> serverResponseCallback);
}
