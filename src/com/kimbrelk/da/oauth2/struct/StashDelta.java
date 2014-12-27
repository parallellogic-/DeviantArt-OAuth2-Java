package com.kimbrelk.da.oauth2.struct;

import org.json.JSONException;
import org.json.JSONObject;

public final class StashDelta {
	private long mFolderId;
	private StashMetadata mMetadata;
	private int mPosition;
	private long mStashId;
	
	public StashDelta(JSONObject json) throws JSONException {
		mFolderId = json.getLong("folderid");
		mMetadata = new StashMetadata(json.getJSONObject("metadata"));
		mPosition = -1;
		if (json.has("position")) {
			mPosition = json.getInt("position");
		}
		mStashId = -1;
		if (json.has("stashid")) {
			mStashId = json.getLong("stashid");
		}
	}
	
	public final boolean isStashItem() {
		return mStashId != -1;
	}
	
	public final long getFolderId() {
		return mFolderId;
	}
	public final StashMetadata getMetadata() {
		return mMetadata;
	}
	public final int getPosition() {
		return mPosition;
	}
	public final long getStashId() {
		return mStashId;
	}
}