package com.example.demo.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class JasonActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fifth);
		String s="{\"acountid\": \"20020001\",\"serviceid\": \"20020000002\"," +
					"\"serviceesn\": \"0001\",\"userid\": \"admin\",\"args\": [{\"a\":\"你\",\"b\":\"我\"}] }";
		try {
			JSONObject obj = new JSONObject(s);
			String acountid = obj.getString("acountid");
			String object = (String) obj.get("acountid");
			String serviceid = (String) obj.get("serviceid");
			String serviceesn = (String) obj.get("serviceesn");
			String userid = (String) obj.get("userid");
			JSONArray  args = (JSONArray) obj.get("args");
			JSONObject object2 = (JSONObject) args.get(0);
			
			Log.e("tag", "acountid:"+acountid);
			Log.e("tag", "object:"+object);
			Log.e("tag", "serviceid:"+serviceid);
			Log.e("tag", "serviceesn:"+serviceesn);
			Log.e("tag", "userid:"+userid);
			Log.e("tag", "args:"+args);
			Log.e("tag", "object2:"+object2);
			
			Log.d("tag", "obj:"+obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
}
