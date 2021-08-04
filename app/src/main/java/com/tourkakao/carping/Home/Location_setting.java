package com.tourkakao.carping.Home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

public class Location_setting implements HomeContract, HomeContract.Location_setting_Contract{
    Context context;
    Activity used_Activity;

    SharedPreferences main_prefs;
    SharedPreferences.Editor main_editor;
    boolean isFirstPermissionCheck;

    public Location_setting(Context context, Activity used_Activity){
        this.context=context;
        this.used_Activity=used_Activity;
    }

    @Override
    public void setting_sharedpreferences(SharedPreferences prefs, SharedPreferences.Editor editor) {
        this.main_prefs=prefs;
        this.main_editor=editor;
        this.isFirstPermissionCheck=this.main_prefs.getBoolean("isFirstPermissionCheck", true);
    }

    @Override
    public void check_locate_permission() {
        if(Build.VERSION.SDK_INT>=23){
            int permission_coarse=context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            int permission_fine=context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            //사용자가 권한 설정을 거절했거나 초기 상태인 경우
            if(permission_coarse== PackageManager.PERMISSION_DENIED || permission_fine==PackageManager.PERMISSION_DENIED){
                //사용자가 권한 설정 거부만 한 경우(다시 묻지않음이 아닌 설정 거부만 총 1번 한 경우)
                if(ActivityCompat.shouldShowRequestPermissionRationale(used_Activity, REQUIRED_PERMISSIONS[0]) ||
                ActivityCompat.shouldShowRequestPermissionRationale(used_Activity, REQUIRED_PERMISSIONS[1])){
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("위치 권한 설정 알림")
                            .setMessage("서비스 사용을 위해서는 위치 권한 설정이 필요합니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(used_Activity, REQUIRED_PERMISSIONS, PERMISSION_LOCATION_REQUESTCODE);
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }else{
                    if(isFirstPermissionCheck){//사용자에게 처음 물어본 경우(처음 앱을 실행한 경우)
                        main_editor.putBoolean("isFirstPermissionCheck", false).apply();
                        ActivityCompat.requestPermissions(used_Activity, REQUIRED_PERMISSIONS, PERMISSION_LOCATION_REQUESTCODE);
                    }else{//권한 설정 거부와 동시에 다시 묻지않음을 선택한 경우, 이번만 허용한 경우, 누적 두번 이상 거절한 경우
                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("위치 권한 설정 알림")
                                .setMessage("서비스 사용을 위해서는 위치 권한 설정이 필요합니다. 설정 화면으로 이동합니다.")
                                .setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent setting_permission_intent=new Intent();
                                        //핸드폰 어플리케이션 설정으로 이동
                                        setting_permission_intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        setting_permission_intent.setData(Uri.parse("package:"+"com.tourkakao.carping"));
                                        context.startActivity(setting_permission_intent);
                                    }
                                });
                        builder.create().show();
                    }
                }
            }
        }
    }
}