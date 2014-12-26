package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashFolder extends Response {
	private long mFolderId;
	private String mFolderName;
	
	public RespStashFolder(JSONObject json) throws JSONException {
		mFolderId = json.getLong("folderid");
		mFolderName = json.getString("folder");
	}

	public final long getFolderId() {
		return mFolderId;
	}
	public final String getFolderName() {
		return mFolderName;
	}
}