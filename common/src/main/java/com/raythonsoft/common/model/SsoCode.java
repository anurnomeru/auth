package com.raythonsoft.common.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Anur IjuoKaruKas on 2018/1/19.
 * Description :
 */
@Data
@Builder
public class SsoCode {

    public SsoCode() {
    }

    public SsoCode(String checkCode) {
        this.checkCode = checkCode;
    }

    private String checkCode;
}
