package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class Watcher {
	private boolean mIsWatching;
	private Date mLastVisit;
	private User mUser;
	private Watch mWatch;
	
	public Watcher(JSONObject json) throws JSONException {
		mIsWatching = json.getBoolean("is_watching");
		mLastVisit = Util.stringToDate(json.optString("lastvisit"));
		mUser = new User(json.getJSONObject("user"));
		mWatch = new Watch(json.getJSONObject("watch"));
	}
	public Watcher fromJSON(JSONObject json) throws JSONException {
		return new Watcher(json);
	}
	
	public final boolean isWatching() {
		return mIsWatching;
	}
	public final Date getLastVisit() {
		return mLastVisit;
	}
	public final User getUser() {
		return mUser;
	}
	public final Watch getWatchInfo() {
		return mWatch;
	}

}