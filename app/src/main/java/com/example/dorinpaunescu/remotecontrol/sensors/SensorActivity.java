package com.example.dorinpaunescu.remotecontrol.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity implements SensorEventListener {

  private TextView txX;
  private TextView txY;
  private TextView txZ;

  public SensorActivity(TextView txX, TextView txY, TextView txZ){
    this.txX = txX;
    this.txY = txY;
    this.txZ = txZ;
  }

  @Override
  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Do something here if sensor accuracy changes.
  }

  @Override
  public final void onSensorChanged(SensorEvent event) {
    // The light sensor returns a single value.
    // Many sensors return 3 values, one for each axis.

    float x = event.values[0];
    float y = event.values[1];
    float z = event.values[2];
    this.txX.setText("X=" + x);
    this.txY.setText("Y=" + y);
    this.txZ.setText("Z=" + z);
    // Do something with this sensor value.
  }

}