package com.raythonsoft.sso.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Anur IjuoKaruKas on 2018/4/9.
 * Description :
 */
@Data
@ConfigurationProperties(prefix = "sso")
public class SsoProperties {
    private String type;

    private String serverUrl;
}
