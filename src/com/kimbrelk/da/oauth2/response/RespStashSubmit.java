package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashSubmit extends Response {
	private String mFolderName;
	private long mFolderId;
	private long mStashId;
	
	public RespStashSubmit(JSONObject json) throws JSONException {
		super();
		mFolderId = -1;
		if (json.has("folderid")) {
			mFolderId = json.getLong("folderid");
		}
		if (json.has("folder")) {
			mFolderName = json.getString("folder");
		}
		mStashId = json.getLong("stashid");
	}

	public final long getFolderId() {
		return mFolderId;
	}
	public final String getFolderName() {
		return mFolderName;
	}
	public final long getStashId() {
		return mStashId;
	}
}