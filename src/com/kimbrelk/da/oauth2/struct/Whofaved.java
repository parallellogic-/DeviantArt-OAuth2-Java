package com.kimbrelk.da.oauth2.struct;

import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public final class Whofaved {
	private Date mTimeStamp;
	private User mUser;
	
	public Whofaved(JSONObject json) throws JSONException {
		mTimeStamp = new Date(json.getInt("time") * 1000);
		mUser = new User(json.getJSONObject("user"));
	}
	
	public final Date getTimeFavedAt() {
		return mTimeStamp;
	}
	public final User getUser() {
		return mUser;
	}
}