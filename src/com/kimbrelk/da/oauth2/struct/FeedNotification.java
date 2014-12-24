package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class FeedNotification {
	public enum Type {
		COMMENT_DEVIATION,
		COMMENT_PROFILE,
		FAVOURITE,
		MENTION_DEVIATION_IN_COMMENT,
		MENTION_DEVIATION_IN_DEVIATION,
		MENTION_DEVIATION_IN_STATUS,
		MENTION_USER_IN_COMMENT,
		MENTION_USER_IN_DEVIATION,
		MENTION_USER_IN_STATUS,
		REPLY,
		WATCH,
	}
	
	private Comment mComment;
	private Deviation mCommentDeviation;
	private Comment mCommentParent;
	private User mCommentProfile;
	private Deviation[] mDeviations;
	private Status mStatus;
	private Date mTimeStamp;
	private Type mType;
	private User mByUser;
	
	public FeedNotification(JSONObject json) throws JSONException {
		mTimeStamp = Util.stringToDate(json.getString("ts"));
		mByUser = new User(json.getJSONObject("by_user"));
		try {
			mType = Type.valueOf(json.getString("type").toUpperCase());
		}
		catch (IllegalArgumentException e) {
			
		}
		
		if (json.has("comment")) {
			mComment = new Comment(json.getJSONObject("comment"));
		}
		if (json.has("comment_deviation")) {
			mCommentDeviation = new Deviation(json.getJSONObject("comment_deviation"));
		}
		if (json.has("comment_parent")) {
			mCommentParent = new Comment(json.getJSONObject("comment_parent"));
		}
		if (json.has("comment_profile")) {
			mCommentProfile = new User(json.getJSONObject("comment_profile"));
		}
		if (json.has("deviations")) {
			JSONArray jsonDeviations = json.getJSONArray("deviations");
			mDeviations = new Deviation[jsonDeviations.length()];
			for(int a=0; a<mDeviations.length; a++) {
				mDeviations[a] = new Deviation(jsonDeviations.getJSONObject(a));
			}
		}
		if (json.has("status")) {
			mStatus = new Status(json.getJSONObject("status"));
		}
	}
	
	public final Comment getComment() {
		return mComment;
	}
	public final Deviation getCommentDeviation() {
		return mCommentDeviation;
	}
	public final Comment getCommentParent() {
		return mCommentParent;
	}
	public final User getCommentProfile() {
		return mCommentProfile;
	}
	public final Deviation[] getDeviations() {
		return mDeviations;
	}
	public final Status getStatus() {
		return mStatus;
	}
	public final Date getTimeStamp() {
		return mTimeStamp;
	}
	public final Type getType() {
		return mType;
	}
	public final User byUser() {
		return mByUser;
	}
}