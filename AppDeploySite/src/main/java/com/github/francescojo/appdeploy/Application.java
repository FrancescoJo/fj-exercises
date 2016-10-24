/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;

import com.github.francescojo.appdeploy.util.EnumUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	private static final String ENV_APPDEPLOY_DB_USER = "APPDEPLOY_DB_USER";
	private static final String ENV_APPDEPLOY_DB_PASSWORD = "APPDEPLOY_DB_PASSWORD";
	private static final ThreadLocal<DateFormat> DATE_FORMATTER = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			Locale currentLocale = Application.getInstance().getCurrentLocale();
			DateFormat fmt = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, currentLocale);
			return fmt;
		}
	};

	static ConfigurableApplicationContext applicationContext;
	
	public static Application getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public static void main(String[] args) throws Exception {
		Options options = buildOptions();
		if (args.length == 0) {
			printUsage(options);
			return;
		}

		Mode serverMode;
		try {
			CommandLine cmd = new DefaultParser().parse(options, args);
			String modeArg = cmd.getOptionValue("m").trim();
			serverMode = EnumUtils.valueOf(Mode.class, modeArg.toUpperCase(Locale.ENGLISH));
			if (null == serverMode) {
				throw new ParseException("Server mode argument " + modeArg + " is not supported");
			}
		} catch (ParseException exp) {
			System.err.println("Error: " + exp.getLocalizedMessage());
			printUsage(options);
			return;
		}
		
		switch (serverMode) {
		case START:
			startServer(args);
			break;
		case STOP:
			stopServer(args);
			break;
		case RESTART:
			restartServer(args);
			break;
		case STATUS:
			dumpServerStatus();
			break;
		default:
			printUsage(options);
		}
	}
	
	private static void startServer(String[] args) {
		if (!isStartable()) {
			return;
		}
		
		if (null != applicationContext) {
			System.err.println("Server has already started. Exiting.");
			dumpServerStatus();
			return;
		}
		
		applicationContext = SpringApplication.run(Application.class, args);
	}
	
	private static boolean isStartable() {
		Pair<String, String> dbAuthInfo = getDbEnvironmentValue();
		if (StringUtils.isEmpty(dbAuthInfo.getLeft()) || StringUtils.isEmpty(dbAuthInfo.getRight())) {
			System.err.println("ERROR: Environment value " + ENV_APPDEPLOY_DB_USER + " or " + ENV_APPDEPLOY_DB_PASSWORD + " is not defined.");
			return false;
		}
		
		return true;
	}

	private static void stopServer(String[] args) {
		// TODO: re-implement this feature
//		if (null != applicationContext) {
//			applicationContext.close();
//		}
	}

	private static void restartServer(String[] args) {
		// TODO: re-implement this feature
//		stopServer(args);
//		startServer(args);
	}

	private static void dumpServerStatus() {
		// TODO: re-implement this feature
//		if (null == applicationContext) {
//			System.out.println("Server is not running");
//			return;
//		}
//
//		System.out.println("Dumping server status:");
//		System.out.println("  Server started at         : " + new Date(applicationContext.getStartupDate()).toString());
//		System.out.println("  Server id                 : " + applicationContext.getId());
//		System.out.println("Server configuration:");
//		applicationContext.getBean(ApplicationConfig.class).dump();
//		System.out.println("  Running system properties : ");
//		Map<String, Object> env = applicationContext.getEnvironment().getSystemEnvironment();
//		for (Map.Entry<String, Object> envEntry : env.entrySet()) {
//			System.out.println("    " + envEntry.getKey() + " = " + envEntry.getValue());
//		}
	}

	private static void printUsage(Options options) {
		String header = "\n\n";
		String footer = "";

		HelpFormatter formatter = new HelpFormatter();
		formatter.setOptionComparator((o1, o2) -> {
			if (o1.isRequired() && !o2.isRequired()) {
				return -1;
			} else if (!o1.isRequired() && o2.isRequired()) {
				return 1;
			} else {
				return o1.getOpt().compareTo(o2.getOpt());
			}
		});
		formatter.printHelp(Application.class.getSimpleName(), header, options, footer, true);

		isStartable();
	}

	private static Options buildOptions() {
		Options options = new Options();
		for (AppOption appOption : AppOption.values()) {
			options.addOption(Option.builder(appOption.shortName)
				.longOpt(appOption.longName)
				.hasArg(appOption.argCount > 0)
				.argName(appOption.argName)
				.required(appOption.isMandatory)
				.desc(appOption.description)
				.build());
		}
		return options;
	}
	
	static Pair<String, String> getDbEnvironmentValue() {
		return Pair.of(System.getenv(ENV_APPDEPLOY_DB_USER), System.getenv(ENV_APPDEPLOY_DB_PASSWORD));
	}

	@Override
	protected final SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	public String getString(String textKey) {
		return getString(textKey, (Object[]) null);
	}

	public String getString(String textKey, Object...args) {
		if (null == applicationContext) {
			return StringUtils.EMPTY;
		}

		MessageSource messageSrc;
		String defaultMessage = "Empty message: " + textKey;
		try {
			messageSrc = applicationContext.getBean(MessageSource.class);
		} catch (BeansException e) {
			return defaultMessage;
		}

		return messageSrc.getMessage(textKey, args, defaultMessage, getCurrentLocale());
	}

	// TODO Utilise client locale instead 
	public Locale getCurrentLocale() {
		return Locale.US;
	}
	
	public DateFormat getDateFormatter() {
		return DATE_FORMATTER.get();
	}

	public ApplicationConfig getConfig() {
		return applicationContext.getBean(ApplicationConfig.class);
	}
	
	public void stopGracefully() {
		System.out.println("SHUT DOWN");
		SpringApplication.exit(applicationContext, () -> 0);
	}

	enum Mode {
		START,
		STOP,
		RESTART,
		STATUS,
		UNDEFINED;
	}
	
	enum AppOption {
		MODE("m", "mode", "mode", 1, true, "Server mode control values. Must be one of these: START, STOP, RESTART or STATUS."),
		SETTINGS("sf", "settings", "path", 1, false, "File path to override default settings of programme, read documentation for default settings syntax. Must be JSON format."),
		HELP("h", "help", "", 0, false, "Prints this help message."),
		;

		final String shortName;
		final String longName;
		final String argName;
		final int argCount;
		final boolean isMandatory;
		final String description;
		
		AppOption(String s1, String s2, String s3, int i1, boolean b1, String s4) {
			this.shortName = s1;
			this.longName = s2;
			this.argName = s3;
			this.argCount = i1;
			this.isMandatory = b1;
			this.description = s4;
		}
	}
	
	private static class InstanceHolder {
		static final Application INSTANCE = new Application();
	}
}
