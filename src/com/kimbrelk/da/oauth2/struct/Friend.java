package com.kimbrelk.da.oauth2.struct;

import org.json.JSONException;
import org.json.JSONObject;

public final class Friend extends Watcher {
	private boolean mIsWatchingYou;
	
	public Friend(JSONObject json) throws JSONException {
		super(json);
		mIsWatchingYou = json.getBoolean("watches_you");
	}
	
	public final boolean isWatchingYou() {
		return mIsWatchingYou;
	}
}