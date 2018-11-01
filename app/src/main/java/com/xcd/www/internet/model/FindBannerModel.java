package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/11/1.
 */

public class FindBannerModel implements Serializable {
    private String img;
    private String url;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
