package com.aleksandar_damnjanovic.cipher_boss_java.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings {

	private static String FILE_NAME = "Settings";
	public static String DEFAULT_DIRECTORY = "DEFAULT_DIRECTORY";
	public static String DEFAULT_KEY="DEFAULT_KEY";
	public static String DEFAULT_LANGUAGE="DEFAULT_LANGUAGE";

	/**
	 * As name implies, it's writes setting and value for it
	 * @param setting
	 * @param value
	 */
	public static void writeSetting(String setting, String value) {
		List<String> list = getSettings();
		write(setting, value, list);
	}
	
	/**
	 * Returns value of particular setting
	 * @param setting which value should be returned
	 * @return
	 */
	public static String getSettingValue(String setting) {
		List<String> list = getSettings();
		
		for(String s: list)
			if(s.contains(setting))
				return s.split("[|]{5}")[1];
		
		return "";
	}

	/**
	 * Write full content of list
	 * @param setting is actual setting that should be written
	 * @param value is value to be written for particular setting
	 * @param list is list of setting already present in the settings file
	 */
	private static void write(String setting, String value, List<String> list) {
		try {
			FileOutputStream fos = new FileOutputStream(FILE_NAME);
	
			int index=-1;
			for(int i=0;i<list.size();i++) {
				if(list.get(i).contains(setting)) {
					index=i;
				}
			}
			
			if(index!=-1)
				list.remove(index);
			
			list.add(setting + "|||||" + value);

			String content = "";
			for (String s : list)
				content += s+"\n";

			byte[] data = content.getBytes();
			fos.write(data, 0, data.length);
			fos.flush();
			fos.close();
			list = null;
			data = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return list with every line from the file
	 * 
	 * @return
	 */
	public static List<String> getSettings() {
		File file = new File(FILE_NAME);

		if (!file.exists())
			return new ArrayList<String>();

		String content = readContent();

		if (content.equals(""))
			return new ArrayList<String>();

		String lines[] = content.split("\n");
		List<String> list = new ArrayList<String>(Arrays.asList(lines));
		return list;
	}

	/**
	 * Returns entire content of the file
	 * 
	 * @return
	 */
	private static String readContent() {
		String content = "";
		try {
			File file = new File(FILE_NAME);
			FileInputStream fis = new FileInputStream(FILE_NAME);
			byte[] data = new byte[(int) file.length()];
			fis.read(data, 0, data.length);
			fis.close();
			file = null;
			content = new String(data, "UTF-8");
			data = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}