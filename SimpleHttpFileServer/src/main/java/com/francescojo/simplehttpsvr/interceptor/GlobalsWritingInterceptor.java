/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.interceptor;

import com.francescojo.simplehttpsvr.enums.Globals;
import com.francescojo.simplehttpsvr.helper.FooterRenderHelper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Oct - 2016
 */
public class GlobalsWritingInterceptor implements HandlerInterceptor {
    public static final String STATIC_DOWNLOAD_PREFIX = ".simplehttpsvr-static";
    private static final String URL_STATIC_BASE = "/" + STATIC_DOWNLOAD_PREFIX + "/";
    private static final String URL_STATIC_ICO = URL_STATIC_BASE + "ico/";

    private final FooterRenderHelper footerHelper;

    public GlobalsWritingInterceptor(FooterRenderHelper footerHelper) {
        this.footerHelper = footerHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        if (null == modelAndView) {
            return;
        }

        footerHelper.renderFooter(modelAndView);
        modelAndView.addObject(Globals.PATH_STATIC.tag, URL_STATIC_BASE);
        modelAndView.addObject(Globals.PATH_STATIC_ICONS.tag, URL_STATIC_ICO);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {}
}
