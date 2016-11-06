package com.example.dorinpaunescu.remotecontrol.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dorinpaunescu.remotecontrol.client.RemoteControllerProtocol;
import com.example.dorinpaunescu.remotecontrol.envelope.AccelerometerEnvelope;
import com.example.dorinpaunescu.remotecontrol.factory.RemoteControllerFactory;
import com.example.dorinpaunescu.remotecontrol.factory.ResourceManagerFactory;
import com.example.dorinpaunescu.remotecontrol.factory.ResourceManagerProducer;

import java.util.Date;

public class SensorActivity implements SensorEventListener {

  private TextView txX;
  private TextView txY;
  private TextView txZ;

  public void setTxX(TextView txX) {
    this.txX = txX;
  }

  public void setTxY(TextView txY) {
    this.txY = txY;
  }

  public void setTxZ(TextView txZ) {
    this.txZ = txZ;
  }

  public SensorActivity(TextView txX, TextView txY, TextView txZ){
    this.txX = txX;
    this.txY = txY;
    this.txZ = txZ;
  }

  @Override
  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Do something here if sensor accuracy changes.
  }

  private Date last = new Date();
  @Override
  public final void onSensorChanged(SensorEvent event) {
    // The light sensor returns a single value.
    // Many sensors return 3 values, one for each axis.

    float x = event.values[0];
    float y = event.values[1];
    float z = event.values[2];

    if(this.txX != null) {
      this.txX.setText("X=" + x);
    }
    if(this.txY != null) {
      this.txY.setText("Y=" + y);
    }

    if(this.txZ != null) {
      this.txZ.setText("Z=" + z);
    }
    // Do something with this sensor value.
    try{
      Date now = new Date();
      if(now.getTime() - last.getTime() > 2000) {
        System.out.println("onSensorChanged");
        AccelerometerEnvelope ae = new AccelerometerEnvelope(Float.toString(x), Float.toString(y), Float.toString(z));
        ResourceManagerFactory factoryManager = ResourceManagerProducer.getFactoryManager(ResourceManagerProducer.REMOTE_CONTROLLER_TYPE);
        RemoteControllerProtocol remoteController = factoryManager.createRemoteController(RemoteControllerFactory.REST_BASED_REMOTE_CONTROLLER, null);
        remoteController.sendAccelerometerDate(ae);
        last = now;
      }
    }catch (Throwable ex) {

    }
  }

}