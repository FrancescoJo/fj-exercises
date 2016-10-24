/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.github.francescojo.appdeploy.bo.AppBo;
import com.github.francescojo.appdeploy.bo.DistStageBo;
import com.github.francescojo.appdeploy.bo.PlatformBo;
import com.github.francescojo.appdeploy.bo.UploadHistoryBo;
import com.github.francescojo.appdeploy.interceptor.AdminInterceptor;
import com.github.francescojo.appdeploy.model.Messages;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Configuration
@MapperScan("com.github.francescojo.appdeploy.sqlmap")
public class ApplicationConfig extends WebMvcConfigurerAdapter {
	public static final String ENV_KEY_HOME = "APPDEPLOY_HOME";
	public static final String ENV_KEY_ADMIN_ENABLED = "APPDEPLOY_ADMIN_ENABLED";

	private static final Log LOGGER = LogFactory.getLog(ApplicationConfig.class);

	// Unmodifiable constants
	public static final String ICON_FILE_WEB_PATH = "/img/icons";
	private static final String DEFAULT_WORKSPACE_PATH_NAME = "appdeploy";
	private static final String DEFAULT_ICON_FILE_PATH_NAME = "icons";
	private static final String DEFAULT_BINARY_FILE_PATH_NAME = "bintray";
	private static final String DEFAULT_DATABASE_NAME = "appdeploy";

	// System environment modifiable constants
	private static final int SERVER_PORT = 8668;
	private static final String SERVER_NAME = "AppDeploy";

	static final String HOME_PATH;
	static final String DATABASE_PATH;
	static final String DATABASE_USER;
	static final String DATABASE_PASSWORD;
	static final String ICON_FILE_BASE_PATH;
	static final String BINARY_FILE_BASE_PATH;

	static {
		String home = System.getenv(ENV_KEY_HOME);
		if (StringUtils.isEmpty(home)) {
			home = System.getProperty("user.home") + "/" + DEFAULT_WORKSPACE_PATH_NAME;
			LOGGER.warn("No " + ENV_KEY_HOME + " environment variable defined. Using default directory: " + home);
		}
		HOME_PATH = home;
		DATABASE_PATH = HOME_PATH + "/" + DEFAULT_DATABASE_NAME;
		LOGGER.info("Using " + DATABASE_PATH + " as database");
		ICON_FILE_BASE_PATH = HOME_PATH + "/" + DEFAULT_ICON_FILE_PATH_NAME;
		BINARY_FILE_BASE_PATH = HOME_PATH + "/" + DEFAULT_BINARY_FILE_PATH_NAME;

		Pair<String, String> dbAuthInfo = Application.getDbEnvironmentValue();
		DATABASE_USER = dbAuthInfo.getLeft();
		DATABASE_PASSWORD = dbAuthInfo.getRight();
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		return resolver;
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(Locale.US);
		resolver.setCookieName("myLocale");
		resolver.setCookieMaxAge(31536000);
		return resolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:strings/messages");
		messageSource.setDefaultEncoding(Charsets.UTF_8.toString());
		return messageSource;
	}

	@Bean
	public DataSource dataSource() {
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(org.h2.Driver.class.getName());
		dataSource.setUrl("jdbc:h2:" + DATABASE_PATH + ";MODE=MySQL");
		dataSource.setUsername(DATABASE_USER);
		dataSource.setPassword(DATABASE_PASSWORD);
		return dataSource;
	}

	@Bean
	public DatabasePopulator databasePopulator(DataSource dataSource, ResourceLoader rl) throws Exception {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		List<Resource> sqlFiles = loadResources(rl, "classpath:/sql/schema_*.sql");
		for (Resource sqlFile : sqlFiles) {
			populator.addScript(sqlFile);
		}

		populator.populate(dataSource.getConnection());
		return populator;
	}

	@Bean
	public FactoryBean<SqlSessionFactory> sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));

		return sqlSessionFactory;
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainerFactory() {
		TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
		tomcatFactory.setTomcatConnectorCustomizers(Collections.singletonList(connector -> {
			connector.setProperty("Server", SERVER_NAME);
			connector.setPort(SERVER_PORT);
			connector.setXpoweredBy(false);
		}));

		tomcatFactory.setPersistSession(false);

		return tomcatFactory;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/img/" + DEFAULT_ICON_FILE_PATH_NAME + "/**")
				.addResourceLocations("file://" + ICON_FILE_BASE_PATH + "/");
		registry.addResourceHandler("/static/" + DEFAULT_BINARY_FILE_PATH_NAME + "/**")
				.addResourceLocations("file://" + BINARY_FILE_BASE_PATH + "/");
	}

	private List<Resource> loadResources(ResourceLoader rl, String resourcePath) throws IOException {
		Resource[] rs = ResourcePatternUtils.getResourcePatternResolver(rl).getResources(resourcePath);
		return Arrays.asList(rs);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin");
	}

	@Configuration
	public static class Bo {
		@Bean
		public AppBo appBo() {
			return new AppBo();
		}

		@Bean
		public PlatformBo platformBo() {
			return new PlatformBo();
		}

		@Bean
		public DistStageBo distStageBo() {
			return new DistStageBo();
		}

		@Bean
		public UploadHistoryBo uploadHistoryBo() {
			return new UploadHistoryBo();
		}
	}

	public File getIconFileBasePath() {
		return getFileBasePathInternal(ICON_FILE_BASE_PATH);
	}
	
	public File getAppBinaryBasePath() {
		return new File(BINARY_FILE_BASE_PATH);
	}
	
	public File getAppBinaryBasePath(long appId, long stageId) {
		File appBinaryPath = new File(getAppBinaryBasePath(), appId + File.separator + stageId);
		return getFileBasePathInternal(appBinaryPath.getAbsolutePath());
	}
	
	private File getFileBasePathInternal(String path) {
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory()) {
			return dir;
		}

		if (dir.exists() && !dir.isDirectory()) {
			try {
				FileUtils.deleteDirectory(dir);
			} catch (IOException e) {
				String errMsgFormat = Messages.ERR_NO_PRIVILEGES_EXCEPTION.getText();
				throw new SecurityException(String.format(errMsgFormat, dir));
			}
		}

		mkdirs(dir);
		return dir;
	}

	public void mkdirs(File dir) {
		boolean result = dir.mkdirs();
		if (!result) {
			String errMsgFormat = Messages.ERR_NO_PRIVILEGES_EXCEPTION.getText();
			throw new SecurityException(String.format(errMsgFormat, dir));
		}
	}

	public void dump() {
		System.out.println("  Server name               : " + SERVER_NAME);
		System.out.println("  Server running port       : " + SERVER_PORT);
		System.out.println("  Server workspace dir      : " + HOME_PATH);
		System.out.println("  Database file location    : " + DATABASE_PATH);
		System.out.println("  Database username         : " + DATABASE_USER);
		String password;
		if (StringUtils.isEmpty(DATABASE_PASSWORD)) {
			password = "<Not set>";
		} else {
			password = "<Set>";
		}
		System.out.println("  Server database password  : " + password);
		System.out.println("  Icon files location       : " + ICON_FILE_BASE_PATH);
		System.out.println("  Binary files location     : " + BINARY_FILE_BASE_PATH);
	}
}
