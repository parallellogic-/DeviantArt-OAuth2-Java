package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashDelete extends Response {
	private long mStashId;
	
	public RespStashDelete(JSONObject json) throws JSONException {
		mStashId = json.getLong("stashid");
	}
	
	public final long getStashId() {
		return mStashId;
	}
}