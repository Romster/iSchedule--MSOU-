package ru.romanov.schedule.adapters;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.romanov.schedule.R;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.XMLParser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Адаптер для главного списка расписания.
 * 
 * @author romanov
 * 
 */
public class ScheduleListAdapter extends BaseAdapter {

	private static String DATE_FORMAT_STRINGNG = "dd.MM";
	private static long MILISEC_IN_DAY = 86400000;
	private String[] dayOfWeekStrings;
	private Context context;
	private List<MySubject> subjList;
	private ArrayList<Date> dates;
	private Date currDate;
	private SimpleDateFormat sdf;

	public ScheduleListAdapter(List<MySubject> subjects, Context context) {
		this.subjList = subjects;
		this.context = context;
		this.dates = new ArrayList<Date>(7);
		this.currDate = new Date(System.currentTimeMillis());
		this.dayOfWeekStrings = context.getResources().getStringArray(
				R.array.days_of_week);
		this.sdf = new SimpleDateFormat(XMLParser.SHORT_STRING_DATE_FORMAT);
		dates.add(currDate);
		GregorianCalendar calend = new GregorianCalendar();
		calend.setTime(currDate);
		Date date = new Date(calend.getTimeInMillis());
		// Показать расписание минимум на 21 день, закончить в воскресенье
		while (calend.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
				|| dates.size() < 21) {
			date = new Date(date.getTime() + MILISEC_IN_DAY);
			dates.add(date);
			calend.setTime(date);
		}

	}

	@Override
	public int getCount() {
		return dates.size();
	}

	@Override
	public Object getItem(int num) {
		return dates.get(num);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ScheduleHolder holder;
		LayoutInflater inflator = LayoutInflater.from(context);
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.schedule_list_item, null);

			holder = new ScheduleHolder();
			holder.dateTV = (TextView) convertView
					.findViewById(R.id.schedule_list_item_date);
			holder.dowTV = (TextView) convertView
					.findViewById(R.id.schedule_lsit_item_dow);
			holder.scheduleLL = (LinearLayout) convertView
					.findViewById(R.id.schedule_list_item_layout);
			holder.childView = inflator.inflate(R.layout.schedule_item_element, null);
			convertView.setTag(holder);
		} else {
			holder = (ScheduleHolder) convertView.getTag();
		}
		GregorianCalendar calend = new GregorianCalendar();
		calend.setTime(dates.get(pos));
		holder.dowTV.setText(this.dayOfWeekStrings[calend
				.get(Calendar.DAY_OF_WEEK)-1]);
		SimpleDateFormat sdf = new SimpleDateFormat(XMLParser.SHORT_STRING_DATE_FORMAT);
		holder.dateTV.setText(sdf.format(dates.get(pos)));
		holder.scheduleLL.removeAllViews();
		try {
			for (MySubject subject : subjList) {
				if (subject.includesDate(sdf, sdf.format(dates.get(pos)))){
					View childView =inflator.inflate(R.layout.schedule_item_element, null);
					TextView sbjTV = (TextView) childView.findViewById(R.id.schedule_item_subject);
					TextView timeTextView = (TextView) childView.findViewById(R.id.schedule_item_time);
					TextView placeTV = (TextView) childView.findViewById(R.id.schedule_item_place);
					TextView groupTV = (TextView) childView.findViewById(R.id.schedule_item_group);
					TextView actTV = (TextView) childView.findViewById(R.id.schedule_item_act);
					sbjTV.setText(subject.getSubject());
					placeTV.setText(subject.getPlace());
					timeTextView.setText(subject.getStartTime()+"-"+subject.getEndTime());
					groupTV.setText(subject.getGroups());
					actTV.setText(subject.getActs());
					holder.scheduleLL.addView(childView);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return convertView;

	}

//	private String getDayOfWeekString(int dayOfWeek) {
//		switch (dayOfWeek) {
//		case Calendar.MONDAY:
//			return this.dayOfWeekStrings[0];
//		case Calendar.TUESDAY:
//			return this.dayOfWeekStrings[1];
//		case Calendar.WEDNESDAY:
//			return this.dayOfWeekStrings[2];
//		case Calendar.THURSDAY:
//			return this.dayOfWeekStrings[3];
//		case Calendar.FRIDAY:
//			return this.dayOfWeekStrings[4];
//		case Calendar.SATURDAY:
//			return this.dayOfWeekStrings[5];
//		case Calendar.SUNDAY:
//			return this.dayOfWeekStrings[6];
//
//		default:
//			return null;
//		}
//
//	}

	private class ScheduleHolder {
		TextView dateTV;
		TextView dowTV;
		LinearLayout scheduleLL;
		View childView;
	}

}
