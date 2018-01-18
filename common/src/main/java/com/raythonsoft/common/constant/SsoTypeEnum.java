package com.raythonsoft.common.constant;

/**
 * Created by Anur IjuoKaruKas on 2018/1/11.
 * Description :
 */
public enum SsoTypeEnum {
    CLIENT("客户端", "0"), SERVER("服务端", "1");

    private String name;
    private String index;

    SsoTypeEnum(String name, String index) {
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
