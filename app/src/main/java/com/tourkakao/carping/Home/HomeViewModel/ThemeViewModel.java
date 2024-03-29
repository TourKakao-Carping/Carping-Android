package com.tourkakao.carping.Home.HomeViewModel;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tourkakao.carping.Home.Fragment.ThemeFragment;
import com.tourkakao.carping.Home.ThemeDataClass.AZPost;
import com.tourkakao.carping.Home.ThemeDataClass.NewCapringPlace;
import com.tourkakao.carping.Home.ThemeDataClass.Popular;
import com.tourkakao.carping.Home.ThemeDataClass.Thisweekend;
import com.tourkakao.carping.Home.ThemeFragmentAdapter.Az_Adapter;
import com.tourkakao.carping.Home.ThemeFragmentAdapter.NewCarpingPlace_Adapter;
import com.tourkakao.carping.Home.ThemeFragmentAdapter.PopularCarpingPlace_Adapter;
import com.tourkakao.carping.Home.ThemeFragmentAdapter.ThisWeekend_Adapter;
import com.tourkakao.carping.NetworkwithToken.CommonClass;
import com.tourkakao.carping.NetworkwithToken.TotalApiClient;
import com.tourkakao.carping.Post.PostDetailActivity;
import com.tourkakao.carping.Post.PostInfoActivity;
import com.tourkakao.carping.newcarping.Activity.Each_NewCarpingActivity;
import com.tourkakao.carping.theme.Activity.ThemeDetailActivity;
import com.tourkakao.carping.thisweekend.Activity.Each_ThisWeekendActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThemeViewModel extends ViewModel {
    private Context context;
    //for thisweekends post in main
    public MutableLiveData<Integer> main_thisweekends_post_cnt=new MutableLiveData<>();
    ThisWeekend_Adapter thisWeekend_adapter;
    ArrayList<Thisweekend> thisweekends=null;
    //for new post in main
    public MutableLiveData<Integer> main_new_post_cnt=new MutableLiveData<>();
    NewCarpingPlace_Adapter newCarpingPlace_adapter;
    ArrayList<NewCapringPlace> newCapringPlaces=null;

    //for az post in main
    public MutableLiveData<Integer> main_az_post_cnt=new MutableLiveData<>();
    Az_Adapter az_adapter;
    ArrayList<AZPost> azPosts=null;

    //for popular carping in main
    public MutableLiveData<Integer> main_popular_cnt=new MutableLiveData<>();
    PopularCarpingPlace_Adapter popularCarpingPlace_adapter;
    ArrayList<Popular> populars=null;

    public ThemeViewModel(){
        main_thisweekends_post_cnt.setValue(-1);
        main_new_post_cnt.setValue(-1);
        main_az_post_cnt.setValue(0);
        main_popular_cnt.setValue(0);
    }

    public void setContext(Context context){
        this.context=context;
    }
    public ThisWeekend_Adapter setting_thisweekend_adapter(){
        thisweekends=new ArrayList<>();
        thisWeekend_adapter=new ThisWeekend_Adapter(context, thisweekends);
        thisWeekend_adapter.setOnSelectItemCLickListener(new ThisWeekend_Adapter.OnSelectItemClickListener() {
            @Override
            public void OnSelectItemClick(View v, int pos, int pk) {
                Intent intent=new Intent(context, Each_ThisWeekendActivity.class);
                intent.putExtra("pk", pk);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return thisWeekend_adapter;
    }

    public void getMain_thisweekends(){
        TotalApiClient.getApiService(context).get_thisweekend_post(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lists -> {
                            if(lists.isSuccess()) {
                                main_thisweekends_post_cnt.setValue(lists.getData().size());
                                Type type = new TypeToken<ArrayList<Thisweekend>>() {
                                }.getType();
                                String result = new Gson().toJson(lists.getData());
                                thisweekends = new Gson().fromJson(result, type);
                                thisWeekend_adapter.update_Item(thisweekends);
                            }else{
                                System.out.println(lists.getError_message());
                            }
                        },
                        error -> {
                            System.out.println(error);
                        }
                );
    }

    public Az_Adapter setting_az_adapter(){
        azPosts=new ArrayList<>();
        az_adapter=new Az_Adapter(context, azPosts);
        az_adapter.setOnSelectItemCLickListener(new Az_Adapter.OnSelectItemClickListener() {
            @Override
            public void OnSelectItemClick(View v, int pos, int pk) {
                Intent intent=new Intent(context, PostInfoActivity.class);
                intent.putExtra("pk", pk);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return az_adapter;
    }
    public void getAz(){
        HashMap<String,Object> map=new HashMap<>();
        map.put("type",1);
        TotalApiClient.getPostApiService(context).getPostList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CommonClass>() {
                    @Override
                    public void onSuccess(@NonNull CommonClass commonClass) {
                        if(commonClass.getCode()==200) {
                            main_az_post_cnt.setValue(commonClass.getData().size());
                            Type type = new TypeToken<ArrayList<AZPost>>() {
                            }.getType();
                            String result = new Gson().toJson(commonClass.getData());
                            azPosts = new Gson().fromJson(result, type);
                            az_adapter.update_Item(azPosts);
                        }
                        else {
                            System.out.println("요청실패:"+commonClass.getCode()+commonClass.getError_message());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public NewCarpingPlace_Adapter setting_newcarping_place_adapter(){
        newCapringPlaces=new ArrayList<>();
        newCarpingPlace_adapter=new NewCarpingPlace_Adapter(context, newCapringPlaces);
        newCarpingPlace_adapter.setOnSelectItemCLickListener(new NewCarpingPlace_Adapter.OnSelectItemClickListener() {
            @Override
            public void OnSelectItemClick(View v, int pos, int pk) {
                Intent intent=new Intent(context, Each_NewCarpingActivity.class);
                intent.putExtra("pk", pk);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return newCarpingPlace_adapter;
    }
    public void getNewCarpingPlace(){
        TotalApiClient.getApiService(context).get_newcarping_place(10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        lists -> {
                            if(lists.isSuccess()) {
                                main_new_post_cnt.setValue(lists.getData().size());
                                Type type = new TypeToken<ArrayList<NewCapringPlace>>() {
                                }.getType();
                                String result = new Gson().toJson(lists.getData());
                                newCapringPlaces = new Gson().fromJson(result, type);
                                newCarpingPlace_adapter.update_Item(newCapringPlaces);
                            }else{
                                System.out.println(lists.getError_message());
                            }
                        },
                        error -> {
                        }
                );
    }

    public PopularCarpingPlace_Adapter setting_popularcarping_place_adapter(){
        populars=new ArrayList<>();
        popularCarpingPlace_adapter=new PopularCarpingPlace_Adapter(context, populars);
        popularCarpingPlace_adapter.setOnSelectItemCLickListener(new PopularCarpingPlace_Adapter.OnSelectItemClickListener() {
            @Override
            public void OnSelectItemClick(View v, int pos, int pk) {
                Intent intent=new Intent(context, ThemeDetailActivity.class);
                intent.putExtra("name", populars.get(pos).getName());
                intent.putExtra("pk", populars.get(pos).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return popularCarpingPlace_adapter;
    }
    public void getPopularCarpingPlace(String region){
        TotalApiClient.getApiService(context).get_popular_place(region)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        res -> {
                            if(res.isSuccess()){
                                System.out.println(res.getData());
                                Type type=new TypeToken<ArrayList<Popular>>(){}.getType();
                                String result=new Gson().toJson(res.getData());
                                populars=new Gson().fromJson(result, type);
                                popularCarpingPlace_adapter.update_Item(populars);
                                main_popular_cnt.setValue(populars.size());
                            }
                        },
                        error -> {}
                );
    }
}
