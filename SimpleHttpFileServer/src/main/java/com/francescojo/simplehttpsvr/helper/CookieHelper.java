/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.helper;

import com.francescojo.simplehttpsvr.enums.Cookies;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2016
 */
public class CookieHelper {
    public Cookie find(@NotNull HttpServletRequest request, @NotNull Cookies cookieDef) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return null;
        }

        for (Cookie c : cookies) {
            String name = c.getName();
            if (cookieDef.name.equals(name)) {
                return c;
            }
        }

        return null;
    }

    public Cookie create(@NotNull Cookies cookieDef, @NotNull Object value) {
        return new Cookie(cookieDef.name, value.toString());
    }

    public <T> T valueOf(@NotNull Cookies cookieDef, @NotNull Cookie cookie) {
        String value = cookie.getValue();
        return (T) cookieDef.type.cast(value);
    }
}