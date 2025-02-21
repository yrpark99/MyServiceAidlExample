package com.my.myserviceserver;

import com.my.myserviceserver.IMyRemoteServiceCallback;

interface IMyRemoteService {
    int getRandomNumber();
    boolean registerCallback(IMyRemoteServiceCallback callback);
    boolean unregisterCallback(IMyRemoteServiceCallback callback);
}