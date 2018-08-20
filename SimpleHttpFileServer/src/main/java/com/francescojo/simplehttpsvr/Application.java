/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr;

import com.francescojo.simplehttpsvr.helper.CookieHeaderLocaleResolver;
import com.francescojo.simplehttpsvr.helper.CookieHelper;
import com.francescojo.simplehttpsvr.helper.FileListHelper;
import com.francescojo.simplehttpsvr.helper.FooterRenderHelper;
import com.francescojo.simplehttpsvr.interceptor.CookieInterceptor;
import com.francescojo.simplehttpsvr.interceptor.GlobalsWritingInterceptor;
import com.francescojo.simplehttpsvr.interceptor.RequestLoggingInterceptor;
import com.francescojo.simplehttpsvr.interceptor.TimeLoggingInterceptor;
import com.francescojo.simplehttpsvr.model.ApplicationConfig;
import com.francescojo.simplehttpsvr.util.NullSafeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.io.File;
import java.util.Collections;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Oct - 2016
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    public static class DefaultConfig extends WebMvcConfigurerAdapter {
        private static final Logger LOG = LoggerFactory.getLogger(DefaultConfig.class);

        private final ApplicationContext context;

        @Autowired
        public DefaultConfig(ApplicationContext context) {
            this.context = context;
        }

        @Bean
        public ApplicationConfigHelper configHelper() {
            return new ApplicationConfigHelper();
        }

        @Bean
        public FileListHelper fileListHelper() {
            return new FileListHelper(new File("").getAbsoluteFile());
        }

        @Bean
        public CookieHelper cookieHelper() {
            return new CookieHelper();
        }

        @Bean
        public FooterRenderHelper footerRenderHelper(ApplicationConfig config) {
            return new FooterRenderHelper(config);
        }

        // VM Options -DconfigFile="????"
        @Bean
        public ApplicationConfig applicationConfig(ApplicationConfigHelper configHelper, Environment environment) {
            File extConfigFile = new File(NullSafeUtils.get(environment.getProperty("configFile")));

            ApplicationConfig config = configHelper.loadConfig(extConfigFile);
            if (null == config) {
                LOG.error("Cannot load configuration file {}.", extConfigFile.getAbsolutePath());
                System.exit(1);
            }

            return config;
        }

        @Bean
        public EmbeddedServletContainerFactory servletContainerFactory(ApplicationConfig config) {
            TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
            tomcatFactory.setTomcatConnectorCustomizers(Collections.singletonList(connector -> {
                connector.setProperty("Server", config.getServer().getName());
                connector.setPort(config.getServer().getPort());
                connector.setXpoweredBy(false);
            }));

            tomcatFactory.setPersistSession(false);
            return tomcatFactory;
        }

        @Bean
        public LocaleResolver localeResolver(CookieHelper cookieHelper) {
            return new CookieHeaderLocaleResolver(cookieHelper, new AcceptHeaderLocaleResolver());
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new GlobalsWritingInterceptor(context.getBean(FooterRenderHelper.class)));
            registry.addInterceptor(new TimeLoggingInterceptor(context.getBean(LocaleResolver.class)));
            registry.addInterceptor(new RequestLoggingInterceptor());
            registry.addInterceptor(new CookieInterceptor(context.getBean(CookieHeaderLocaleResolver.class)));
        }
    }
}
