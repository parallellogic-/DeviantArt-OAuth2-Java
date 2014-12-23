package com.kimbrelk.da.oauth2.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespBrowseTagsSearch extends Response {
	private String[] mResults;
	
	public RespBrowseTagsSearch(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new String[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = jsonResults.getJSONObject(a).getString("tag_name");
		}
	}
	
	public final String[] getTags() {
		return mResults;
	}
}