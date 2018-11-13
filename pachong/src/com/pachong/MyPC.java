package com.pachong;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyPC {
	
	//链接：https://github.com/FIRHQ/?page=1
	public static void main(String[] args) throws Exception {
		
		String url = "https://github.com/FIRHQ/";
		
		String[] urls = new String[MyPC.getPage(url)];
		
		for (int i = 0; i < urls.length; i++) {
			urls[i] = url + "?page=" + (i+1);
		}
		
		ArrayList<String> html = MyPC.getHtml(urls);
		
		ArrayList<ProName> pros = MyPC.getPros(html);
		
		
		
		
		for(ProName p : pros) {
			System.out.println(p);
		}
		
	}
	//获取到网页内容并截取项目名跟星级所在的li标签存放到list每一个list对应一个项目，并返回这个list集合
	public static ArrayList<String> getHtml(String[] ur) throws Exception {
		
		ArrayList<String> list = new ArrayList<String>();

		InputStream is = null;
		BufferedReader br = null;
		
		for (int i = 0; i < ur.length; i++) {
			URL url = new URL(ur[i]);
			
			HttpURLConnection httpURL = (HttpURLConnection)url.openConnection();
			
			is = httpURL.getInputStream();
			
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			StringBuilder sb = new StringBuilder();
			
			String line;
			
			while ((line = br.readLine()) != null) {
				
				sb.append(line);

			}
			
			String html = sb.toString();
			
			Pattern pattern = Pattern.compile("<li.+?col-12.+?>(.+?)</li>");
			
			Matcher m = pattern.matcher(html);
			
			while (m.find()) {
				list.add(m.group());
			}
		}
		
		is.close();
		br.close();
		
		return list;
		
	}
	
	//传入获取到的标签集合，解析里面的项目名跟星级，将项目名跟对象封装到一个对象中然后再添加到list集合中并返回这个list
	public static ArrayList<ProName> getPros(ArrayList<String> html) {
		ArrayList<ProName> pros = new ArrayList<ProName>();
		
		for(String li : html) {
			String[] split = li.split("octicon-star");
			if(split.length == 1) {
				String star = "没有星级";
				Pattern p = Pattern.compile("<a.+?codeRepository.+?>(.+?)</a>");
				Matcher m = p.matcher(split[0]);
				while (m.find()) {
					String name = m.group(1).trim();
					pros.add(new ProName(name, star));
				}
			} else {
				
				String name = "";
				String star = "";
				
				Pattern p1 = Pattern.compile("<a.+?codeRepository.+?>(.+?)</a>");
				
				Matcher nameM = p1.matcher(split[0]);
				
				while(nameM.find()) {
					name = nameM.group(1).trim();
				}
				
				Pattern p2 = Pattern.compile("6z.+?</svg>(.+?)</a>");
				
				Matcher starM = p2.matcher(split[1]);
				
				while(starM.find()) {
					star = starM.group(1).trim();
				}
				
				pros.add(new ProName(name, star));
				
			}
		}
		
		return pros;
	}
	

	public static int getPage(String ur) throws Exception {
		
		URL url = new URL(ur);
		
		HttpURLConnection httpURL = (HttpURLConnection)url.openConnection();
		
		InputStream is = httpURL.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
		
		StringBuilder sb = new StringBuilder();
		
		String line;
		
		while ((line = br.readLine()) != null) {
			
			sb.append(line);

		}
		
		Pattern p = Pattern.compile(".+?pagination.+?>(.+?)</div>");
		
		Matcher matcher = p.matcher(sb);
		
		String urls = null;
		
		while(matcher.find()) {
			urls = matcher.group(1);
		}
		
		String[] split = urls.split("</a>");
		
		is.close();
		
		br.close();
		
		return split.length;
	}
}
