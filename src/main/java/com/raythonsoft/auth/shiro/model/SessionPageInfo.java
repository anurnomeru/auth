package com.raythonsoft.auth.shiro.model;

import lombok.Builder;
import lombok.Data;
import org.apache.shiro.session.Session;

import java.util.List;

/**
 * Created by Anur IjuoKaruKas on 2018/1/16.
 * Description :
 */
@Data
@Builder
public class SessionPageInfo {
    private long total;
    private List<Session> rows;
}
