package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashMoveFile extends Response {
	private long mFolderId;
	private int mPosition;
	private long mStashId;
	
	public RespStashMoveFile(JSONObject json) throws JSONException {
		super();
		mFolderId = json.getLong("folderid");
		if (json.has("position")) {
			mPosition = json.getInt("position");
		}
		mStashId = json.getLong("stashid");
	}

	public final long getFolderId() {
		return mFolderId;
	}
	public final int getPosition() {
		return mPosition;
	}
	public final long getStashId() {
		return mStashId;
	}
}