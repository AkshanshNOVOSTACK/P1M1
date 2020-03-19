package com.factor8.p1m1.Network;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApi {
    @FormUrlEncoded
    @POST("register")
        // specify the sub url for our base url
    Call<JsonElement> login(
            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("verifyOtp")
        // specify the sub url for our base url
    Call<JsonElement> login2(
            @Field("mobile") String mobile,
             @Field("salary") String salary);

}
