package com.raythonsoft.sso.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;

@Data
@Builder
@Table(name = "auth_user")
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    private String username;

    private String pwd;

    private String salt;

    private Boolean locked;

    @Column(name = "create_time")
    private Date createTime;
}