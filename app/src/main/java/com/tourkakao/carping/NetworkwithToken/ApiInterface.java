package com.tourkakao.carping.NetworkwithToken;

import com.tourkakao.carping.Home.ThemeDataClass.AZPost;
import com.tourkakao.carping.Home.ThemeDataClass.Thisweekend;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Single;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("posts/autocamp/weekend-post")
    Single<ArrayList<Thisweekend>> get_thisweekend_post(@Field("count")int count);

    @FormUrlEncoded
    @POST("accounts/token/refresh")
    Single<AccessToken> getNewtoken(@Field("refresh_token")String refresh_token);

    @GET("")
    Single<ArrayList<AZPost>> get_az_post();
}
