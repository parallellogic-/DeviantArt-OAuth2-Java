package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

import com.kimbrelk.da.oauth2.struct.StashMetadata;

public final class RespStashMetadata extends Response {
	private StashMetadata mMetadata;
	
	public RespStashMetadata(JSONObject json) throws JSONException {
		mMetadata = new StashMetadata(json);
	}

	public final StashMetadata getMetadata() {
		return mMetadata;
	}
}