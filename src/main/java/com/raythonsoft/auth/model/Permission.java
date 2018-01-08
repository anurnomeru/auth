package com.raythonsoft.auth.model;

import com.raythonsoft.auth.bo.BasePermission;
import com.raythonsoft.auth.common.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
public class Permission extends PageModel implements BasePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private String name;

    private String resource;

    @Column(name = "parent_id")
    private Integer parentId;
}