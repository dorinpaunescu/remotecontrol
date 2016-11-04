package com.example.dorinpaunescu.remotecontrol.client.rest;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by dorin.paunescu on 10/27/2016.
 */
public interface RobotControlRestProtocol {
    // This method is used for "POST"
    @POST("/testpost")
    void sendCommand(@Body JSONObject payload,  Callback<Response> serverResponseCallback);

    @GET("/headers")
    void testGet(Callback<Response> serverResponseCallback);
}
