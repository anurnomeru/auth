package com.raythonsoft.sso.model;

import com.raythonsoft.common.model.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @Column(name = "pic_uri")
    private String picUri;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "server_id")
    private Integer serverId;

    private Integer orders;

    @Transient
    private PageModel pageModel;
}