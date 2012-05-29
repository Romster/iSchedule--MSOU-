package ru.romanov.schedule.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.romanov.schedule.R;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.XMLParser;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class ScheduleCheckListAdapter extends BaseAdapter {
	private static String AGREE = "agree";
	private static String DISAGREE = "disagree";
	private static String STRING_FORMAT = "dd.MM.yyyy";
	private static Map<MySubject, String> checkMap;
	private List<MySubject> subjectList;
	private Context context;
	private String[] dayOfWeekStrings;
	private SimpleDateFormat sdf;

	public ScheduleCheckListAdapter(List<MySubject> subjects, Context context) {
		this.subjectList = subjects;
		this.context = context;
		checkMap = new HashMap<MySubject, String>(subjects.size());
		for (MySubject sbj : subjectList) {
			checkMap.put(sbj, AGREE);
		}
		this.dayOfWeekStrings = context.getResources().getStringArray(
				R.array.days_of_week);
		this.sdf = new SimpleDateFormat(STRING_FORMAT);
	}

	@Override
	public int getCount() {
		return subjectList.size();
	}

	@Override
	public Object getItem(int pos) {
		subjectList.get(pos);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		final MySubject mySubj = subjectList.get(pos);
		LayoutInflater inflator = LayoutInflater.from(context);
		View view = inflator.inflate(R.layout.check_item, null);
		TextView subjTV = (TextView) view.findViewById(R.id.check_subject_tv);
		subjTV.setText(mySubj.getSubject());
		if (!mySubj.getRepeat().equals(XMLParser.REPEAT_MODE_NONE)) {
			TextView startdateTV = (TextView) view
					.findViewById(R.id.check_start_date_tv);
			startdateTV.setText(mySubj.getStartDte());
			TextView endDateTV = (TextView) view
					.findViewById(R.id.check_end_date_tv);
			endDateTV.setText(mySubj.getEndDate());
			TextView dowTV = (TextView) view.findViewById(R.id.check_dow_tv);
			dowTV.setText(dayOfWeekStrings[XMLParser.getDayOfWeekInteger(mySubj
					.getDayOfWeek()) - 1]);
			List<Date> dates = mySubj.getExcludes();
			if (dates.size() > 0) {
				LinearLayout excludesLL = (LinearLayout) view
						.findViewById(R.id.check_excludes_layout);
				excludesLL.setVisibility(View.VISIBLE);
				TextView excludesTV = (TextView) view
						.findViewById(R.id.check_excludes_tv);
				StringBuffer sb = new StringBuffer();
				for (Date date : dates) {
					sb.append(sdf.format(date));
					sb.append("  ");
				}
				excludesTV.setText(sb.toString());
			}

		} else {
			LinearLayout periodLL = (LinearLayout) view
					.findViewById(R.id.check_period_layout);
			periodLL.setVisibility(View.GONE);
			LinearLayout dateLL = (LinearLayout) view
					.findViewById(R.id.check_date_include_layout);
			dateLL.setVisibility(View.VISIBLE);
			TextView dateTV = (TextView) view
					.findViewById(R.id.check_date_include_tv);
			List<Date> dates = mySubj.getIncludes();
			StringBuffer sb = new StringBuffer();
			for (Date date : dates) {
				sb.append(sdf.format(date));
				sb.append("  ");
			}
			dateTV.setText(sb.toString());
		}

		TextView timeTV = (TextView) view.findViewById(R.id.check_time_tv);
		timeTV.setText(mySubj.getStartTime() + " - " + mySubj.getEndTime());
		TextView groupTV = (TextView) view.findViewById(R.id.check_group_tv);
		groupTV.setText(mySubj.getGroups());
		TextView placeTV = (TextView) view.findViewById(R.id.check_place_tv);
		placeTV.setText(mySubj.getPlace());
		TextView actTV = (TextView) view.findViewById(R.id.check_act_tv);
		actTV.setText(mySubj.getActs());

		RadioGroup radGroup = (RadioGroup) view
				.findViewById(R.id.check_agree_rbg);
		if (checkMap.get(mySubj).equals(AGREE))
			radGroup.check(R.id.radioButton1);
		else
			radGroup.check(R.id.radioButton2);

		radGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.radioButton1:
					checkMap.put(mySubj, AGREE);
					break;

				case R.id.radioButton2:
					checkMap.put(mySubj, DISAGREE);
					break;
				default:
					break;
				}

			}
		});
		return view;
	}
	
	public HashMap<String, String> getCheckedElemetsStatus() {
		HashMap<String, String> idMap = new HashMap<String, String>(this.checkMap.size());
		for (MySubject sbj : this.checkMap.keySet()) {
			idMap.put(sbj.getId(),checkMap.get(sbj));
		}
		return idMap;
	}

}
