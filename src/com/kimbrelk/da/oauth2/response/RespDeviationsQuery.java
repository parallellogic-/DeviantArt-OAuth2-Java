package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespDeviationsQuery extends RespDeviations {
	public final static int ERROR_INVALID_PRINT = 0;
	
	private boolean mHasMore;
	private int mNextOffset;
	
	public RespDeviationsQuery(JSONObject json) throws JSONException {
		super(json);
		mHasMore = json.getBoolean("has_more");
		mNextOffset = json.optInt("next_offset");
	}
	
	public final boolean hasMore() {
		return mHasMore;
	}
	public final int nextOffset() {
		return mNextOffset;
	}
}