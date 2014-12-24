package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Deviation;
import org.json.JSONException;
import org.json.JSONObject;

public class RespDeviation extends Response {
	private Deviation mResult;
	
	public RespDeviation(JSONObject json) throws JSONException {
		super();
		mResult = new Deviation(json);
	}
	
	public final Deviation getDeviation() {
		return mResult;
	}
}