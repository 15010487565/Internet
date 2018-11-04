package com.xcd.www.internet.activity;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.king.zxing.CaptureActivity;
import com.xcd.www.internet.R;

public class CustomCaptureActivity extends CaptureActivity {

    private Button mBtnCancel;
    private TextView mTitleBottom,mTitleTop;
    @Override
    public int getLayoutId() {
        return R.layout.activity_custom_capture;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

//        TextView tvTitle = findViewById(R.id.tvTitle);
//        tvTitle.setText(getIntent().getStringExtra(MainActivity.KEY_TITLE));

        getBeepManager().setPlayBeep(true);
        getBeepManager().setVibrate(true);
    }

    private void offFlash(){
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }

    public void openFlash(){
        Camera camera = getCameraManager().getOpenCamera().getCamera();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
    }


    private void clickFlash(View v){
//        if(v.isSelected()){
//            lightSwitch(true);
//            v.setSelected(false);
//        }else{
//            lightSwitch(false);
//            v.setSelected(true);
//        }
        if(v.isSelected()){
            offFlash();
            v.setSelected(false);
        }else{
            openFlash();
            v.setSelected(true);
        }
    }

    public void OnClick(View v){
        switch (v.getId()){
//            case R.id.ivLeft:
//                onBackPressed();
//                break;
            case R.id.ivFlash:
                clickFlash(v);
                break;
        }
    }
    /**
     * 手电筒控制方法
     *
     * @param lightStatus
     * @return
     */
    private CameraManager manager;// 声明CameraManager对象
    private Camera m_Camera = null;// 声明Camera对象
    private void lightSwitch(final boolean lightStatus) {
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (lightStatus) { // 关闭手电筒
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    manager.setTorchMode("0", false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (m_Camera != null) {
                    m_Camera.stopPreview();
                    m_Camera.release();
                    m_Camera = null;
                }
            }
        } else { // 打开手电筒
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    manager.setTorchMode("0", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                final PackageManager pm = getPackageManager();
                final FeatureInfo[] features = pm.getSystemAvailableFeatures();
                for (final FeatureInfo f : features) {
                    if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
                        if (null == m_Camera) {
                            m_Camera = Camera.open();
                        }
                        final Camera.Parameters parameters = m_Camera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        m_Camera.setParameters(parameters);
                        m_Camera.startPreview();
                    }
                }
            }
        }
    }
}
