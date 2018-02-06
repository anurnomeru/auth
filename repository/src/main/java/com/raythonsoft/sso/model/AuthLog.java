package com.raythonsoft.sso.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_log")
public class AuthLog {
    @Id
    @Column(name = "log_id")
    private Integer logId;

    private String description;

    private String username;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "base_path")
    private String basePath;

    private String uri;

    private String method;

    @Column(name = "user_agent")
    private String userAgent;

    private String ip;

    private String permissions;

    private String parameter;
}