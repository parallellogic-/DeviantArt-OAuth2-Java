package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespPaginationOffset<T> extends RespPagination<T> {
	private int mNextOffset;
	
	public RespPaginationOffset(JSONObject json, String resultsName) throws JSONException {
		super(json, resultsName);
		mNextOffset = json.optInt("next_offset");
		if (!hasMore()) {
			mNextOffset = -1;
		}
	}
	
	public final int getNextOffset() {
		return mNextOffset;
	}
}