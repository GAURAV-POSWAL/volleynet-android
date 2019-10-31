package com.volleynet.network;

public interface RequestComplete {
    void requestSuccess(ResponseObject object);

    void requestFailed(ResponseObject object);
}
