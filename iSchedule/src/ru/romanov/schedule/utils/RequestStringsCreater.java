package ru.romanov.schedule.utils;

public abstract class RequestStringsCreater {
	private static String REQUEST_VERSION = "'0.1'";
	private static String REQUEST_TYPE_AUTH = "'auth'";
	private static String REQUEST_TYPE_UPDATE = "'update'";
	private static String REQUEST_TYPE_CHECK_UPDATES = "'last_update_info'";
	//public static String MY_URI = "http://172.16.0.2:19090/main";
	public static String createAuthRequestSting(String login, String pass) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request version=").append(REQUEST_VERSION).append(" type=").append(REQUEST_TYPE_AUTH).append(">");
		sb.append("<login>").append(login).append("</login>");
		sb.append("<pass>").append(pass).append("</pass>");
		sb.append("</request>");
		return sb.toString();
	}
	
	public static String createUpdateString (String token) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request version=").append(REQUEST_VERSION).append(" type=").append(REQUEST_TYPE_UPDATE).append(">");
		sb.append("<token>").append(token).append("</token>");
		sb.append("</request>");
		return sb.toString();
	}
	
	public static String createCheckUpdateString (String token) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request version=").append(REQUEST_VERSION).append(" type=").append(REQUEST_TYPE_CHECK_UPDATES).append(">");
		sb.append("<token>").append(token).append("</token>");
		sb.append("</request>");
		return sb.toString();
	}

}
