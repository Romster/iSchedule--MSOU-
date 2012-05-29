package ru.romanov.schedule.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import ru.romanov.schedule.R;
import ru.romanov.schedule.adapters.ScheduleCheckListAdapter;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.StringConstants;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class UpdateListActivity extends ListActivity {

	private List<MySubject> newSubjects;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_list_activity_layout);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences sp = getSharedPreferences(
				StringConstants.MY_SCHEDULE, MODE_PRIVATE);
		Map<String, String> map = (Map<String, String>) sp.getAll();
		ArrayList<MySubject> scedule = new ArrayList<MySubject>(map.size());
		try {
			for (String key : map.keySet()) {
				MySubject sbj = new MySubject(key, new JSONObject(map.get(key)));
				scedule.add(sbj);
			}
			ArrayList<MySubject> sbjToCheck = new ArrayList<MySubject>();
			for (MySubject sbj : scedule) {
				if (!sbj.isChecked()) {
					sbjToCheck.add(sbj);
				}
			}
			this.newSubjects = sbjToCheck;
		} catch (Exception e) {
			// TODO: handle exception
		}
		setListAdapter(new ScheduleCheckListAdapter(this.newSubjects, this));

	}
}
