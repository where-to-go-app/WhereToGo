package com.wheretogo.data.remote;

public interface DefaultCallback<T> {
    void onSuccess(T data);

    void onError(int error);
}
