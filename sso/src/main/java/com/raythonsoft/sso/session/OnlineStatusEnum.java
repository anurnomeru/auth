package com.raythonsoft.sso.session;

/**
 * Created by Anur IjuoKaruKas on 2018/1/11.
 * Description :
 */
public enum OnlineStatusEnum {

    ON_LINE("在线", "0"), OFF_LINE("离线", "1"), FORCE_LOGOUT("强制退出", "2");

    private String name;
    private String index;

    OnlineStatusEnum(String name, String index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
