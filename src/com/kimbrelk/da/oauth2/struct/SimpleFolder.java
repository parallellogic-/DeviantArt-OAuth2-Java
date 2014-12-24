package com.kimbrelk.da.oauth2.struct;

import org.json.JSONException;
import org.json.JSONObject;

public final class SimpleFolder {
	private String mId;
	private String mName;
	private String mParentId;
	
	public SimpleFolder(JSONObject json) throws JSONException {
		mId = json.getString("folderid");
		mName = json.getString("name");
		if (json.has("parent")) {
			mParentId = json.optString("parent");
		}
	}

	public final String getId() {
		return mId;
	}
	public final String getName() {
		return mName;
	}
	public final String getParentId() {
		return mParentId;
	}
}