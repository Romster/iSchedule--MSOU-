package ru.romanov.schedule.utils;

import java.util.Map;

public abstract class RequestStringsCreater {
	
	public static String CHECK_AGREE = "agree";
	public static String CHECK_DISAGREE = "disagree";
	
	
	private static String REQUEST_VERSION = "'0.1'";
	private static String REQUEST_TYPE_AUTH = "'auth'";
	private static String REQUEST_TYPE_UPDATE = "'update'";
	private static String REQUEST_TYPE_CHECK_UPDATES = "'last_update_info'";
	private static String REQUEST_TYPE_CONFIRM_CHECK = "'check'";
	
	//public static String MY_URI = "http://172.16.0.2:19090/main";
	/**
	 * Создать строку - тело запроса авторизации
	 * @param login
	 * @param pass
	 * @return
	 */
	public static String createAuthRequestSting(String login, String pass) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request version=").append(REQUEST_VERSION).append(" type=").append(REQUEST_TYPE_AUTH).append(">");
		sb.append("<login>").append(login).append("</login>");
		sb.append("<pass>").append(pass).append("</pass>");
		sb.append("</request>");
		return sb.toString();
	}
	/**
	 * Создать строку - тело запроса на загрузку изменений
	 * @param token
	 * @return
	 */
	public static String createUpdateString (String token) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request version=").append(REQUEST_VERSION).append(" type=").append(REQUEST_TYPE_UPDATE).append(">");
		sb.append("<token>").append(token).append("</token>");
		sb.append("</request>");
		return sb.toString();
	}
	
	/**
	 * Создать строку - тело запроса проверки наличия обновлений
	 * @param token
	 * @return
	 */
	public static String createCheckUpdateString (String token) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request version=").append(REQUEST_VERSION).append(" type=").append(REQUEST_TYPE_CHECK_UPDATES).append(">");
		sb.append("<token>").append(token).append("</token>");
		sb.append("</request>");
		return sb.toString();
	}
	
	/**
	 * Создать строку - тело запроса подтверждения получения изменений.
	 * @param token
	 * @param idMap - карта, формата <ID, CHECK_STATUS(CHECK_AGREE, CHECK_DISAGREE)>
	 * @return
	 */
	public static String createConfirmCheckString (String token, Map <String, String> idMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request version=").append(REQUEST_VERSION).append(" type=").append(REQUEST_TYPE_CONFIRM_CHECK).append(">");
		sb.append("<token>").append(token).append("</token>");
		for (String mID : idMap.keySet()) {
			sb.append("<id checked='").append(idMap.get(mID)).append("'>");
			sb.append(mID).append("</id>");	
		}
		sb.append("</request>");
		return sb.toString();
	}
	
	

}
