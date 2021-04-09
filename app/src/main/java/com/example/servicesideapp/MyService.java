package com.example.servicesideapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Random;

public class MyService extends Service {

    int mRandomNumber;
    boolean mIsRandomGeneratorOn;

    final int MIN=0;
    final int MAX=100;

  public static final int GET_COUNT=0;

  private class RandomNumberRequestHandler extends Handler{
      @Override
      public void handleMessage(@NonNull Message msg) {
          switch (msg.what){
              case GET_COUNT:
                  Message messageSendRandomNumber=Message.obtain(null,GET_COUNT);
                  messageSendRandomNumber.arg1=getRandomNumber();
                  try{
                      msg.replyTo.send(messageSendRandomNumber);
                  }catch (RemoteException e){
                      e.printStackTrace();
                  }
          }
          super.handleMessage(msg);
      }
  }

  private Messenger randomNumberMessenger=new Messenger(new RandomNumberRequestHandler());


    @Override
    public IBinder onBind(Intent intent) {

        if (intent.getPackage().equals("com.example.clientsideapp")){
            Toast.makeText(getApplicationContext(), "Correct Package", Toast.LENGTH_SHORT).show();
            return randomNumberMessenger.getBinder();
        }else {
            Toast.makeText(getApplicationContext(), "Wrong Package", Toast.LENGTH_SHORT).show();
        }
        return null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i(getString(R.string.app_name),"Service Destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(getString(R.string.app_name),"In onStartCommand,id:"+Thread.currentThread().getId());

        mIsRandomGeneratorOn=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyService.this.startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    private void startRandomNumberGenerator() {

        while (mIsRandomGeneratorOn){
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn){
                    mRandomNumber=new Random().nextInt(MAX)+MIN;
                    Log.i("ServiceDemo","Thread id:"+Thread.currentThread().getId()+"Random Number:"+mRandomNumber);

                }
            }catch (InterruptedException e){
                Log.i("ServiceDemo","Thread Interrupted");

            }
        }
    }
    private void stopRandomNumberGenerator(){
        mIsRandomGeneratorOn=false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("ServiceDemo","In onUnBind");
        return super.onUnbind(intent);

    }

    public int getRandomNumber(){
        return mRandomNumber;
    }
}