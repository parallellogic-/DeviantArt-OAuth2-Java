package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Whofaved;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespDeviationWhofaved extends RespPaginationOffset<Whofaved> {
	
	public RespDeviationWhofaved(JSONObject json) throws JSONException {
		super(json, "results");
	}
	
	@Override
	protected final void getResultsFromJsonArray(JSONArray json) throws JSONException {
		mResults = new Whofaved[json.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Whofaved(json.getJSONObject(a));
		}
	}
}