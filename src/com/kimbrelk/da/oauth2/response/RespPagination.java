package com.kimbrelk.da.oauth2.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class RespPagination<T> extends Response {
	protected T[] mResults;
	private boolean mHasMore;
	
	public RespPagination(JSONObject json, String arrayName) throws JSONException {
		super();
		mHasMore = json.optBoolean("has_more");
		getResultsFromJsonArray(json.getJSONArray(arrayName));
	}
	
	protected abstract void getResultsFromJsonArray(JSONArray json) throws JSONException;
	
	public final T[] getResults() {
		return mResults;
	}
	public final boolean hasMore() {
		return mHasMore;
	}
}