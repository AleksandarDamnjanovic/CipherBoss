package com.aleksandar_damnjanovic.cipher_boss_java.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Strings {

	public static String LANGUAGE_ENGLISH="english";
	
	private String manual="";
	
	public Strings(String language) {
		
		if(language.equals(LANGUAGE_ENGLISH))
			manual=loadContent(new File("./res/Text/Manual_english.txt"));
		
	}
	
	public String getManual() {
		if(manual.contains("%%%%%"))
			manual=manual.replace("%%%%%", "\n\n");
		
		return manual;
	}
	
	private static String loadContent(File file){
		byte data[]=new byte[(int)file.length()];
		try {
			FileInputStream fis=new FileInputStream(file);
			fis.read(data, 0, data.length);
			fis.close();
			return new String(data,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}