/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.controller;

import com.francescojo.simplehttpsvr.exception.NotFoundException;
import com.francescojo.simplehttpsvr.helper.FooterRenderHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2016
 */
public class ExceptionControllerTest {
    private FooterRenderHelper  mockFooterHelper;
    private ExceptionController controller;

    @Before
    public void setUp() throws Exception {
        this.mockFooterHelper = mock(FooterRenderHelper.class);
        this.controller = new ExceptionController(mockFooterHelper);
    }

    @Test
    public void testHandleNotFoundException() throws Exception {
        HttpServletResponse response = mock(HttpServletResponse.class);
        ExtendedModelMap    model    = new ExtendedModelMap();
        NotFoundException   ex       = new NotFoundException("Not found");
        ResponseStatus      status   = ex.getClass().getAnnotation(ResponseStatus.class);
        String              pageName = controller.notFoundException(model, response, ex);

        assertEquals(model.get("code"), status.code().value());
        assertEquals(model.get("reason"), status.reason());
        assertEquals(model.get("message"), ex.getMessage());
        assertEquals(pageName, "error");
    }

}