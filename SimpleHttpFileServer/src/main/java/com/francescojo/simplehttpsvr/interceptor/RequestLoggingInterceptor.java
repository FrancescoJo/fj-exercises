/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.interceptor;

import com.francescojo.simplehttpsvr.enums.Globals;
import com.google.common.base.Strings;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
public class RequestLoggingInterceptor implements HandlerInterceptor {
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

        modelAndView.addObject(Globals.REQUEST_IP.tag, getClientIpAddr(request));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception { }

    private static String getClientIpAddr(HttpServletRequest request) {
        String[] headers = new String[] { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

        String ip;
        for (String header : headers) {
            ip = request.getHeader(header);
            if (isIpKnown(ip)) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private static boolean isIpKnown(String ipStr) {
        return !Strings.isNullOrEmpty(ipStr) && !"unknown".equalsIgnoreCase(ipStr);
    }
}
