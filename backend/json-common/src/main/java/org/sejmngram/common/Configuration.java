package org.sejmngram.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration {

	private static final String COMMON_OUTPUT_FILENAME_DATE_PATTERN = "common.json.filename.datePattern";
	private static final String COMMON_OUTPUT_FILENAME_ENDING = "common.json.filename.ending";
	private static final String SEJMOMETR_URL = "sejmometr.url";
	private static final String SEJMOMETR_ACCEPT_HEADER = "sejmometr.acceptHeader";
	private static final String SEJMOMETR_ID_LIST = "sejmometr.idList";
	private static final String SEJMOMETR_IDS_DELIMETER = ",";
	private static final String SEJMOMETR_IDS_RANGE_DELIMETER = "-";
	
	private Properties properties;
	
	private Configuration() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("common.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class SingletonHolder { 
        public static final Configuration INSTANCE = new Configuration();
	}
	
	public static Configuration getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String getCommonOutputFilenameDatePattern() {
		return properties.getProperty(COMMON_OUTPUT_FILENAME_DATE_PATTERN);
	}

	public String getCommonOutputFilenameEnding() {
		return properties.getProperty(COMMON_OUTPUT_FILENAME_ENDING);
	}
	
	public String getSejmometrUrl() {
		return properties.getProperty(SEJMOMETR_URL);
	}
	
	public String getSejmometrAcceptHeader() {
		return properties.getProperty(SEJMOMETR_ACCEPT_HEADER);
	}
	
	public List<Integer> getSejmometrIds() {
		List<Integer> result = new ArrayList<Integer>();
		String property = properties.getProperty(SEJMOMETR_ID_LIST);
		if (property == null || property.isEmpty()) {
			return new ArrayList<Integer>();
		}
		for (String id : property.split(SEJMOMETR_IDS_DELIMETER)) {
			if (id.contains(SEJMOMETR_IDS_RANGE_DELIMETER)) {
				String[] ids = id.split(SEJMOMETR_IDS_RANGE_DELIMETER);
				int minId = Integer.parseInt(ids[0]);
				int maxId = Integer.parseInt(ids[1]);
				for (int i = minId; i <= maxId; ++i) {
					result.add(i);
				}
			} else {
				result.add(Integer.parseInt(id));
			}
		}
		return result;
	}

}
