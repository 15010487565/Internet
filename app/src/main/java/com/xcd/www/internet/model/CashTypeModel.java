package com.xcd.www.internet.model;

import java.io.Serializable;

/**
 * Created by gs on 2018/10/27.
 */

public class CashTypeModel implements Serializable {
    private String name;
    private boolean isSelect;
    private String coin;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
