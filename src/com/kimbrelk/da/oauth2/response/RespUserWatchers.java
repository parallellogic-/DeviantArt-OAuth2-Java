package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Watcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespUserWatchers extends RespPagination<Watcher> {
	public RespUserWatchers(JSONObject json) throws JSONException {
		super(json);
	}

	@Override
	protected final void getResultsFromJsonArray(JSONArray json) throws JSONException {
		mResults = new Watcher[json.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Watcher(json.getJSONObject(a));
		}
	}
}