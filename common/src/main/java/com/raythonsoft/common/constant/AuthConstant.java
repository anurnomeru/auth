package com.raythonsoft.common.constant;

/**
 * Created by Anur IjuoKaruKas on 2018/1/18.
 * Description :
 */
public class AuthConstant {
    public static final class SSO_PROPERTY {
        public static String getPropertyFileName() {
            return "sso";
        }

        public static final String SSO_PROPERTY_TYPE = "sso.type";
        public static final String SSO_PROPERTY_SERVER_URL = "sso.server.url";
        public static final String SSO_PROPERTY_APPID = "sso.appid";
        public static final String SSO_PROPERTY_NAME = "sso.app.name";// 这个看起来和上面的是一个东西
    }

    public static final String REQUEST_PARAM_CODE = "code";
    public static final String REQUEST_PARAM_OSS_CODE = "check_code";
    public static final String REQUEST_PARAM_OSS_USERNAME = "username";

    public static final String URL_SSO_CODE = "/sso/code";
    public static final String URL_SSO_LOGIN = "/sso/login";
    public static final String URL_SSO_INDEX = "/sso/index";

}
