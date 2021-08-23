package com.tourkakao.carping.NetworkwithToken;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ThemeInterface {
    @FormUrlEncoded
    @POST("posts/autocamp/weekend-post")
    Single<CommonClass> get_thisweekend_post(@Field("count")int count);

    @FormUrlEncoded
    @POST("accounts/token/refresh")
    Single<AccessToken> getNewtoken(@Field("refresh_token")String refresh_token);

    @GET("")
    Single<CommonClass> get_az_post();
}