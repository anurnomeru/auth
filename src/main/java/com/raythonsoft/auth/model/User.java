package com.raythonsoft.auth.model;

import com.raythonsoft.auth.common.PageModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends PageModel {
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

    @Column(name = "system_id")
    private Integer systemId;

    private Integer orders;
}