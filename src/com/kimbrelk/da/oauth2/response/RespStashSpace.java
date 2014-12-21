package com.kimbrelk.da.oauth2.response;

import org.json.pc.JSONException;
import org.json.pc.JSONObject;

public final class RespStashSpace extends Response {
	private long mSpaceAvailable;
	private long mSpaceTotal;

	public RespStashSpace(long availableSpace, long totalSpace) {
		mSpaceAvailable = availableSpace;
		mSpaceTotal = totalSpace;
	}
	public RespStashSpace(JSONObject json) throws JSONException {
		mSpaceAvailable = json.getLong("available_space");
		mSpaceTotal = json.getLong("total_space");
	}

	public final long getAvailableSpace() {
		return mSpaceAvailable;
	}
	public final double getPercentFree() {
		return (double)mSpaceAvailable / (double)mSpaceTotal;
	}
	public final double getPercentUsed() {
		return 100.0 - getPercentFree();
	}
	public final long getTotalSpace() {
		return mSpaceTotal;
	}
}