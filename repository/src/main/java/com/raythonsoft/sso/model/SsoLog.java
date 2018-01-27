package com.raythonsoft.sso.model;

import com.raythonsoft.common.model.PageModel;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sso_log")
@Data
@Builder
public class SsoLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sso_log_id")
    private Integer ssoLogId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "method_permissions")
    private String methodPermissions;

    @Column(name = "method_description")
    private String methodDescription;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "request_time")
    private Date requestTime;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "request_param")
    private String requestParam;

    @Column(name = "request_ip")
    private String requestIp;

    @Transient
    private PageModel pageModel;
}