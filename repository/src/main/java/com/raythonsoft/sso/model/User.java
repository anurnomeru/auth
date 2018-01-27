package com.raythonsoft.sso.model;

import com.raythonsoft.common.model.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sign;

    private String username;

    private String password;

    private String salt;

    private Boolean lock;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "server_id")
    private Integer serverId;

    private Integer orders;

    @Transient
    private PageModel pageModel;
}