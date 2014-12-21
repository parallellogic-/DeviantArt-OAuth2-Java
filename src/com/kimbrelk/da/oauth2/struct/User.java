package com.kimbrelk.da.oauth2.struct;

import org.json.JSONException;
import org.json.JSONObject;

public final class User {
	public enum Type {
		ADMIN,
		BANNED,
		BETA,
		HELL,
		HELL_BETA,
		PREMIUM,
		REGULAR,
		SENIOR,
		VOLUNTEER
	}
	
	private String mIcon;
	private String mId;
	private String mName;
	private Type mType;

	public User(String name, String id, String type, String icon) {
		mIcon = icon;
		mId = id;
		mName = name;
		if (type != null) {
			mType = Type.valueOf(type.toUpperCase().replace("-", "_"));
		}
		else {
			mType = null;
		}
	}
	public User(JSONObject json) throws JSONException {
		mIcon = json.getString("usericon");
		mId = json.getString("userid");
		mName = json.getString("username");
		mType = null;
		if (json.has("type")) {
			mType = Type.valueOf(json.getString("type").toUpperCase().replace("-", "_"));
		}
	}
	
	public final String getIcon() {
		return mIcon;
	}
	public final String getId() {
		return mId;
	}
	public final String getName() {
		return mName;
	}
	/**
	 * @return User type
	 *  Will return null when using /user/whoami
	 */
	public final Type getType() {
		return mType;
	}
}