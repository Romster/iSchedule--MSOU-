package ru.romanov.schedule.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.romanov.schedule.R;
import ru.romanov.schedule.adapters.ScheduleListAdapter;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.MySubjectUpdateManager;
import ru.romanov.schedule.utils.RequestStringsCreater;
import ru.romanov.schedule.utils.StringConstants;
import ru.romanov.schedule.utils.XMLParser;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class ScheduleListActivity extends ListActivity {

	private ArrayList <MySubject> subjList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_layout);
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			loadSchedule();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*TextView tv = (TextView) findViewById(android.R.id.empty);
		File f = new File(Environment.getDataDirectory().getPath().concat("/schdule_response.xml"));
		String xmlSchedule = getTestXMLStringFromLocalFile(f);
		tv.setText(xmlSchedule);
		try{
			ArrayList<MySubject> sbjects = new ArrayList<MySubject>();
			for (String key : myMap.keySet()) {
				sbjects.add(new MySubject(key, new JSONObject(myMap.get(key))));
			}
			MySubjectUpdateManager manager = XMLParser.parseXMLScheduleResponse(xmlSchedule);
			HashMap<String, String> map = new HashMap<String, String>();
			ArrayList<MySubject> arr = new ArrayList<MySubject>();
			for (MySubject sbj : manager.getSubjectsToAdd()) {
				map.put(sbj.getId(), sbj.toJSONObject().toString());
				arr.add(new MySubject(sbj.getId(),new JSONObject(map.get(sbj.getId()))));
			}
			SharedPreferences.Editor editor = sherPref.edit();
			for (String key : map.keySet()) {
				editor.putString(key, map.get(key));
			}
			editor.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		ScheduleListAdapter adapter = new ScheduleListAdapter(subjList, this);
		setListAdapter(adapter);
	}

	/**
	 * Временная функция для тестирования. Информация берётся не с сервера, а из указанного фала
	 * @param f
	 * @return
	 */
	private static String getTestXMLStringFromLocalFile(File f) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			StringBuilder total = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				total.append(line);
			} 
			return total.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
	
	private void loadSchedule() throws JSONException, ParseException {
		SharedPreferences sherPref = getSharedPreferences(StringConstants.MY_SCHEDULE, MODE_PRIVATE);
		Map<String,String> myMap = (Map<String, String>) sherPref.getAll();
		this.subjList = new ArrayList<MySubject>(myMap.size());
		for (String key : myMap.keySet()) {
			this.subjList.add(new MySubject(key, new JSONObject(myMap.get(key))));
		}
		
	}


}
