package com.my.myserviceserver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

public class MyRemoteService extends Service {
    private final String TAG = "MyServiceServer";
    private final Random mGenerator = new Random();
    private final RemoteCallbackList<IMyRemoteServiceCallback> callbacks = new RemoteCallbackList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRunning = false;
    private int state = 1;

    private final Runnable periodicTask = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                Log.i(TAG, "Call callback function with state: " + state);
                try {
                    callRegisteredCallback(state);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException", e);
                }
                state++;
                handler.postDelayed(this, 3000);
            }
        }
    };

    public IMyRemoteService.Stub binder = new IMyRemoteService.Stub() {
        @Override
        public int getRandomNumber() {
            int randomNum = mGenerator.nextInt(1000);
            Log.i(TAG, "getRandomNumber() return randomNum: " + randomNum);
            return randomNum;
        }

        @Override
        public boolean registerCallback(IMyRemoteServiceCallback callback) {
            boolean ret = callbacks.register(callback);
            Log.d(TAG, "registerCallback: " + ret);
            isRunning = true;
            handler.post(periodicTask);
            return ret;
        }

        @Override
        public boolean unregisterCallback(IMyRemoteServiceCallback callback) {
            boolean ret = callbacks.unregister(callback);
            Log.d(TAG, "unregisterCallback: " + ret);
            isRunning = false;
            handler.removeCallbacks(periodicTask);
            return ret;
        }
    };

    public void callRegisteredCallback(int state) throws RemoteException {
        Log.d(TAG, "callRegisteredCallback() state:" + state);
        int num = callbacks.beginBroadcast();
        for (int i = 0; i < num; i++) {
            IMyRemoteServiceCallback item = callbacks.getBroadcastItem(i);
            if (item != null) {
                Log.i(TAG, "Call onMyServiceStateChanged() with state: " + state);
                item.onMyServiceStateChanged(state);
            }
        }
        callbacks.finishBroadcast();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind()");
        return binder;
    }
}
