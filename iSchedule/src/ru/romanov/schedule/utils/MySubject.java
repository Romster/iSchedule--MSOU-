package ru.romanov.schedule.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.xml;

public class MySubject {
//public static String DATE_FORMAT = "yyyy-MM-dd";

	private String id;
	private String place;
	private String subject;
	private String updateDate;
	private String mode;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String dayOfWeek;
	private String repeat;
	private Map<Date, String> dates;
	private String groups;
	private String acts;
	private boolean checked;

	public MySubject() {
		this.dates = new HashMap<Date, String>();
	}

	public MySubject(String _id, JSONObject description) throws JSONException, ParseException {
		this.dates = new HashMap<Date, String>();
		this.id = _id;
		if (description.has(XMLParser.PLACE))
			this.place = description.getString(XMLParser.PLACE);
		if (description.has(XMLParser.SUBJECT))
			this.subject = description.getString(XMLParser.SUBJECT);
		if (description.has(XMLParser.ACTS)) {
			this.acts = description.getString(XMLParser.ACTS);
		}
		if (description.has(XMLParser.GROUPS)) {
			this.groups = description.getString(XMLParser.GROUPS);
		}
		if (description.has(XMLParser.CHECKED)) {
			this.checked = description.getBoolean(XMLParser.CHECKED);
		}
		if (description.has(XMLParser.UPDATE_DT))
			this.updateDate = description.getString(XMLParser.UPDATE_DT);
		if (description.has(XMLParser.REPEAT)) {
			this.repeat = description.getString(XMLParser.REPEAT);
			if (!this.repeat.equals(XMLParser.REPEAT_MODE_NONE)) {
				if (description.has(XMLParser.PERIOD)) {
					JSONObject period = description
							.getJSONObject(XMLParser.PERIOD);
					if (period.has(XMLParser.START))
						this.startDate = period.getString(XMLParser.START);
					if (period.has(XMLParser.END))
						this.endDate = period.getString(XMLParser.END);
					if (period.has(XMLParser.DAY_OF_WEEK))
						this.dayOfWeek = period
								.getString(XMLParser.DAY_OF_WEEK);
				}
			}
			if (description.has(XMLParser.TIME)) {
				JSONObject time = description.getJSONObject(XMLParser.TIME);
				if (time.has(XMLParser.START))
					this.startTime = time.getString(XMLParser.START);
				if (time.has(XMLParser.END))
					this.endTime = time.getString(XMLParser.END);
			}
			SimpleDateFormat sdf = new SimpleDateFormat(XMLParser.SHORT_STRING_DATE_FORMAT);
			if (description.has(XMLParser.EXCLUDES)) {
				JSONArray excludes = description.getJSONArray(XMLParser.EXCLUDES);
				for (int i=0; i<excludes.length(); i++) {
					this.dates.put(sdf.parse(excludes.getString(i)), XMLParser.EXCLUDES);
				}
				
			}
			if (description.has(XMLParser.INCLUDES)) {
				JSONArray incudes = description.getJSONArray(XMLParser.INCLUDES);
				for (int i=0; i<incudes.length(); i++) {
					this.dates.put(sdf.parse(incudes.getString(i)), XMLParser.INCLUDES);
				}
				
			}
		}

	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject object = new JSONObject();
		try {
			object.put(XMLParser.SUBJECT, this.subject);
			object.put(XMLParser.ACTS, this.acts);
			object.put(XMLParser.GROUPS, this.groups);
			object.put(XMLParser.PLACE, this.place);
			object.put(XMLParser.CHECKED, this.checked);
			JSONObject timeJSON = new JSONObject();
			timeJSON.put(XMLParser.START, this.startTime);
			timeJSON.put(XMLParser.END, this.endTime);
			object.put(XMLParser.TIME, timeJSON);
			SimpleDateFormat sdf = new SimpleDateFormat(XMLParser.SHORT_STRING_DATE_FORMAT);
			JSONArray includes = new JSONArray(getIncludesAsStringsIndateFormat(sdf));
			object.put(XMLParser.INCLUDES, includes);
			JSONArray excludes = new JSONArray(getExcludesAsStringsIndateFormat(sdf));
			object.put(XMLParser.EXCLUDES, excludes);
			object.put(XMLParser.UPDATE_DT, this.updateDate);
			object.put(XMLParser.REPEAT, this.repeat);
			if (!this.repeat.equals(XMLParser.REPEAT_MODE_NONE)) {
				JSONObject period = new JSONObject();
				period.put(XMLParser.START, this.startDate);
				period.put(XMLParser.END, this.endDate);
				period.put(XMLParser.DAY_OF_WEEK, this.dayOfWeek);
				object.put(XMLParser.PERIOD, period);
			}
		} catch (JSONException e) {
			throw (e);
		}

		return object;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDte() {
		return startDate;
	}

	public void setStartDate(String startDte) {
		this.startDate = startDte;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getActs() {
		return acts;
	}

	public void setActs(String acts) {
		this.acts = acts;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	// Функции для работы с датами

	/**
	 * Добавление одиночной даты из хмл в виде строки.
	 * 
	 * @param date
	 *            -дата
	 * @param type
	 *            - includes or excludes
	 * @throws ParseException
	 */
	public void addDate(Date newDate, String type) throws ParseException {
		this.dates.put(newDate, type);
	}

	public List<Date> getIncludes() {
		ArrayList<Date> list = new ArrayList<Date>();
		String type = "";
		for (Date key : this.dates.keySet()) {
			type = dates.get(key);
			if (type.equals(XMLParser.INCLUDES)) {
				list.add(key);
			}
		}
		return list;
	}

	public List<String> getIncludesAsStringsIndateFormat(SimpleDateFormat sdf) {
		ArrayList<String> list = new ArrayList<String>();
		String type = "";
		for (Date key : this.dates.keySet()) {
			type = dates.get(key);
			if (type.equals(XMLParser.INCLUDES)) {
				list.add(sdf.format(key));
			}
		}
		return list;
	}

	public List<Date> getExcludes() {
		ArrayList<Date> list = new ArrayList<Date>();
		String type = "";
		for (Date key : this.dates.keySet()) {
			type = dates.get(key);
			if (type.equals(XMLParser.EXCLUDES)) {
				list.add(key);
			}
		}
		return list;
	}

	public List<String> getExcludesAsStringsIndateFormat(SimpleDateFormat sdf) {
		ArrayList<String> list = new ArrayList<String>();
		String type = "";
		for (Date key : this.dates.keySet()) {
			type = dates.get(key);
			if (type.equals(XMLParser.EXCLUDES)) {
				list.add(sdf.format(key));
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param sdf
	 * @param date дата в формате sdf
	 * @return Есть ли занятия в указанную дату.
	 * @throws ParseException
	 */
	public boolean includesDate(SimpleDateFormat sdf, String date) throws ParseException {
		Date mDate = sdf.parse(date);
		Set <Date> dts = dates.keySet();
		ArrayList<Date> dateList = new ArrayList<Date>(dts.size());
		for(Date d : dts) {
			dateList.add(d);
		}
		if (dates.containsKey(mDate) && dates.get(mDate).equals(XMLParser.INCLUDES))
			return true;
		else return false;
	}
 
}
