package com.example.dorinpaunescu.remotecontrol.sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import com.example.dorinpaunescu.remotecontrol.adapters.Constants;
import com.example.dorinpaunescu.remotecontrol.client.RemoteControllerProtocol;
import com.example.dorinpaunescu.remotecontrol.envelope.AccelerometerEnvelope;
import com.example.dorinpaunescu.remotecontrol.factory.RemoteControllerFactory;
import com.example.dorinpaunescu.remotecontrol.factory.ResourceManagerFactory;
import com.example.dorinpaunescu.remotecontrol.factory.ResourceManagerProducer;
import com.example.dorinpaunescu.remotecontrol.properties.PropConfigHolder;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.Map;

public class SensorActivity implements SensorEventListener {

  private TextView txX;
  private TextView txY;
  private TextView txZ;
    private RemoteControllerProtocol remoteController;

    private TextView status;

  public void setTxX(TextView txX) {
    this.txX = txX;
  }

  public void setTxY(TextView txY) {
    this.txY = txY;
  }

  public void setTxZ(TextView txZ) {
    this.txZ = txZ;
  }

    public void setStatus(TextView status) {
        this.status = status;
    }

    public void setRemoteConnector(RemoteControllerProtocol remoteController){
        this.remoteController = remoteController;
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
      //
      Date now = new Date();
    try{

      Map<String, String> properties = PropConfigHolder.getInstance().getProperties();
      long delay = Long.parseLong(properties.get(Constants.ACC_REQ_DELAY));
      if(now.getTime() - last.getTime() > delay) {
        System.out.println("onSensorChanged");
          if(this.remoteController != null) {
              AccelerometerEnvelope ae = new AccelerometerEnvelope(Float.toString(x), Float.toString(y), Float.toString(z));
              remoteController.sendAccelerometerDate(ae, status);
          }
        last = now;
      }
    }catch (Throwable ex) {
        last = now;
        System.out.println("Some critical error happened: " + ex.getLocalizedMessage());
        if(status != null) {
            status.setText(ex.getLocalizedMessage());
        }
    }
  }

}