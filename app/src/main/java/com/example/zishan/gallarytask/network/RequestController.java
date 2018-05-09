package com.example.zishan.gallarytask.network;


import com.example.zishan.gallarytask.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class RequestController {

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create());

    private static RequestController requestController;

    private final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES);
    private final HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static RequestController getInstance() {
        if (requestController == null) {
            requestController = new RequestController();
        }
        return requestController;
    }

    public ApiService createService() {

        if (!okHttpClient.interceptors().contains(logging)) {
            okHttpClient.addInterceptor(logging);
        }

        Retrofit retrofit = builder.client(okHttpClient.build()).build();
        return retrofit.create(ApiService.class);
    }
}
