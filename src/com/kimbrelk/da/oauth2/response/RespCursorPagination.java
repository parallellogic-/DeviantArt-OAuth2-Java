package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespCursorPagination<T> extends RespPagination<T> {
	private String mCursor;
	
	public RespCursorPagination(JSONObject json) throws JSONException {
		super(json, "items");
		mCursor = json.optString("cursor");
	}
	
	public final String getCursor() {
		return mCursor;
	}
}