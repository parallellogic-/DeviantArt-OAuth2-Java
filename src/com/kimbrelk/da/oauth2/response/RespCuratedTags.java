package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Tag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespCuratedTags extends Response {
	private Tag[] mResults;
	
	public RespCuratedTags(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new Tag[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Tag(jsonResults.getJSONObject(a));
		}
	}
	
	public final Tag[] getResults() {
		return mResults;
	}
}