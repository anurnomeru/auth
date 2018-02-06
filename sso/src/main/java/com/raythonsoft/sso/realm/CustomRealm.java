package com.raythonsoft.sso.realm;

import com.github.pagehelper.util.StringUtil;
import com.raythonsoft.common.constant.AuthConstant;
import com.raythonsoft.common.constant.SsoTypeEnum;
import com.raythonsoft.common.util.PropertiesFileUtil;

import com.raythonsoft.sso.model.AuthPermission;
import com.raythonsoft.sso.model.AuthRole;
import com.raythonsoft.sso.model.AuthUser;
import com.raythonsoft.sso.service.AuthPermissionService;
import com.raythonsoft.sso.service.AuthRoleService;
import com.raythonsoft.sso.service.AuthUserService;
import com.raythonsoft.sso.util.MD5Util;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Anur IjuoKaruKas on 2018/1/8.
 * Description :
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthPermissionService authPermissionService;

    @Autowired
    private AuthRoleService authRoleService;

    /**
     * 认证：登陆
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());

        // 无密认证
        String ssoType = PropertiesFileUtil.getInstance(AuthConstant.SSO_PROPERTY.getPropertyFileName()).get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE);
        if (ssoType.equals(SsoTypeEnum.CLIENT.getName())) {
            return new SimpleAuthenticationInfo(
                    username,
                    password,
                    getName()
            );
        }

        AuthUser user = authUserService.findBy("username", username);
        if (null == user) {
            throw new UnknownAccountException();
        }

        if (!user.getPwd().equals(MD5Util.md5(password + user.getSalt()))) {
            throw new IncorrectCredentialsException();
        }

        if (user.getLocked()) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(
                username,
                password,
                getName()
        );
    }

    /**
     * 授权：鉴权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();
        AuthUser user = authUserService.findBy("username", username);
        Integer userId = user.getUserId();

        List<AuthPermission> permissionList = authPermissionService.findByUserId(userId);
        Set<String> permissionSet = new HashSet<>();
        for (AuthPermission next : permissionList) {
            String permissionName = next.getName();
            if (StringUtil.isNotEmpty(permissionName)) {
                permissionSet.add(permissionName);
            }
        }

        List<AuthRole> roleList = authRoleService.findByUserId(userId);
        Set<String> roleSet = new HashSet<>();
        for (AuthRole next : roleList) {
            String roleName = next.getName();
            if (StringUtil.isNotEmpty(roleName)) {
                roleSet.add(roleName);
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        simpleAuthorizationInfo.setRoles(roleSet);
        return simpleAuthorizationInfo;
    }
}
