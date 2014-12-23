package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespCollections extends RespDeviationsQuery {
	private String mName;
	
	public RespCollections(JSONObject json) throws JSONException {
		super(json);
		mName = json.getString("name");
	}
	
	public final String getName() {
		return mName;
	}
}