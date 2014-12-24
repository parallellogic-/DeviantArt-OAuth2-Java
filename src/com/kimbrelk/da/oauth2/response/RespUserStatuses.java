package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespUserStatuses extends Response {
	private Status[] mResults;
	
	public RespUserStatuses(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new Status[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Status(jsonResults.getJSONObject(a));
		}
	}
	
	public final Status[] getStatuses() {
		return mResults;
	}
}