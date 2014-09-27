package com.cao.xinyao.guzhang;

import java.util.List;

import org.apache.commons.httpclient.Cookie;

/**
 * @author caoyaojun
 */
public class CookieUtil {

    public static String getCookie(List<Cookie> cookies) {
        StringBuilder cookieBuilder = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieBuilder.append(cookie.getName() + "=" + cookie.getValue() + ";");
        }
        String cookie = cookieBuilder.substring(0, cookieBuilder.length() - 1);
        System.out.println("this request cookie is  :" + cookie);
        return cookie;
    }


}
