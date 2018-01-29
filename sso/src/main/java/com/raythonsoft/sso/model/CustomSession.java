package com.raythonsoft.sso.model;

import com.raythonsoft.sso.session.OnlineStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.shiro.session.mgt.SimpleSession;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Anur IjuoKaruKas on 2018/1/11.
 * Description :
 */
@Data
public class CustomSession extends SimpleSession {
    private String userAgent;
    private OnlineStatusEnum onlineStatusEnum = OnlineStatusEnum.OFF_LINE;
}
