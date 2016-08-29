/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.github.francescojo.awesome;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Configuration
@MapperScan("com.github.francescojo.awesome.sqlmap")
public class ApplicationConfig extends WebMvcConfigurerAdapter {
	private static final String SERVER_NAME = "MyAwesomeApp";
	private static final String DATABASE_USER = "MyAwesome";
	private static final String DATABASE_PASS = "O6jPqMsNdVGv5A1M";
	
	private static final int SERVER_PORT = 8668;

	@Bean
	public DataSource dataSource() {
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://localhost:3306/myAwesomeApp");
		dataSource.setUsername(DATABASE_USER);
		dataSource.setPassword(DATABASE_PASS);
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

	//	public static final String ENV_KEY_HOME = "APPDEPLOY_HOME";
//	public static final String ENV_KEY_ADMIN_ENABLED = "APPDEPLOY_ADMIN_ENABLED";
//
//	private static final Log LOGGER = LogFactory.getLog(ApplicationConfig.class);
//
//	// Unmodifiable constants
//	public static final String ICON_FILE_WEB_PATH = "/img/icons";
//	private static final String DEFAULT_WORKSPACE_PATH_NAME = "appdeploy";
//	private static final String DEFAULT_ICON_FILE_PATH_NAME = "icons";
//	private static final String DEFAULT_BINARY_FILE_PATH_NAME = "bintray";
//	private static final String DEFAULT_DATABASE_NAME = "appdeploy";
//
//	// System environment modifiable constants
//	private static final int SERVER_PORT = 8668;
//	private static final String SERVER_NAME = "AppDeploy";
//
//	static final String HOME_PATH;
//	static final String DATABASE_PATH;
//	static final String DATABASE_USER;
//	static final String DATABASE_PASSWORD;
//	static final String ICON_FILE_BASE_PATH;
//	static final String BINARY_FILE_BASE_PATH;
//
//	static {
//		String home = System.getenv(ENV_KEY_HOME);
//		if (StringUtils.isEmpty(home)) {
//			home = System.getProperty("user.home") + "/" + DEFAULT_WORKSPACE_PATH_NAME;
//			LOGGER.warn("No " + ENV_KEY_HOME + " environment variable defined. Using default directory: " + home);
//		}
//		HOME_PATH = home;
//		DATABASE_PATH = HOME_PATH + "/" + DEFAULT_DATABASE_NAME;
//		LOGGER.info("Using " + DATABASE_PATH + " as database");
//		ICON_FILE_BASE_PATH = HOME_PATH + "/" + DEFAULT_ICON_FILE_PATH_NAME;
//		BINARY_FILE_BASE_PATH = HOME_PATH + "/" + DEFAULT_BINARY_FILE_PATH_NAME;
//
//		Pair<String, String> dbAuthInfo = Application.getDbEnvironmentValue();
//		DATABASE_USER = dbAuthInfo.getLeft();
//		DATABASE_PASSWORD = dbAuthInfo.getRight();
//	}
//
//	@Bean
//	public LocaleResolver localeResolver() {
//		CookieLocaleResolver resolver = new CookieLocaleResolver();
//		resolver.setDefaultLocale(Locale.US);
//		resolver.setCookieName("myLocale");
//		resolver.setCookieMaxAge(31536000);
//		return resolver;
//	}
//
//	@Bean
//	public MessageSource messageSource() {
//		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//		messageSource.setBasename("classpath:strings/messages");
//		messageSource.setDefaultEncoding(Charsets.UTF_8.toString());
//		return messageSource;
//	}
//
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/static/img/" + DEFAULT_ICON_FILE_PATH_NAME + "/**")
//				.addResourceLocations("file://" + ICON_FILE_BASE_PATH + "/");
//		registry.addResourceHandler("/static/" + DEFAULT_BINARY_FILE_PATH_NAME + "/**")
//				.addResourceLocations("file://" + BINARY_FILE_BASE_PATH + "/");
//	}
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin");
//	}

	private List<Resource> loadResources(ResourceLoader rl, String resourcePath) throws IOException {
		Resource[] rs = ResourcePatternUtils.getResourcePatternResolver(rl).getResources(resourcePath);
		return Arrays.asList(rs);
	}
}