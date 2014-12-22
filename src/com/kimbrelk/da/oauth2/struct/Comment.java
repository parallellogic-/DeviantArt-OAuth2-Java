package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public final class Comment {
	private String mBody;
	private Date mTimePosted;
	private String mId;
	private String mParentId;
	private int mReplies;
	private User mUser;
	
	public Comment(JSONObject json) throws JSONException {
		mBody = json.getString("body");
		mTimePosted = Util.stringToDate(json.getString("posted"));
		mId = json.getString("commentid");
		mParentId = json.getString("parentid");
		mReplies = json.getInt("replies");
		mUser = new User(json.getJSONObject("user"));
	}

	public final String getBody() {
		return mBody;
	}
	public final Date getTimePosted() {
		return mTimePosted;
	}
	public final String getId() {
		return mId;
	}
	public final String getParentId() {
		return mParentId;
	}
	public final int getNumReplies() {
		return mReplies;
	}
	public final User getUser() {
		return mUser;
	}
}