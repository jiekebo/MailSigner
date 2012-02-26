package com.mailsigner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Test {
	
	public Test() {
		URL in = getClass().getResource("/wordprocessor.properties");
		try {
			FileReader reader = new FileReader(in.getPath());
			Properties properties = new Properties();
			properties.load(reader);
			String boldProperty = properties.getProperty("italicIcon");
			System.out.println(boldProperty);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		System.out.println(test);
	}
}