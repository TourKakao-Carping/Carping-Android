package com.tourkakao.carping.registernewcarping.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tourkakao.carping.Gallerypermission.Gallery_setting;
import com.tourkakao.carping.R;
import com.tourkakao.carping.SharedPreferenceManager.SharedPreferenceManager;
import com.tourkakao.carping.databinding.ActivityRegisterBinding;
import com.tourkakao.carping.databinding.EachImageBinding;
import com.tourkakao.carping.registernewcarping.viewmodel.RegisterViewmodel;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding registerBinding;
    Gallery_setting gallery_setting;
    Context context;
    MapView mapView=null;
    RegisterViewmodel registerViewmodel;
    private TextView addtag=null;
    private TextView newtag=null;
    int image_count=0;
    boolean send_image_ok=false;
    boolean send_text_ok=false;
    boolean send_locate_ok=false;
    boolean send_title_ok=false;
    float lat=-1f, lon=-1f;
    MapPoint mapPoint=null;
    MapPOIItem marker=null;
    boolean page_to_search=false;
    Point size;
    int screen_width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(registerBinding.getRoot());
        context=this;
        gallery_setting=new Gallery_setting(context, RegisterActivity.this);
        size=new Point();
        getWindowManager().getDefaultDisplay().getRealSize(size);
        screen_width=size.x;

        registerViewmodel=new ViewModelProvider(this).get(RegisterViewmodel.class);
        registerViewmodel.setContext(context);
        registerViewmodel.setUserpk(SharedPreferenceManager.getInstance(context).getInt("id", 0));

        setting_locate();
        setting_title();
        setting_review_edittext();
        setting_getting_review_image();
        setting_initial_tags();
        setting_remove_tags();
        setting_sending_button();
        starting_observe_send_newcarping_ok();
        starting_observe_lat_and_lon();
    }
    public void setting_locate(){
        if(mapView==null){
            mapView=new MapView(this);
            registerBinding.mapView.addView(mapView);
        }
        registerBinding.locateTextview.setOnClickListener( v -> {
            page_to_search=true;
            startActivityForResult(new Intent(context, SearchNewCarpingActivity.class), 1003);
        });
        registerBinding.reLocateTextview.setOnClickListener(v -> {
            page_to_search=true;
            startActivityForResult(new Intent(context, SearchNewCarpingActivity.class), 1003);
        });
    }
    public void setting_title(){
        registerBinding.titleEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerViewmodel.n_title=s.toString();
                if(s.toString().length()<=0){
                    send_title_ok=false;
                }else{
                    send_title_ok=true;
                }
                changeing_register_button();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void setting_review_edittext(){
        registerBinding.reviewEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerViewmodel.n_text=s.toString();
                if(s.toString().length()<=0){
                    send_text_ok=false;
                }else{
                    send_text_ok=true;
                }
                registerBinding.textCount.setText(s.toString().length()+"/100");
                changeing_register_button();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void setting_getting_review_image(){
        registerBinding.addImage.setOnClickListener(v -> {
            if(image_count<4) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int permission_read = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    int permission_write = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (permission_read == PackageManager.PERMISSION_DENIED || permission_write == PackageManager.PERMISSION_DENIED) {
                        gallery_setting.check_gallery_permission();
                    } else {
                        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryintent.setType("image/*");
                        startActivityForResult(galleryintent, gallery_setting.GALLERY_CODE);
                        /*Intent galleryintent=new Intent();
                        galleryintent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        galleryintent.setType("image/*");
                        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(galleryintent, gallery_setting.GALLERY_CODE);*/
                    }
                } else {
                    Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryintent.setType("image/*");
                    startActivityForResult(galleryintent, gallery_setting.GALLERY_CODE);
                }
            }else{
                Toast.makeText(context, "4장까지 첨부 가능해요", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setting_sending_button(){
        registerBinding.registerBtn.setOnClickListener(v -> {
            if(send_image_ok && send_locate_ok && send_text_ok && send_title_ok){
                registerViewmodel.sending_newcarping();
            }else if(!send_locate_ok){
                Toast.makeText(context, "차박지 위치를 검색해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }else if(!send_image_ok){
                Toast.makeText(context, "사진을 업로드해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }else if(!send_title_ok){
                Toast.makeText(context, "차박지 이름을 적어주세요!", Toast.LENGTH_SHORT).show();
                return;
            }else if(!send_text_ok){
                Toast.makeText(context, "내용을 적어주세요!", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
    public void starting_observe_send_newcarping_ok(){
        registerViewmodel.newcarping_send_ok.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    finish();
                }else if(integer==-1){
                    Toast.makeText(context, "10M 이하의 이미지로 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void starting_observe_lat_and_lon(){
        registerViewmodel.n_longitude.observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                float lat=registerViewmodel.n_latitude.getValue();
                float lon=aFloat;
                if(page_to_search) {
                    page_to_search=false;
                    mapView=new MapView(context);
                    registerBinding.mapView.addView(mapView);
                }
                if(mapPoint!=null){
                    mapPoint=null;
                }
                mapPoint=MapPoint.mapPointWithGeoCoord(lat, lon);
                mapView.setMapCenterPointAndZoomLevel(mapPoint, 4, true);
                marker=null;
                marker=new MapPOIItem();
                marker.setItemName("검색 위치");
                marker.setMapPoint(mapPoint);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                mapView.addPOIItem(marker);
                send_locate_ok=true;
            }
        });
    }
    public void setting_initial_tags(){
        addtag=new TextView(context);
        addtag.setText("+");
        addtag.setClickable(true);
        addtag.setBackgroundResource(R.drawable.purple_border_round);
        addtag.setPadding(60, 30, 60, 30);
        addtag.setOnClickListener(v -> {
            Intent intent=new Intent(context, TagsActivity.class);
            startActivityForResult(intent, 1004);
        });
        registerBinding.tagLayout.addView(addtag);
    }
    public void setting_remove_tags(){
        registerBinding.tagsRemove.setOnClickListener(v -> {
            registerBinding.tagLayout.removeAllViews();
            registerViewmodel.n_tags.clear();
            setting_initial_tags();
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==gallery_setting.GALLERY_CODE){
                if(data.getData()!=null) {
                    System.out.println(data.getData());
                    int idx=0;
                    for(int i=0; i<4; i++){
                        if(registerViewmodel.n_uri.get(i)==null){
                            registerViewmodel.n_uri.set(i, data.getData());
                            idx=i;
                            break;
                        }
                    }
                    image_count++;
                    registerBinding.imageCnt.setText(image_count+"/4");
                    EachImageBinding binding=EachImageBinding.inflate(getLayoutInflater());
                    ImageView addimage=binding.addEachImage;
                    ImageView cancelimage=binding.cancelImage;
                    cancelimage.setClickable(true);
                    Glide.with(context).load(data.getData()).transform(new CenterCrop(), new RoundedCorners(30)).into(addimage);
                    Glide.with(context).load(R.drawable.cancel_image).transform(new CenterCrop(), new RoundedCorners(30)).into(cancelimage);
                    int finalIdx = idx;
                    cancelimage.setOnClickListener(v -> {
                        registerBinding.imageAreaLayout.removeView(binding.getRoot());
                        registerViewmodel.n_uri.set(finalIdx, null);
                        image_count--;
                        registerBinding.imageCnt.setText(image_count+"/4");
                        if(image_count==0){
                            send_image_ok=false;
                        }
                        changeing_register_button();
                    });
                    registerBinding.imageAreaLayout.addView(binding.getRoot());
                    send_image_ok=true;
                    changeing_register_button();
                }
            }else if(requestCode==1003){
                registerBinding.beforeSearchLayout.setVisibility(View.GONE);
                registerBinding.afterSearchLayout.setVisibility(View.VISIBLE);
                registerBinding.locateTextview.setVisibility(View.GONE);
                registerBinding.mapView.setVisibility(View.VISIBLE);
                registerBinding.searchText.setText(data.getStringExtra("place"));
                registerViewmodel.n_latitude.setValue(Float.parseFloat(data.getStringExtra("lat")));
                registerViewmodel.n_longitude.setValue(Float.parseFloat(data.getStringExtra("lon")));
            }else if(requestCode==1004){
                String tag=data.getStringExtra("tag");
                registerViewmodel.n_tags.add(tag);
                newtag=null;
                TextView newtag=new TextView(context);
                newtag.setText("#"+tag);
                newtag.setBackgroundResource(R.drawable.purple_border_round);
                newtag.setTextColor(Color.parseColor("#9F81F7"));
                newtag.setPadding(60, 30, 60, 30);
                registerBinding.tagLayout.addView(newtag);
            }
        }
    }
    public void changeing_register_button(){
        if(send_image_ok && send_text_ok && send_locate_ok && send_title_ok){
            registerBinding.registerBtn.setBackgroundColor(Color.BLACK);
        }else{
            registerBinding.registerBtn.setBackgroundColor(Color.parseColor("#A4A4A4"));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==gallery_setting.PERMISSION_GALLERY_REQUESTCODE){
            if(grantResults.length==gallery_setting.REQUIRED_PERMISSIONS.length){
                boolean check_result=true;
                for(int result: grantResults){
                    if(result== PackageManager.PERMISSION_DENIED){
                        check_result=false;
                        break;
                    }
                }
                if(check_result){
                    Toast.makeText(context, "갤러리 접근 권한이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("갤러리 접근 권한 설정 알림")
                            .setMessage("서비스 사용을 위해서는 갤러리 접근 권한 설정이 필요합니다. [설정]->[앱]에서 갤러리 접근 권한을 승인해주세요")
                            .setCancelable(false)
                            .setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(page_to_search) {
            registerBinding.mapView.removeView(mapView);
            mapView = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}