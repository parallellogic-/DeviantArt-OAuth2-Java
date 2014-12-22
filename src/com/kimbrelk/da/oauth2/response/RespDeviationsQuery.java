package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespDeviationsQuery extends RespDeviations {
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