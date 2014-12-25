package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.CuratedModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespCurated extends RespPaginationOffset<CuratedModule> {
	public RespCurated(JSONObject json) throws JSONException {
		super(json, "results");
	}
	
	@Override
	protected final void getResultsFromJsonArray(JSONArray json) throws JSONException {
		mResults = new CuratedModule[json.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new CuratedModule(json.getJSONObject(a));
		}
	}
}