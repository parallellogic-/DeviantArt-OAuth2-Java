package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Deviation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespDeviationEmbeddedContent extends RespPaginationOffsetFull<Deviation> {
	public RespDeviationEmbeddedContent(JSONObject json) throws JSONException {
		super(json, "results");
	}
	
	@Override
	protected final void getResultsFromJsonArray(JSONArray json) throws JSONException {
		mResults = new Deviation[json.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Deviation(json.getJSONObject(a));
		}
	}
}