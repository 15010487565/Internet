package com.xcd.www.internet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.king.zxing.util.CodeUtils;
import com.xcd.www.internet.R;
import com.xcd.www.internet.application.BaseApplication;
import com.xcd.www.internet.util.VersionUtil;
import com.xyzlf.share.library.bean.ShareEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.help.HelpUtils;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class AccountCodeActivity extends PhotoActivity implements View.OnLongClickListener {

    private ImageView ivAccountCode;
    private TextView tvAccountCodeName;
    private LinearLayout llAccountShareCode;
    private ShareEntity testBean;
    private Bitmap bitmap;
    private ImageView ivAccountCodeHead;
    @Override
    protected Object getTopbarTitle() {
        return "二维码名片";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_code);

        Intent intent = getIntent();
        String AccountInfoHead = intent.getStringExtra("AccountInfoHead");
        Glide.with(this)
                .load(AccountInfoHead)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.launcher_login)
                .error(R.mipmap.launcher_login)
                .into(ivAccountCodeHead);

        String AccountInfoName = intent.getStringExtra("AccountInfoName");
        tvAccountCodeName.setText(TextUtils.isEmpty(AccountInfoName)?"":AccountInfoName);
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("type", "2");
            jsonObj.put("userId", BaseApplication.getInstance().getId());
            createQRCode( jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 生成二维码
     * @param content
     */
    private void createQRCode(String content){
        //生成二维码最好放子线程生成防止阻塞UI，这里只是演示
        Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        bitmap = CodeUtils.createQRCode(content,600,logo);
        //显示二维码
        ivAccountCode.setImageBitmap(bitmap);
    }
    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        ivAccountCodeHead = findViewById(R.id.iv_AccountCodeHead);
        tvAccountCodeName =  findViewById(R.id.tv_AccountCodeName);


        ivAccountCode = findViewById(R.id.iv_AccountCode);

        llAccountShareCode =  findViewById(R.id.ll_AccountShareCode);
        llAccountShareCode.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_DialogMakerShare://发送给朋友
                if (bitmap == null) {
                    ToastUtil.showToast("分享失败！");
                    return;
                }
                String llshare = HelpUtils.showPic(this, llAccountShareCode);
                Log.e("TAG_分享二维码", "llshare=" + llshare);
                if (!TextUtils.isEmpty(llshare)) {
                    mAuthNotifyDialog.dismiss();
                    shareMsg(this, llshare);
                } else {
                    ToastUtil.showToast("分享失败！");
                }
                break;
            case R.id.tv_DialogMakerPhone://保存到手机
                if (bitmap == null) {
                    ToastUtil.showToast("保存到手机失败！");
                    return;
                }
                String bSave = HelpUtils.showPic(this, llAccountShareCode);
                if (!TextUtils.isEmpty(bSave)) {
                    ToastUtil.showToast("保存到手机成功！");
                    mAuthNotifyDialog.dismiss();
                } else {
                    ToastUtil.showToast("保存到手机失败！");
                }
                break;
//            case R.id.tv_DialogMakerCode://扫一扫
//                Intent inteng = new Intent(this, WeChatCaptureActivity.class);
//                startActivity(inteng);
//                submitOneDialog.dismiss();
//                break;
//            case R.id.tv_DialogMakerImageCode://识别图中二维码
//                Intent albumIntent = new Intent(this, AlbumPhotoActivity.class);
//                if (getTpye().equals(AlbumPhotoActivity.TYPE_SINGLE)) {
//                    albumIntent.putExtra(AlbumPhotoActivity.EXTRA_TYPE, AlbumPhotoActivity.TYPE_SINGLE);
//                } else {
//                    albumIntent.putExtra(AlbumPhotoActivity.EXTRA_TYPE, "");
//                }
//                // start
//                startActivityForResult(albumIntent, REQUEST_CODE_HEAD_ALBUM);
//                submitOneDialog.dismiss();
//                break;
        }
    }

    private void shareMsg(Context context, String imgPath) {
        if (!VersionUtil.isAvilible(this, "com.tencent.mm")) {
            ToastUtil.showToast("未安装微信App");
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        if ((imgPath == null) || (imgPath.equals(""))) {
            intent.setType("text/plain");
        } else {
            File f = new File(imgPath);
            if ((f != null) && (f.exists()) && (f.isFile())) {
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            }
        }
//        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, "微信");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        context.startActivity(intent);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, Object> paramsMaps) {
        super.onSuccessResult(requestCode,returnCode,returnMsg,returnData,paramsMaps);
        switch (requestCode) {
//            case 100:
//                if (returnCode == 200) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(returnData);
//                        String img = jsonObject.optString("img");
//                        bitmap = HelpUtils.stringtoBitmap(img);
//                        ivAccountCode.setImageBitmap(bitmap);
//                        String name = jsonObject.optString("name");
//                        tvAccountCodeName.setText(name == null ? "" : name);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    ToastUtil.showToast(returnMsg);
//                }
//                break;
        }
    }

    @Override
    public void onCancelResult() {

    }

    @Override
    public void onErrorResult(int errorCode, IOException errorExcep) {

    }

    @Override
    public void onParseErrorResult(int errorCode) {

    }

    @Override
    public void onFinishResult() {

    }

    @Override
    public boolean onLongClick(View view) {
        showMakerCodeDialog();
        return false;
    }

    //长按弹窗
    protected AlertDialog mAuthNotifyDialog;

    private void showMakerCodeDialog() {
        if (mAuthNotifyDialog !=null && mAuthNotifyDialog.isShowing()){
            return;
        }
        LayoutInflater factor = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View serviceView = factor.inflate(R.layout.dialog_groupcode, null);
        TextView tvDialogMakerShare =  serviceView.findViewById(R.id.tv_DialogMakerShare);
        tvDialogMakerShare.setOnClickListener(this);
        TextView tvDialogMakerPhone = serviceView.findViewById(R.id.tv_DialogMakerPhone);
        tvDialogMakerPhone.setOnClickListener(this);
//        TextView tvDialogMakerCode = (TextView) serviceView.findViewById(R.id.tv_DialogMakerCode);
//        tvDialogMakerCode.setOnClickListener(this);
//        TextView tvDialogMakerImageCode = (TextView) serviceView.findViewById(R.id.tv_DialogMakerImageCode);
//        tvDialogMakerImageCode.setOnClickListener(this);
        Activity activity = this;
        while (activity.getParent() != null) {
            activity = activity.getParent();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        mAuthNotifyDialog = builder.create();
        mAuthNotifyDialog.show();
        mAuthNotifyDialog.setContentView(serviceView);
        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.WRAP_CONTENT);
        //layout.setMargins(WallspaceUtil.dip2px(this, 10), 0, FeatureFunction.dip2px(this, 10), 0);
        serviceView.setLayoutParams(layout);
    }
}
