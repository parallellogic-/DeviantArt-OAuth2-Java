package com.kimbrelk.da.oauth2.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespPagination<T> extends Response {
	protected T[] mResults;
	private boolean mHasMore;
	private int mNextOffset;
	
	public RespPagination(JSONObject json) throws JSONException {
		super();
		getResultsFromJsonArray(json.getJSONArray("results"));
		mHasMore = json.getBoolean("has_more");
		mNextOffset = json.optInt("next_offset");
	}
	
	protected abstract void getResultsFromJsonArray(JSONArray json) throws JSONException;
	
	public final T[] getResults() {
		return mResults;
	}
	public final boolean hasMore() {
		return mHasMore;
	}
	public final int nextOffset() {
		return mNextOffset;
	}
}