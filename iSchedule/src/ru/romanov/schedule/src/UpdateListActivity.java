package ru.romanov.schedule.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import ru.romanov.schedule.R;
import ru.romanov.schedule.adapters.ScheduleCheckListAdapter;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.RequestStringsCreater;
import ru.romanov.schedule.utils.StringConstants;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class UpdateListActivity extends ListActivity implements OnClickListener {

	private List<MySubject> newSubjects;
	private ScheduleCheckListAdapter adapter;
	private String token;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_list_activity_layout);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences sp = getSharedPreferences(StringConstants.SCHEDULE_SHARED_PREFERENCES, MODE_PRIVATE);
		this.token = sp.getString(StringConstants.TOKEN, null);
		if(this.token==null) {
			//parampampam ERROR
			Toast.makeText(this, "Что-то странное происходит! Где токен-то? Сейчас каааак всё сломается..", Toast.LENGTH_LONG).show();
		}
		sp = getSharedPreferences(
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
		this.adapter =new ScheduleCheckListAdapter(this.newSubjects, this);
		setListAdapter(this.adapter);
		
		Button confirmButton = (Button) findViewById(R.id.check_confirm_button);
		confirmButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.check_confirm_button:
			StringBuilder sb = new StringBuilder();
			HashMap<String, String> idMap= this.adapter.getCheckedElemetsStatus();
			String reqBody = RequestStringsCreater.createConfirmCheckString(token, idMap);
			new AlertDialog.Builder(this).setMessage(reqBody).show();
			break;
		}
	}
}
