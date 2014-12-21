package com.kimbrelk.da.oauth2.response;

import org.json.pc.JSONException;
import org.json.pc.JSONObject;

public class RespUserDamntoken extends Response {
	private String mDamnToken;

	public RespUserDamntoken(String dAmnToken) {
		mDamnToken = dAmnToken;
	}
	public RespUserDamntoken(JSONObject json) throws JSONException {
		mDamnToken = json.getString("damntoken");
	}
	
	public final String getDamnToken() {
		return mDamnToken;
	}
}