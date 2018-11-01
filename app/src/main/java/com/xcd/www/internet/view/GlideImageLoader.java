package com.xcd.www.internet.view;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xcd.www.internet.R;
import com.xcd.www.internet.model.FindBannerModel;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        String url = ((FindBannerModel) path).getImg();
        Log.e("TAG_轮播图","url="+url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.image_error)
                .error(R.mipmap.image_error)
                .into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {

        ImageView imageView = new ImageView(context);
//        imageView.setBackgroundResource(R.mipmap.login_y_yasn);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return imageView;
    }
}
