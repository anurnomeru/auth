package com.raythonsoft.sso.model;

/**
 * Created by Anur IjuoKaruKas on 2018/1/11.
 * Description :
 */
public enum ShiroTypeEnum {
    CLIENT("客户端", "0"), SERVER("服务端", "1");

    private String name;
    private String index;

    ShiroTypeEnum(String name, String index) {
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
