package com.example.zishan.gallarytask.network;

import android.support.annotation.NonNull;

import com.example.zishan.gallarytask.ui.Constants;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback<T extends BaseResponse> implements Callback<T> {

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.body() != null && response.isSuccessful()) {
            if (response.body().getStat().equals(Constants.SUCCESS_RESPONSE_CODE))
                onSuccess(response.body());
            else {
                onFail(call, Boolean.FALSE);
            }
        } else {
            onFail(call, Boolean.FALSE);
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            onFail(call, Boolean.TRUE);
        }
        onFail(call, Boolean.FALSE);

    }

    public abstract void onSuccess(T response);

    public abstract void onFail(Call<T> call, Boolean networkError);
}
