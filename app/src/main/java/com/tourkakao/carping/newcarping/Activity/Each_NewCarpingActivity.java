package com.tourkakao.carping.newcarping.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.tourkakao.carping.BuildConfig;
import com.tourkakao.carping.EcoCarping.Activity.EcoCarpingEditActivity;
import com.tourkakao.carping.NetworkwithToken.TotalApiClient;
import com.tourkakao.carping.R;
import com.tourkakao.carping.SharedPreferenceManager.SharedPreferenceManager;
import com.tourkakao.carping.databinding.ActivityEachNewCarpingBinding;
import com.tourkakao.carping.newcarping.Fragment.InfoFragment;
import com.tourkakao.carping.newcarping.Fragment.ReviewFragment;
import com.tourkakao.carping.newcarping.viewmodel.EachNewCarpingViewModel;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Each_NewCarpingActivity extends AppCompatActivity implements MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private ActivityEachNewCarpingBinding eachNewCarpingBinding;
    private final String KAKAO_APPKEY=BuildConfig.KAKAO_NATIVE_APPKEY;
    Context context;
    int post_pk;
    Each_NewCarpingActivity each_newCarpingActivity;
    MapView mapView=null;
    InfoFragment infoFragment;
    ReviewFragment reviewFragment;
    EachNewCarpingViewModel eachNewCarpingViewModel;
    MapReverseGeoCoder.ReverseGeoCodingResultListener reverseGeoCodingResultListener;
    MapReverseGeoCoder reverseGeoCoder=null;
    MapPoint mapPoint=null;
    boolean to_edit=false;
    boolean list_layout=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eachNewCarpingBinding=ActivityEachNewCarpingBinding.inflate(getLayoutInflater());
        setContentView(eachNewCarpingBinding.getRoot());
        context=this;
        each_newCarpingActivity=this;
        reverseGeoCodingResultListener=this;
        Glide.with(context).load(R.drawable.locate_img).into(eachNewCarpingBinding.locateImg);

        eachNewCarpingViewModel=new ViewModelProvider(this).get(EachNewCarpingViewModel.class);
        eachNewCarpingViewModel.setContext(context);
        post_pk=getIntent().getIntExtra("pk", 0);
        eachNewCarpingViewModel.setPk(post_pk);
        eachNewCarpingViewModel.setUserpk(SharedPreferenceManager.getInstance(getApplicationContext()).getInt("id", 0));
        eachNewCarpingBinding.setLifecycleOwner(this);
        eachNewCarpingBinding.setEachNewCarpingViewModel(eachNewCarpingViewModel);
        //setting viewmodel & binding

        infoFragment=new InfoFragment();
        infoFragment.setting_viewmodel(eachNewCarpingViewModel);
        reviewFragment=new ReviewFragment();
        reviewFragment.setting_viewmodel(eachNewCarpingViewModel);
        getSupportFragmentManager().beginTransaction().add(R.id.newcarping_frame, infoFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.newcarping_frame, reviewFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(reviewFragment).commit();
        //setting fragment
        eachNewCarpingViewModel.get_newcarping_detail();

        setting_map();
        setting_tablayout();
        starting_observe_bookmark_image();
        starting_observe_map_and_address();
        starting_observe_userpk();
        starting_observe_delete_review();
        setting_back();
    }

    public void setting_map(){
        if(mapView==null) {
            mapView = new MapView(this);
            eachNewCarpingBinding.mapView.addView(mapView);
        }
    }

    public void setting_tablayout(){
        eachNewCarpingBinding.newcarpingTabs.addTab(eachNewCarpingBinding.newcarpingTabs.newTab().setText("정보"));
        eachNewCarpingBinding.newcarpingTabs.addTab(eachNewCarpingBinding.newcarpingTabs.newTab().setText("리뷰"));
        eachNewCarpingBinding.newcarpingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos=tab.getPosition();
                if(pos==0){
                    getSupportFragmentManager().beginTransaction().show(infoFragment).commit();
                    getSupportFragmentManager().beginTransaction().hide(reviewFragment).commit();
                }else{
                    getSupportFragmentManager().beginTransaction().hide(infoFragment).commit();
                    getSupportFragmentManager().beginTransaction().show(reviewFragment).commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }
    public void starting_observe_bookmark_image(){
        eachNewCarpingViewModel.check_bookmark.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    Glide.with(context).load(R.drawable.bookmark_img).into(eachNewCarpingBinding.bookmarkImg);
                }else if(aBoolean){
                    Glide.with(context).load(R.drawable.mybookmark_img).into(eachNewCarpingBinding.bookmarkImg);
                }
            }
        });
        eachNewCarpingBinding.bookmarkImg.setOnClickListener(v -> {
            if(!eachNewCarpingViewModel.check_bookmark.getValue()) {
                eachNewCarpingViewModel.setting_newcarping_bookmark();
            }else if(eachNewCarpingViewModel.check_bookmark.getValue()){
                eachNewCarpingViewModel.releasing_newcarping_bookmark();
            }
        });
    }
    public void starting_observe_map_and_address(){
        eachNewCarpingViewModel.carpingplace_lon.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                double lat=eachNewCarpingViewModel.carpingplace_lat.getValue();
                double lon=aDouble;
                mapPoint=MapPoint.mapPointWithGeoCoord(lat, lon);
                mapView.setMapCenterPointAndZoomLevel(mapPoint, 3, true);
                MapPOIItem marker=new MapPOIItem();
                marker.setItemName(eachNewCarpingViewModel.title.getValue());
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                marker.setCustomImageResourceId(R.drawable.nowmarker);
                marker.setCustomImageAutoscale(false);
                mapView.addPOIItem(marker);

                reverseGeoCoder=new MapReverseGeoCoder(KAKAO_APPKEY, mapPoint, reverseGeoCodingResultListener, each_newCarpingActivity);
                reverseGeoCoder.startFindingAddress();
            }
        });
    }

    public void starting_observe_userpk(){
        eachNewCarpingViewModel.post_userpk.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    Glide.with(context).load(R.drawable.list_show_btn).into(eachNewCarpingBinding.listbtn);
                    eachNewCarpingBinding.listbtn.setVisibility(View.VISIBLE);
//                    eachNewCarpingBinding.listbtn.setOnClickListener(v -> {
//                        eachNewCarpingBinding.listLayout.setVisibility(View.VISIBLE);
//                        list_layout=true;
//                        eachNewCarpingBinding.newcarpingDelete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                AlertDialog.Builder builder=new AlertDialog.Builder(Each_NewCarpingActivity.this);
//                                builder.setCancelable(false)
//                                        .setTitle("신규 차박지 삭제 알림")
//                                        .setMessage("차박지를 삭제 할까요?")
//                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                TotalApiClient.getApiService(context).delete_newcarping(post_pk)
//                                                        .subscribeOn(Schedulers.io())
//                                                        .observeOn(AndroidSchedulers.mainThread())
//                                                        .subscribe(
//                                                                res -> {
//                                                                    if(res.isSuccess()){
//                                                                        Toast.makeText(Each_NewCarpingActivity.this, "신규 차박지가 삭제되었어요", Toast.LENGTH_SHORT).show();
//                                                                        finish();
//                                                                    }
//                                                                },
//                                                                error -> {}
//                                                        );
//                                            }
//                                        }).create().show();
//
//                            }
//                        });
//                        eachNewCarpingBinding.newcarpingEdit.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                eachNewCarpingBinding.mapView.removeView(mapView);
//                                mapView=null;
//                                eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
//                                to_edit=true;
//                                Intent fixintent=new Intent(context, Fix_newcarpingActivity.class);
//                                fixintent.putExtra("lat", eachNewCarpingViewModel.carpingplace_lat.getValue());
//                                fixintent.putExtra("lon", eachNewCarpingViewModel.carpingplace_lon.getValue());
//                                fixintent.putExtra("image1", eachNewCarpingViewModel.image1.getValue());
//                                fixintent.putExtra("image2", eachNewCarpingViewModel.image2.getValue());
//                                fixintent.putExtra("image3", eachNewCarpingViewModel.image3.getValue());
//                                fixintent.putExtra("image4", eachNewCarpingViewModel.image4.getValue());
//                                fixintent.putExtra("tags", eachNewCarpingViewModel.info_tags.getValue());
//                                fixintent.putExtra("title", eachNewCarpingViewModel.title.getValue());
//                                fixintent.putExtra("review", eachNewCarpingViewModel.info_review.getValue());
//                                fixintent.putExtra("postpk", eachNewCarpingViewModel.pk);
//                                fixintent.putExtra("userpk", eachNewCarpingViewModel.userpk);
//                                startActivityForResult(fixintent, 1001);
//                            }
//                        });
//                    });
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        eachNewCarpingBinding.newcarpingFrame.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                    eachNewCarpingBinding.newcarpingTabs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
                        }
                    });
                    eachNewCarpingBinding.newcarpingTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
                        }
                    });
                    eachNewCarpingBinding.newcarpingTotalRating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
                        }
                    });
                    eachNewCarpingBinding.locate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
                        }
                    });*/
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        eachNewCarpingBinding.parentLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                            @Override
//                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                                eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
//                            }
//                        });
//                    }
                }else if(integer==0){
                    eachNewCarpingBinding.listbtn.setVisibility(View.GONE);
                }
            }
        });
        eachNewCarpingBinding.listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerForContextMenu(view);
                view.showContextMenu();
                unregisterForContextMenu(view);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_page_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.edit:
                eachNewCarpingBinding.mapView.removeView(mapView);
                mapView=null;
                eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
                to_edit=true;
                Intent fixintent=new Intent(context, Fix_newcarpingActivity.class);
                fixintent.putExtra("lat", eachNewCarpingViewModel.carpingplace_lat.getValue());
                fixintent.putExtra("lon", eachNewCarpingViewModel.carpingplace_lon.getValue());
                fixintent.putExtra("image1", eachNewCarpingViewModel.image1.getValue());
                fixintent.putExtra("image2", eachNewCarpingViewModel.image2.getValue());
                fixintent.putExtra("image3", eachNewCarpingViewModel.image3.getValue());
                fixintent.putExtra("image4", eachNewCarpingViewModel.image4.getValue());
                fixintent.putExtra("tags", eachNewCarpingViewModel.info_tags.getValue());
                fixintent.putExtra("title", eachNewCarpingViewModel.title.getValue());
                fixintent.putExtra("review", eachNewCarpingViewModel.info_review.getValue());
                fixintent.putExtra("postpk", eachNewCarpingViewModel.pk);
                fixintent.putExtra("userpk", eachNewCarpingViewModel.userpk);
                startActivityForResult(fixintent, 1001);
                return true;
            case R.id.delete:
                AlertDialog.Builder builder=new AlertDialog.Builder(Each_NewCarpingActivity.this);
                builder.setCancelable(false)
                        .setTitle("신규 차박지 삭제 알림")
                        .setMessage("차박지를 삭제 할까요?")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TotalApiClient.getApiService(context).delete_newcarping(post_pk)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                res -> {
                                                    if(res.isSuccess()){
                                                        Toast.makeText(Each_NewCarpingActivity.this, "신규 차박지가 삭제되었어요", Toast.LENGTH_SHORT).show();
                                                        SharedPreferenceManager.getInstance(getApplicationContext()).setInt("newcarping_delete", 1);
                                                        finish();
                                                    }
                                                },
                                                error -> {}
                                        );
                            }
                        }).create().show();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    public void starting_observe_delete_review(){
        eachNewCarpingViewModel.delete_review_ok.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    eachNewCarpingViewModel.get_newcarping_detail();
                }
            }
        });
    }
    public void setting_back(){
        eachNewCarpingBinding.back.setOnClickListener(v -> {
            finish();
        });
    }
    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        eachNewCarpingViewModel.address.setValue(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==1001){
                eachNewCarpingViewModel.get_newcarping_detail();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setting_map();
        if(to_edit){
            double lat=eachNewCarpingViewModel.carpingplace_lat.getValue();
            double lon=eachNewCarpingViewModel.carpingplace_lon.getValue();
            mapPoint=MapPoint.mapPointWithGeoCoord(lat, lon);
            mapView.setMapCenterPointAndZoomLevel(mapPoint, 3, true);
            MapPOIItem marker=new MapPOIItem();
            marker.setItemName(eachNewCarpingViewModel.title.getValue());
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.nowmarker);
            marker.setCustomImageAutoscale(false);
            mapView.addPOIItem(marker);
        }
        if(SharedPreferenceManager.getInstance(context).getInt("newcarping_review_fix", 0)==1){
            eachNewCarpingViewModel.get_newcarping_detail();
            SharedPreferenceManager.getInstance(context).setInt("newcarping_review_fix", 0);
        }
    }

    @Override
    public void onBackPressed() {
        if(list_layout){
            eachNewCarpingBinding.listLayout.setVisibility(View.GONE);
            list_layout=false;
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        eachNewCarpingBinding.mapView.removeView(mapView);
        mapView=null;
    }
}