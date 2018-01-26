package com.raythonsoft.repository.model;

import com.raythonsoft.common.model.PageModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Log extends PageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String description;

    @Column(name = "operation_time")
    private Date operationTime;

    private String uri;

    private String method;

    @Column(name = "user_agent")
    private String userAgent;

    private String ip;

    private String permissions;

    private String param;

    private String result;
}