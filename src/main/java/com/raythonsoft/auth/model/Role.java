package com.raythonsoft.auth.model;

import com.raythonsoft.auth.bo.BaseRole;
import com.raythonsoft.auth.common.PageModel;
import lombok.Data;

import javax.persistence.*;

@Data
public class Role extends PageModel implements BaseRole{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
}