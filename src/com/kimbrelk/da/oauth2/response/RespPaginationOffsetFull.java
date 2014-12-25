package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespPaginationOffsetFull<T> extends RespPaginationOffset<T> {
	private boolean mHasLess;
	private int mPrevOffset;
	
	public RespPaginationOffsetFull(JSONObject json, String resultsName) throws JSONException {
		super(json, resultsName);
		mHasLess = json.getBoolean("has_less");
		mPrevOffset = json.optInt("prev_offset");
	}
	
	public final boolean hasLess() {
		return mHasLess;
	}
	public final int getPreviousOffset() {
		return mPrevOffset;
	}
}