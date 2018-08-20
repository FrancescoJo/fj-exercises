/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.interceptor;

import com.francescojo.simplehttpsvr.helper.CookieHeaderLocaleResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - 10월 - 2016
 */
public class CookieInterceptor implements HandlerInterceptor {
    private final CookieHeaderLocaleResolver localeCookieMgr;

    public CookieInterceptor(CookieHeaderLocaleResolver cookieHeaderLocaleResolver) {
        this.localeCookieMgr = cookieHeaderLocaleResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        localeCookieMgr.setLocale(request, response);
    }
}
