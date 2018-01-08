package com.raythonsoft.auth.model;

import com.raythonsoft.auth.common.PageModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Resources extends PageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId;

    private String name;

    private String description;

    private String uri;

    @Column(name = "pic_uri")
    private String picUri;

    private String resource;

    private String status;

    private String type;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "system_id")
    private Integer systemId;

    private Integer orders;
}