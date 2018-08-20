/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.helper;

import com.francescojo.simplehttpsvr.enums.Globals;
import com.francescojo.simplehttpsvr.model.ApplicationConfig;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 26 - Oct - 2016
 */
public class FooterRenderHelper {
    private final ApplicationConfig config;

    public FooterRenderHelper(ApplicationConfig config) {
        this.config = config;
    }

    public void renderFooter(ModelAndView modelAndView) {
        ZonedDateTime     now    = ZonedDateTime.now();
        Locale            locale = LocaleContextHolder.getLocale();
        DateTimeFormatter dtf    = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);

        modelAndView.addObject(Globals.SERVER_NAME.tag, config.getServer().getName());
        modelAndView.addObject(Globals.SERVER_DATE.tag, now.format(dtf));
        modelAndView.addObject(Globals.SERVER_TIMEZONE.tag, now.getZone().getId());
    }

    public void renderFooter(Model model) {
        ZonedDateTime     now    = ZonedDateTime.now();
        Locale            locale = LocaleContextHolder.getLocale();
        DateTimeFormatter dtf    = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);

        model.addAttribute(Globals.SERVER_NAME.tag, config.getServer().getName());
        model.addAttribute(Globals.SERVER_DATE.tag, now.format(dtf));
        model.addAttribute(Globals.SERVER_TIMEZONE.tag, now.getZone().getId());
    }
}
