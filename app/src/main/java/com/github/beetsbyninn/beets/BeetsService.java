package com.github.beetsbyninn.beets;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.LocaleList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The service listens for events from the sensor handler.
 */
public class BeetsService extends Service implements StepDetectorListener {
    private SensorHandler sensorHandler = new SensorHandler(this,this);
    private LocalBinder mBinder = new LocalBinder();
    private MusicPlayer mMusicPlayer;
    private ArrayList timeStamps = new ArrayList();


    public BeetsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        sensorHandler.registerListener();
        return mBinder;

    }



    @Override
    public void onCreate() {
        super.onCreate();
        sensorHandler.onCreate();
        mMusicPlayer = new MusicPlayer(this);
        try {
            mMusicPlayer.initStepMediaPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorHandler.onDestroy();
    }

    @Override
    public void onStepDetected() {
        try {
            mMusicPlayer.playStep();
            stepTaken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stepTaken() {
        long currentTime = System.currentTimeMillis();
        timeStamps.add(currentTime);
    }


    /**
     *  Returns a reference to the service.
     */
    public class LocalBinder extends Binder {
        BeetsService getService() {
            return BeetsService.this;
        }
    }
}
