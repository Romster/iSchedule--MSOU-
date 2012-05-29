package ru.romanov.schedule.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;

import ru.romanov.schedule.R;
import ru.romanov.schedule.utils.MySubject;
import ru.romanov.schedule.utils.MySubjectUpdateManager;
import ru.romanov.schedule.utils.RequestStringsCreater;
import ru.romanov.schedule.utils.StringConstants;
import ru.romanov.schedule.utils.SubjectToRemove;
import ru.romanov.schedule.utils.XMLParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateDialogActivity extends Activity implements OnClickListener {

	private TextView updatesTV;
	private TextView lastUpdateTV;
	private Button checkBut;
	private Button downloadBut;
	private Button exitBut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_dialog_background);
	}

	@Override
	protected void onStart() {
		super.onStart();
		updatesTV = (TextView) findViewById(R.id.update_found);
		lastUpdateTV = (TextView) findViewById(R.id.update_last_update);
		checkBut = (Button) findViewById(R.id.update_check);
		checkBut.setOnClickListener(this);
		downloadBut = (Button) findViewById(R.id.update_load);
		downloadBut.setOnClickListener(this);
		exitBut = (Button) findViewById(R.id.update_exit);
		exitBut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_check:
			UpdateChecker checker = new UpdateChecker(getSharedPreferences(
					StringConstants.SCHEDULE_SHARED_PREFERENCES, MODE_PRIVATE)
					.getString(StringConstants.TOKEN, null));
			checker.execute(null);
			break;
		case R.id.update_load:
			AsyncUpdater updater = new AsyncUpdater(getSharedPreferences(
					StringConstants.SCHEDULE_SHARED_PREFERENCES, MODE_PRIVATE)
					.getString(StringConstants.TOKEN, null));
			updater.execute(null);
			break;
		case R.id.update_exit:
			finish();
			break;
		}

	}

	private class UpdateChecker extends
			AsyncTask<Void, Void, Map<String, String>> {

		private String token;
		private ProgressDialog dialog;

		public UpdateChecker(String token) {
			this.token = token;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(UpdateDialogActivity.this, "",
					getString(R.string.loading), true);
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			String reqString = RequestStringsCreater
					.createCheckUpdateString(token);
			HttpResponse responce = null;
			try {
				HttpPost request = new HttpPost(StringConstants.MY_URI);
				StringEntity entity = new StringEntity(reqString, "UTF-8");
				request.setHeader(HTTP.CONTENT_TYPE, "text/xml");
				request.setEntity(entity);
				responce = client.execute(request);
				HttpEntity ent = responce.getEntity();

				BufferedReader r = new BufferedReader(new InputStreamReader(
						ent.getContent()));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				Map<String, String> map = XMLParser
						.parseLastUpdateInfoResponse(total.toString());
				return map;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			super.onPostExecute(result);
			if (result != null) {
				String status = result.get(XMLParser.STATUS);
				if (status.equals("true")) {
					updatesTV.setTextColor(Color.RED);
					lastUpdateTV.setTextColor(Color.RED);
					updatesTV.setText(getString(R.string.present));
					checkBut.setEnabled(false);
					downloadBut.setEnabled(true);
				} else {
					updatesTV.setText(getString(R.string.apsent));
				}
				lastUpdateTV.setText(result.get(XMLParser.LAST_UPDATE_DT));

			} else {
				Toast.makeText(
						UpdateDialogActivity.this,
						"Ошибка! Проверьте соединение с сетью. Возможно, токен устарел",
						Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
		}
	}

	private class AsyncUpdater extends
			AsyncTask<Void, Void, MySubjectUpdateManager> {

		private ProgressDialog dialog;
		private String token;

		public AsyncUpdater(String token) {
			this.token = token;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(UpdateDialogActivity.this, "",
					getString(R.string.loading), true);
		}

		@Override
		protected MySubjectUpdateManager doInBackground(Void... params) {
			HttpClient client = new DefaultHttpClient();
			String reqString = RequestStringsCreater.createUpdateString(token);
			HttpResponse response = null;
			try {
				HttpPost request = new HttpPost(StringConstants.MY_URI);
				StringEntity entity = new StringEntity(reqString, "UTF-8");
				request.setHeader(HTTP.CONTENT_TYPE, "text/xml");
				request.setEntity(entity);
				response = client.execute(request);
				HttpEntity ent = response.getEntity();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						ent.getContent()));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				MySubjectUpdateManager msManager = XMLParser
						.parseXMLScheduleResponse(total.toString());
				return msManager;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();

			}
			return null;

		}

		@Override
		protected void onPostExecute(MySubjectUpdateManager updateManager) {
			super.onPostExecute(updateManager);
			if (updateManager != null) {
				try {
					SharedPreferences schedule = getSharedPreferences(
							StringConstants.MY_SCHEDULE, MODE_PRIVATE);
					ArrayList<MySubject> sbjToAdd = updateManager
							.getSubjectsToAdd();
					ArrayList<SubjectToRemove> sbjToRemove = updateManager
							.getSubjectsToDelete();
					SharedPreferences.Editor editor = schedule.edit();
					if (sbjToAdd.size() > 0) {
						for (MySubject sbj : sbjToAdd) {
							editor.putString(sbj.getId(), sbj.toJSONObject()
									.toString());
						}
					}
					if (sbjToRemove.size() > 0) {
						for (SubjectToRemove sbj : sbjToRemove) {
							// TODO: remove sbj
						}
					}
					editor.commit();
					editor = getSharedPreferences(StringConstants.SCHEDULE_SHARED_PREFERENCES, MODE_PRIVATE).edit();
					Calendar calend = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat(XMLParser.MY_LONG_DATE_FORMAT);
					String time = sdf.format(calend.getTime());
					editor.putString(StringConstants.SHARED_LAST_SYNC_DATE, time);
					editor.commit();
					updatesTV.setTextColor(Color.WHITE);
					lastUpdateTV.setTextColor(Color.WHITE);
					updatesTV.setText(getString(R.string.apsent));
					downloadBut.setEnabled(false);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {

			}
			dialog.dismiss();

		}
	}

}
