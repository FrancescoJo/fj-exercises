/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.interceptor;

import com.francescojo.simplehttpsvr.enums.Globals;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.Callable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
public class TimeLoggingInterceptor implements HandlerInterceptor {
    private static final Callable<Long> TIME_GETTER = System::nanoTime;

    private final LocaleResolver lr;

    public TimeLoggingInterceptor(LocaleResolver localeResolver) {
        this.lr = localeResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        request.setAttribute(Globals.EXECUTION_TIME.tag, TIME_GETTER.call());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {
        if (null == modelAndView) {
            return;
        }

        long startTime = (Long) request.getAttribute(Globals.EXECUTION_TIME.tag);
        long endTime   = TIME_GETTER.call();
        request.removeAttribute(Globals.EXECUTION_TIME.tag);

        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(lr.resolveLocale(request));
        df.applyLocalizedPattern("#,###.###");

        String delta = df.format((double) (endTime - startTime) / 1000000);
        modelAndView.addObject(Globals.EXECUTION_TIME.tag, delta);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {

    }
}
