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
	private static final String DATABASE_PROTOCOL = "jdbc";
	private static final String DATABASE_ENGINE = "mysql";
	private static final String DATABASE_HOST = "localhost";
	private static final String DATABASE_NAME = "myAwesomeApp";
	private static final String DATABASE_USER = "MyAwesome";
	private static final String DATABASE_PASS = "O6jPqMsNdVGv5A1M";
	
	private static final int SERVER_PORT = 0;
	private static final int DATABASE_PORT = 3306;

	@Bean
	public DataSource dataSource() {
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		dataSource.setUrl(String.format("%s:%s://%s:%s/%s", 
				DATABASE_PROTOCOL, DATABASE_ENGINE, DATABASE_HOST, DATABASE_PORT, DATABASE_NAME));
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

	private List<Resource> loadResources(ResourceLoader rl, String resourcePath) throws IOException {
		Resource[] rs = ResourcePatternUtils.getResourcePatternResolver(rl).getResources(resourcePath);
		return Arrays.asList(rs);
	}
}