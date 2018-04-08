package com.raythonsoft.sso.service;

import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

/**
 * Created by Anur IjuoKaruKas on 2018/4/8.
 * Description :
 */
@Service
public interface SessionService {

    /**
     * 判断session是否有效（redis是否过期）
     *
     * @param session
     * @return
     */
    boolean isEffective(Session session);

    /**
     * 维护shiroSession，在这里，将进行下列操作：
     * <!-- shiroSession在会话创建时就已经创建 >
     * <p>
     * 1、我们需要把它的状态改为在线，并重新序列化，存进Redis中
     * 2、然后将这个sessionId 存进 ServerSessionIds 这个列表进行维护
     * 3、创建一个校验code（checkCode），维护在ServerSessionId下
     * 4、将校验code持久化到redis
     * <p>
     *
     * @param session
     */
    void sessionEffective(Session session);
}
