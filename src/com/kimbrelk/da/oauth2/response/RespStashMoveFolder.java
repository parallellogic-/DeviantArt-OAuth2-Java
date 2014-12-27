package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashMoveFolder extends Response {
	private String mFolder;
	private long mFolderId;
	private int mPosition;
	
	public RespStashMoveFolder(JSONObject json) throws JSONException {
		super();
		mFolder = json.getString("folder");
		mFolderId = json.getLong("folderid");
		if (json.has("position")) {
			mPosition = json.getInt("position");
		}
	}
	
	public final String getFolder() {
		return mFolder;
	}
	public final long getFolderId() {
		return mFolderId;
	}
	public final int getPosition() {
		return mPosition;
	}
}