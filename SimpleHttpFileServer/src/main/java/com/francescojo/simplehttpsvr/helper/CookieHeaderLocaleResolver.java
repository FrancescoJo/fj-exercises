/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.helper;

import com.francescojo.simplehttpsvr.enums.Cookies;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2016
 */
public class CookieHeaderLocaleResolver implements LocaleResolver {
    private final CookieHelper   cookieHelper;
    private final LocaleResolver headerResolver;

    public CookieHeaderLocaleResolver(CookieHelper cookieHelper, LocaleResolver headerLocaleResolver) {
        this.cookieHelper = cookieHelper;
        this.headerResolver = headerLocaleResolver;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Cookie prefLanguage = cookieHelper.find(request, Cookies.PREFERRED_LANGUAGE);
        Locale locale;
        if (null != prefLanguage) {
            locale = Locale.forLanguageTag(cookieHelper.valueOf(Cookies.PREFERRED_LANGUAGE, prefLanguage));
        } else {
            locale = headerResolver.resolveLocale(request);
        }

        LocaleContextHolder.setLocale(locale);
        return locale;
    }

    public void setLocale(HttpServletRequest request, HttpServletResponse response) {
        setLocale(request, response, LocaleContextHolder.getLocale());
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        Cookie prefLanguage = cookieHelper.find(request, Cookies.PREFERRED_LANGUAGE);
        if (null == prefLanguage) {
            response.addCookie(cookieHelper.create(Cookies.PREFERRED_LANGUAGE, locale.toLanguageTag()));
        }
    }
}
