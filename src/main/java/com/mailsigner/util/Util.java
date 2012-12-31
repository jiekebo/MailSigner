package com.mailsigner.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Util {
	
	/**
	 * Loads a map from file
	 * @param filename Location of the file
	 * @param delimiter Delimiter between key and value used in the file
	 * @return Map containing the key-value mappings
	 * @throws Exception
	 */
	public static Map<String, String> readPropertiesFileAsMap(InputStream inputStream, String delimiter) throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.trim().length() == 0)
				continue;
			if (line.charAt(0) == '#')
				continue;
			int delimPosition = line.indexOf(delimiter);
			String key = line.substring(0, delimPosition).trim();
			String value = line.substring(delimPosition + 1).trim();
			map.put(key, value);
		}
		reader.close();
		return map;
	}
	
}
