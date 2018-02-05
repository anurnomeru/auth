package com.raythonsoft.sso.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String permissions;

    private String description;

    @Column(name = "user_agent")
    private String userAgent;

    private Date time;

    private String uri;

    private String method;

    private String param;

    private String ip;

}