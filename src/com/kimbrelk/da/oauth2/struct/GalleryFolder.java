package com.kimbrelk.da.oauth2.struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class GalleryFolder {
	private Deviation[] mDeviations;
	private String mId;
	private String mName;
	private String mParentId;
	private int mSize;
	
	public GalleryFolder(JSONObject json) throws JSONException {
		if (json.has("deviations")) {
			JSONArray jsonDeviations = json.getJSONArray("deviations");
			mDeviations = new Deviation[jsonDeviations.length()];
			for(int a=0; a<mDeviations.length; a++) {
				mDeviations[a] = new Deviation(jsonDeviations.getJSONObject(a));
			}
		}
		mId = json.getString("folderid");
		mName = json.getString("name");
		mParentId = json.optString("parent");
		mSize = -1;
		if (json.has("size")) {
			mSize = json.getInt("size");
		}
	}

	public final Deviation[] getDeviations() {
		return mDeviations;
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
	public final int getSize() {
		return mSize;
	}
}