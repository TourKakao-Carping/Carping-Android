package com.tourkakao.carping.LoginNetwork;

import com.tourkakao.carping.Login.Google_Access_Token;
import com.tourkakao.carping.Login.Google_Token_and_User_Info;
import com.tourkakao.carping.Login.Kakao_Token_and_User_Info;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("accounts/login/kakao")
    Call<Kakao_Token_and_User_Info> kakao_signin(@Field("access_token")String access_token);

    @FormUrlEncoded
    @POST("token")
    Call<Google_Access_Token> postAuth(@FieldMap HashMap<String, String> parameters);
    @FormUrlEncoded
    @POST("accounts/login/google")
    Call<Google_Token_and_User_Info> google_signin(@Field("access_token")String access_token);
}
