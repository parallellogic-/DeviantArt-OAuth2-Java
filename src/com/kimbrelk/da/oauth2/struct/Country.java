package com.kimbrelk.da.oauth2.struct;

import org.json.JSONObject;

public final class Country {
	private String mName;
	private int mId;
	
	public Country(JSONObject json) {
		mName = json.getString("name");
		mId = json.getInt("countryid");
	}
	
	public final String getName() {
		return mName;
	}
	public final int getId() {
		return mId;
	}
}
