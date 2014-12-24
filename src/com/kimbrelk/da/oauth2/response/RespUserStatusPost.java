package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public class RespUserStatusPost extends Response {
	private String mStatusId;
	
	public RespUserStatusPost(JSONObject json) throws JSONException {
		super();
		mStatusId = json.getString("statusid");
	}
	
	public final String getStatusId() {
		return mStatusId;
	}
}