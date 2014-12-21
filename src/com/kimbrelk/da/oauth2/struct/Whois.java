package com.kimbrelk.da.oauth2.struct;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.kimbrelk.da.util.Util;

public final class Whois {
	private User mUser;
	private String mRealName;
	private String mTypeName;
	private Date mJoinDate;
	
	public Whois(User user, String realName, String typeName, String joinDate) {
		mUser = user;
		mRealName = realName;
		mTypeName = typeName;
		mJoinDate = new Date(joinDate);
	}
	public Whois(JSONObject json) throws JSONException {
		mUser = new User(json.getJSONObject("user"));
		mRealName = json.getString("realname");
		mTypeName = json.getString("typename");
		mJoinDate = Util.stringToDate(json.getString("joindate"));
	}
	
	public final Date getJoinDate() {
		return mJoinDate;
	}
	public final String getRealName() {
		return mRealName;
	}
	public final String getTypeName() {
		return mTypeName;
	}
	public final User getUser() {
		return mUser;
	}
	public final String getUserName() {
		return mUser.getName();
	}
}