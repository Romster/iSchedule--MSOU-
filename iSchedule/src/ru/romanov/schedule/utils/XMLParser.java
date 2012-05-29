package ru.romanov.schedule.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class XMLParser {
	private static long MILISEC_IN_DAY = 1000 * 60 * 60 * 24;
	private static long MILISEC_IN_WEEK = MILISEC_IN_DAY * 7;

	public static String OK = "OK";
	public static String NO_AUTH = "NO_AUTH";
	public static String STATUS = "status";
	public static String TOKEN = "token";
	public static String ITEM = "item";
	public static String ID = "id";
	public static String UPDATE_DT = "update-dt";
	public static String MODE = "mode";
	public static String CHECKED = "checked";
	public static String MODE_ADD = "add";
	public static String MODE_DEL = "del";
	public static String PLACE = "place";
	public static String SUBJECT = "subject";
	public static String ACTS = "activities";
	public static String GROUPS = "groups";
	public static String TIME = "time";
	public static String PERIOD = "period";
	public static String START = "start";
	public static String END = "end";
	public static String REPEAT = "repeat";
	public static String DAY_OF_WEEK = "dow";
	public static String HOURS = "hours";
	public static String EXCLUDES = "excludes";
	public static String INCLUDES = "includes";
	public static String NAME = "name";
	public static String EMAIL ="email";
	public static String PHONE = "phone";
	public static String LAST_UPDATE_DT = "last_update_dt";
	public static String REPEAT_MODE_EACH = "each";
	public static String REPEAT_MODE_NONE = "none";
	public static String SHORT_STRING_DATE_FORMAT = "yyyy-MM-dd";
	public static String TIME_DATE_FORMAT = "kkmm";
	public static String MY_TIME_DATE_FORMAT = "kk:mm";
	public static String MY_LONG_DATE_FORMAT = "dd.MM.dd  kk:mm";

	/**
	 * разбор ответа на запрос авторизации
	 * 
	 * @param XMLResponse
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Map<String, String> parseAuthResponse(String XMLResponse)
			throws ParserConfigurationException, SAXException, IOException {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(is);
			NodeList domNodes = dom.getChildNodes();
			Node response = domNodes.item(0);
			NodeList responceNodes = response.getChildNodes();
			String status = responceNodes.item(0).getFirstChild()
					.getNodeValue();
			resultMap.put(STATUS, status);
			if(status.equals(OK)){
				String token = responceNodes.item(1).getFirstChild()
						.getNodeValue();
				resultMap.put(TOKEN, token);
				
				//Получим информацию о пользователе. Остальноe, пока, - не важно.
				NodeList userInfoList = responceNodes.item(2).getChildNodes().item(1).getChildNodes();
				resultMap.put(NAME, userInfoList.item(1).getFirstChild().getNodeValue());
				resultMap.put(EMAIL, userInfoList.item(3).getFirstChild().getNodeValue());
				resultMap.put(PHONE, userInfoList.item(4).getFirstChild().getNodeValue());
			}

		} catch (IOException e) {
			throw (e);
		} finally {
			if (is != null)
				is.close();
		}

		return resultMap;
	}

	/**
	 * разбор ответа на запрос информации о последнем обновлении
	 * 
	 * @param XMLResponse
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static HashMap<String, String> parseLastUpdateInfoResponse(
			String XMLResponse) throws IOException,
			ParserConfigurationException, SAXException {
		HashMap<String, String> map = new HashMap<String, String>();
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(is);
			NodeList domNodes = dom.getChildNodes();
			Node response = domNodes.item(0);
			NodeList responseNodes = response.getChildNodes();
			String status = responseNodes.item(0).getFirstChild()
					.getNodeValue();
			if (status.equals(OK)) {
				NodeList info = responseNodes.item(1).getChildNodes();
				map.put(info.item(0).getNodeName(), info.item(0)
						.getFirstChild().getNodeValue());
				map.put(info.item(1).getNodeName(), info.item(1)
						.getFirstChild().getNodeValue());
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw e;
		} catch (SAXException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null)
				is.close();
		}

		return map;
	}

	/**
	 * разбор ответа на запрос обновления
	 * 
	 * @param XMLResponse
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParseException
	 */
	public static MySubjectUpdateManager parseXMLScheduleResponse(
			String XMLResponse) throws IOException,
			ParserConfigurationException, SAXException, ParseException {
		MySubjectUpdateManager manager = null;
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(is);
			NodeList domNodes = dom.getChildNodes();
			Node response = domNodes.item(0);
			NodeList responseNodes = response.getChildNodes();
			if (responseNodes.item(0).getChildNodes().item(0).getNodeValue()
					.equals(OK)) {
				if (responseNodes.getLength() > 1) {
					manager = parseSchedule(responseNodes.item(1)
							.getChildNodes().item(0).getChildNodes());
					manager.setStatus(MySubjectUpdateManager.OK);
				}
			} else {
				// Ошибка! Статус != ОК
				manager = new MySubjectUpdateManager();
				manager.setStatus(MySubjectUpdateManager.FAIL);
			}
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (ParserConfigurationException e) {
			throw e;
		} catch (SAXException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		} finally {
			if (is != null)
				is.close();
		}
		return manager;
	}

	/**
	 * Служебный метод. Разбирает NodeList items из schedule
	 * 
	 * @param shedule
	 * @throws ParseException
	 */
	private static MySubjectUpdateManager parseSchedule(NodeList schedule)
			throws ParseException {
		MySubjectUpdateManager manager = new MySubjectUpdateManager();
		for (int i = 0; i < schedule.getLength(); i++) {
			NodeList sItem = schedule.item(i).getChildNodes();
			String mode = sItem.item(0).getFirstChild().getNodeValue();
			if (mode.equals(MODE_ADD)) {
				MySubject sbj = new MySubject();
				String _id = sItem.item(1).getFirstChild().getNodeValue();
				sbj.setId(_id);
				String updateDate = sItem.item(2).getFirstChild()
						.getNodeValue();
				sbj.setUpdateDate(updateDate);
				String checkedStatus = sItem.item(3).getFirstChild()
						.getNodeValue();
				sbj.setChecked(checkedStatus.equals("true") ? true : false);
				String place = sItem.item(4).getFirstChild().getNodeValue();
				sbj.setPlace(place);
				String subject = sItem.item(5).getFirstChild().getNodeValue();
				sbj.setSubject(subject);
				NodeList period = sItem.item(6).getFirstChild().getChildNodes();
				setTimeToMySubjectObject(period, sbj);
				String groups = sItem.item(7).getFirstChild().getNodeValue();
				sbj.setGroups(groups);
				String acts = sItem.item(8).getFirstChild().getNodeValue();
				sbj.setActs(acts);
				manager.addSubjectToAdd(sbj);
			} else {
				// TODO: Удаление элемента
			}

		}
		return manager;

	}

	/**
	 * разобрать и установить дату в указанный MySubject
	 * 
	 * @param period
	 * @param sbj
	 * @throws ParseException
	 */
	private static void setTimeToMySubjectObject(NodeList period, MySubject sbj)
			throws ParseException {
		int curr_position = 0;
		String repeat = period.item(curr_position++).getFirstChild()
				.getNodeValue();
		sbj.setRepeat(repeat);
		ArrayList<Date> includes = new ArrayList<Date>();
		ArrayList<Date> excludes = new ArrayList<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_STRING_DATE_FORMAT);
		if (!repeat.equals(REPEAT_MODE_NONE)) {
			// repeat
			String startDateString = period.item(curr_position++)
					.getFirstChild().getNodeValue();
			sbj.setStartDate(startDateString);
			String endDateString = period.item(curr_position++).getFirstChild()
					.getNodeValue();
			sbj.setEndDate(endDateString);
			String day_of_week = period.item(curr_position++).getFirstChild()
					.getNodeValue();
			sbj.setDayOfWeek(day_of_week);
			Date startDate = sdf.parse(startDateString);
			Date endDate = sdf.parse(endDateString);
			includes.addAll(datesInPeriod(startDate, endDate,
					getDayOfWeekInteger(day_of_week), repeat));
		}
		// Time
		NodeList time = period.item(curr_position++).getChildNodes();
		String startTime = time.item(0).getFirstChild().getNodeValue();
		String endTime = time.item(1).getFirstChild().getNodeValue();
		SimpleDateFormat timeSDF = new SimpleDateFormat(TIME_DATE_FORMAT);
		SimpleDateFormat myTimeSDF = new SimpleDateFormat(MY_TIME_DATE_FORMAT);
		sbj.setStartTime(myTimeSDF.format(timeSDF.parse(startTime)));
		sbj.setEndTime(myTimeSDF.format(timeSDF.parse(endTime)));
		// includes
		NodeList includesNodeList = period.item(curr_position++)
				.getChildNodes();
		for (int i = 0; i < includesNodeList.getLength(); i++) {
			includes.add(sdf.parse(includesNodeList.item(i).getFirstChild()
					.getNodeValue()));
		}
		// excludes
		NodeList excludesNodeList = period.item(curr_position++)
				.getChildNodes();
		for (int i = 0; i < excludesNodeList.getLength(); i++) {
			excludes.add(sdf.parse(excludesNodeList.item(i).getFirstChild()
					.getNodeValue()));
		}

		for (Date date : includes) {
			sbj.addDate(date, INCLUDES);
		}

		for (Date date : excludes) {
			sbj.addDate(date, EXCLUDES);
		}

	}

	/**
	 * Получить массив дат из периода с указанным днём недели
	 * 
	 * @param startDate
	 * @param endDate
	 * @param dayOfWeek
	 * @return
	 */
	private static ArrayList<Date> datesInPeriod(Date startDate, Date endDate,
			int dayOfWeek, String repeat) {
		ArrayList<Date> dates = new ArrayList<Date>();
		GregorianCalendar myCaned = new GregorianCalendar();
		myCaned.setTime(startDate);
		if (repeat.equals(REPEAT_MODE_EACH)) {
			while (myCaned.get(Calendar.DAY_OF_WEEK) != dayOfWeek)
				myCaned.setTime(new Date(myCaned.getTime().getTime()
						+ MILISEC_IN_DAY));
			while (myCaned.getTime().compareTo(endDate) <= 0) {
				dates.add(myCaned.getTime());
				myCaned.setTime(new Date(myCaned.getTime().getTime()
						+ MILISEC_IN_WEEK));
			}
		}
		return dates;
	}

	/**
	 * перевод из строкового значения дня недели в интовый
	 * 
	 * @param dow
	 * @return
	 */
	public static int getDayOfWeekInteger(String dow) {
		if (dow.equals("Sun"))
			return Calendar.SUNDAY;
		if (dow.equals("Mon"))
			return Calendar.MONDAY;
		if (dow.equals("Tue"))
			return Calendar.TUESDAY;
		if (dow.equals("Wed"))
			return Calendar.WEDNESDAY;
		if (dow.equals("Thu"))
			return Calendar.THURSDAY;
		if (dow.equals("Fri"))
			return Calendar.FRIDAY;
		if (dow.equals("Sat"))
			return Calendar.SATURDAY;

		return -1;
	}

}
