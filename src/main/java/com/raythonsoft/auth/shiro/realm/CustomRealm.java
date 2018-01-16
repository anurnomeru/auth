package com.raythonsoft.auth.shiro.realm;

import com.github.pagehelper.util.StringUtil;
import com.raythonsoft.auth.model.Resources;
import com.raythonsoft.auth.model.Role;
import com.raythonsoft.auth.model.User;
import com.raythonsoft.auth.service.ResourcesService;
import com.raythonsoft.auth.service.RoleService;
import com.raythonsoft.auth.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by Anur IjuoKaruKas on 2018/1/8.
 * Description :
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private RoleService roleService;

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

        // fixme 无密认证

        User user = userService.findBy("username", username);
        user = new User();
        user.setUsername("1");
        user.setPassword("1");
        user.setLock(false);
        user.setSalt("1");
        if (null == user) {
            throw new UnknownAccountException();
        }

        if (user.getLock()) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(
                username,
                password,
                ByteSource.Util.bytes(user.getSalt()),
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
        User user = userService.findBy("username", username);
        Integer userId = user.getId();

        List<Resources> permissionList = resourcesService.findAllByUserId(userId);
        Set<String> permissionSet = new HashSet<>();
        for (Resources next : permissionList) {
            String permissionName = next.getName();
            if (StringUtil.isNotEmpty(permissionName)) {
                permissionSet.add(permissionName);
            }
        }

        List<Role> roleList = roleService.findAllByUserId(userId);
        Set<String> roleSet = new HashSet<>();
        for (Role next : roleList) {
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
