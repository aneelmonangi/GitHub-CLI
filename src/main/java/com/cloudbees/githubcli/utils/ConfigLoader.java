package com.cloudbees.githubcli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
	private static ConfigLoader instance;
	private final Properties properties;

	private ConfigLoader() {
		properties = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("Sorry, unable to find config.properties");
			}
			properties.load(input);
		} catch (IOException ex) {
			throw new RuntimeException("Error loading configuration file", ex);
		}
	}

	public static ConfigLoader getInstance() {
		if (instance == null) {
			instance = new ConfigLoader();
		}
		return instance;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
