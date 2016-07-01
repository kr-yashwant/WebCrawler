package com.util;

public class UrlUtil {
	public static String sanitiseUrl(String url) {
		if(url.lastIndexOf('#') == (url.length())) {
			url = url.substring(0, url.lastIndexOf('#'));
		}
		return url;
	}

}
