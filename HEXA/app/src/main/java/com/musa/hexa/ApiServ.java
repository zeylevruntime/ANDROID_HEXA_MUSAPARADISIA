package com.musa.hexa;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServ {
    String BASE_URL = "http://hexavara.ip-dynamic.com/androidrec/public/api";
    @Multipart
    @POST("/login")
    Call<MyResponse> login( @Part("username") RequestBody username, @Part("password") RequestBody password);

}
