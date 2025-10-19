package com.WHXeO46.github.stepcounter.data;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.WHXeO46.github.stepcounter.user.Account;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class StepService extends Service implements SensorEventListener {
    public static final String ACTION_STEP_UPDATE = "com.WHXeO46.github.stepcounter.STEP_UPDATE";
    public static final String EXTRA_STEP_COUNT = "step_count";
    private static final long STEP_THRESHOLD  = 500;
    private static final long SAVE_THRESHOLD = 10000;
    private SensorManager sensorManager;
    private String nowDate = null, lastDate = null;
    private static int step;// for fetching steps outside the object

    private static long lastSensorEventTime = 0;
    private static long lastSaveEventTime = 0;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // step签名，计步服务显示，不打扰
            NotificationChannel channel = new NotificationChannel("step", "恰洛计步器", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, "step")
                                                        .setContentTitle("计步器运行中, Ciallo~")
                                                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                                                        .build();
            startForeground(1, notification);
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        lastDate = sdf.format(System.currentTimeMillis());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            Blob tempBlob = Blob.load(getApplicationContext(), null, true);
            if (tempBlob != null) {
                step = tempBlob.getStep();
                lastDate = tempBlob.getDate().toString();
            }
        } catch (IOException | ClassNotFoundException e) {
        }

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent e){
        if (e.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (e.values == null || e.values.length == 0) {
                return; // No data, do nothing
            }
            if (lastSensorEventTime==0 || System.currentTimeMillis()-lastSensorEventTime>=STEP_THRESHOLD){
                if (lastDate!=null && !lastDate.isEmpty()) {
                    if (isDateChanged()) {
                        if(Account.getUserName() != null) {
                            Blob blob = new Blob(step);
                            try {
                                blob.save(getApplicationContext(), false);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            lastDate = sdf.format(System.currentTimeMillis());
                            step = 0;
                        }
                    } else {
                        if (lastDate == null || lastDate.isEmpty()) {
                            lastDate = sdf.format(System.currentTimeMillis());
                        } else {
                            step++;
                        }
                    }
                    // Broadcast the step update
                    if (step>=0) {
                        Intent intent = new Intent(ACTION_STEP_UPDATE);
                        intent.putExtra(EXTRA_STEP_COUNT, step);
                        sendBroadcast(intent);
                    }

                    lastSensorEventTime = System.currentTimeMillis();
                } else {
                    lastDate = sdf.format(System.currentTimeMillis());
                }
            }

            if (lastSaveEventTime==0 || System.currentTimeMillis()-lastSaveEventTime>=SAVE_THRESHOLD) {
                Blob blob = new Blob(step);
                try {
                    blob.save(getApplicationContext(), true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                lastSaveEventTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isDateChanged(){
        nowDate = sdf.format(System.currentTimeMillis());
        if (!nowDate.equals(lastDate)) {
            return true;
        } else{
            return false;
        }
    }

    public static int getStep() {
        return step;
    }

    public static void setStep(int val) {
        step = val;
    }

    @Override
    public void onDestroy() {
        if(Account.getUserName() != null) {
            Blob blob = new Blob(step);
            try {
                blob.save(getApplicationContext(), true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        super.onDestroy();
    }


}