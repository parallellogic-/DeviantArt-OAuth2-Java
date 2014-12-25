package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespPaginationDelta<T> extends RespPagination<T> {
	private String mCursor;
	private int mNextOffset;
	private boolean mReset;
	
	public RespPaginationDelta(JSONObject json) throws JSONException {
		super(json, "entries");
		mCursor = json.getString("cursor");
		mNextOffset = json.optInt("next_offset");
		mReset = json.getBoolean("reset");
	}
	
	public final String getCursor() {
		return mCursor;
	}
	public final int getNextOffset() {
		return mNextOffset;
	}
	public final boolean isReset() {
		return mReset;
	}
}