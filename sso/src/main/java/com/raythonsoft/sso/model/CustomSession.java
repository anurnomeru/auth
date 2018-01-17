package com.raythonsoft.sso.model;

import com.raythonsoft.sso.session.OnlineStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.shiro.session.mgt.SimpleSession;

/**
 * Created by Anur IjuoKaruKas on 2018/1/11.
 * Description :
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomSession extends SimpleSession {

    private String userAgent;

    private OnlineStatusEnum onlineStatusEnum = OnlineStatusEnum.OFF_LINE;
}
