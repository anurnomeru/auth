package com.raythonsoft.auth.shiro.realm;

import com.raythonsoft.auth.model.User;
import com.raythonsoft.auth.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Anur IjuoKaruKas on 2018/1/8.
 * Description :
 */
public class CostomRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

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
        return null;
    }
}
