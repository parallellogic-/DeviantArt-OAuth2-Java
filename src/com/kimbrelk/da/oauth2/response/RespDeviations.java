package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Deviation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespDeviations extends Response {
	private Deviation[] mResults;
	
	public RespDeviations(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new Deviation[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Deviation(jsonResults.getJSONObject(a));
		}
	}
	
	public final Deviation[] getDeviations() {
		return mResults;
	}
}