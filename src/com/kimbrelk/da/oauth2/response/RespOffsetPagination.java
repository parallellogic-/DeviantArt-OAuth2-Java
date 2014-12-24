package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespOffsetPagination<T> extends RespPagination<T> {
	private int mNextOffset;
	
	public RespOffsetPagination(JSONObject json) throws JSONException {
		super(json, "results");
		mNextOffset = json.optInt("next_offset");
	}
	
	public final int nextOffset() {
		return mNextOffset;
	}
}