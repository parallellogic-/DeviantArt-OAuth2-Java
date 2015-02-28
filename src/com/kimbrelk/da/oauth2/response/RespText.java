package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespText extends Response {
	private final String mText;
	
	public RespText(JSONObject json) throws JSONException {
		super();
		mText = json.getString("text");
	}
	
	public final String getText() {
		return mText;
	}
}