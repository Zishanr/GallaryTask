package com.example.zishan.gallarytask.network;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("rest")
    Call<BaseResponse> searchFlickerImage(@Query("method") String method,
                                            @Query("api_key") String api_key,
                                            @Query("text") String text,
                                            @Query("per_page") int per_page,
                                            @Query("page") int page,
                                            @Query("format") String format,
                                            @Query("nojsoncallback") int nojsoncallback);


}
