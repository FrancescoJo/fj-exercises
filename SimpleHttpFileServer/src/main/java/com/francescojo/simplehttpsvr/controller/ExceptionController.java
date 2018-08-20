/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.controller;

import com.francescojo.simplehttpsvr.exception.NotFoundException;
import com.francescojo.simplehttpsvr.helper.FooterRenderHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Oct - 2016
 */
@ControllerAdvice
public class ExceptionController {
    private final FooterRenderHelper footerHelper;

    public ExceptionController(FooterRenderHelper footerHelper) {
        this.footerHelper = footerHelper;
    }

    @ExceptionHandler(NotFoundException.class)
    public String notFoundException(Model model, HttpServletResponse response, NotFoundException e) {
        return handleException(model, response, e);
    }

    private String handleException(Model model, HttpServletResponse response, Exception e) {
        ResponseStatus status = e.getClass().getAnnotation(ResponseStatus.class);
        response.setStatus(status.code().value());

        model.addAttribute("code", status.code().value());
        model.addAttribute("reason", status.reason());
        model.addAttribute("message", e.getMessage());
        footerHelper.renderFooter(model);

        return "error";
    }
}
