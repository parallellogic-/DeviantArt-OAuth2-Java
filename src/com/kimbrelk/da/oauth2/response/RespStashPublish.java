package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashPublish extends Response {
	private String mDeviationId;
	private String mURL;
	
	public RespStashPublish(JSONObject json) throws JSONException {
		mDeviationId = json.getString("deviationid");
		mURL = json.getString("url");
	}

	public final String getDeviationId() {
		return mDeviationId;
	}
	public final String getURL() {
		return mURL;
	}
}