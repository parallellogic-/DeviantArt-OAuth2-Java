package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Status;
import org.json.JSONException;
import org.json.JSONObject;

public class RespUserStatus extends Response {
	private Status mResult;
	
	public RespUserStatus(JSONObject json) throws JSONException {
		super();
		mResult = new Status(json);
	}
	
	public final Status getStatus() {
		return mResult;
	}
}