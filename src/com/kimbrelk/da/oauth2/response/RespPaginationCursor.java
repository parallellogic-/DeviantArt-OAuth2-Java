package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespPaginationCursor<T> extends RespPagination<T> {
	private String mCursor;
	
	public RespPaginationCursor(JSONObject json) throws JSONException {
		super(json, "items");
		mCursor = json.optString("cursor");
	}
	
	public final String getCursor() {
		return mCursor;
	}
}