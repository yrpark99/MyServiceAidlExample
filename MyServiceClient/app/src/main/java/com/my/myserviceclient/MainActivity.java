package com.my.myserviceclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.my.myserviceserver.IMyRemoteService;
import com.my.myserviceserver.IMyRemoteServiceCallback;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MyServiceClient";
    private IMyRemoteService mRemoteService = null;
    private Button btnRandomNumber;
    private TextView tvRandomNumber, tvStateFromCallback;

    /** Callbacks for service binding */
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i(TAG, "onServiceConnected() Service is bounded");
            mRemoteService = IMyRemoteService.Stub.asInterface(service);
            registerCallback();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.i(TAG, "onServiceDisconnected() Service is unbounded");
            mRemoteService = null;
        }
    };

    /* Callbacks for service callback */
    private IMyRemoteServiceCallback mCallback = new IMyRemoteServiceCallback.Stub() {
        @Override
        public void onMyServiceStateChanged(int state) {
            Log.i(TAG, "onMyServiceStateChanged() state: " + state);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvStateFromCallback.setText("Callback state: " + String.valueOf(state));
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRandomNumber = findViewById(R.id.btnRandomNumber);
        tvRandomNumber = findViewById(R.id.tvRandomNumber);
        tvStateFromCallback = findViewById(R.id.tvStateFromCallback);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
        bindToMyRemoteService();
    }

    /* Bind to service: Create Intent with service name defined in server app manifest */
    private void bindToMyRemoteService() {
        Log.i(TAG, "bindToMyRemoteService()");
        Intent intent = new Intent("MyRemoteService");
        intent.setPackage("com.my.myserviceserver");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onRandomNumberButtonClick(View view) {
        if (mRemoteService == null) {
            Log.i(TAG, "Service is not bounded yet");
            return;
        }
        try {
            // Call service for a random number
            int randomNum = mRemoteService.getRandomNumber();
            Log.i(TAG, "Got randomNum: " + randomNum);
            tvRandomNumber.setText(String.valueOf(randomNum));
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException: " + e.getMessage());
        }
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop() Unbind service");
        super.onStop();
        unRegisterCallback();
        unbindService(serviceConnection);
        mRemoteService = null;
    }

    private void registerCallback() {
        if (mRemoteService == null) {
            return;
        }
        try {
            mRemoteService.registerCallback(mCallback);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException: " + e.getMessage());
        }
    }

    private void unRegisterCallback() {
        if (mRemoteService == null) {
            return;
        }
        try {
            mRemoteService.unregisterCallback(mCallback);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException: " + e.getMessage());
        }
    }
}
