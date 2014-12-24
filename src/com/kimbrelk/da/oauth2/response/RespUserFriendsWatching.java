package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public class RespUserFriendsWatching extends Response {
	private boolean mIsWatching;
	
	public RespUserFriendsWatching(JSONObject json) throws JSONException {
		mIsWatching = json.getBoolean("watching");
	}
	
	public final boolean isWatching() {
		return mIsWatching;
	}
}