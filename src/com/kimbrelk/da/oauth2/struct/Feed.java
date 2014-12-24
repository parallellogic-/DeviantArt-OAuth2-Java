package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Feed {
	public enum Type {
		DEVIATION_SUBMITTED,
		JOURNAL_SUBMITTED,
		USERNAME_CHANGE,
		STATUS,
		COLLECTION_UPDATE
	}

	private int mAddedCount;
	private SimpleFolder mCollection;
	private Comment mComment;
	private Deviation mCommentDeviation;
	private Comment mCommentParent;
	private User mCommentProfile;
	private String mCritiqueText;
	private Deviation[] mDeviations;
	private String mFormerly;
	private Poll mPoll;
	private Status mStatus;
	private Date mTimeStamp;
	private Type mType;
	private User mByUser;
	
	public Feed(JSONObject json) throws JSONException {
		mTimeStamp = Util.stringToDate(json.getString("ts"));
		mByUser = new User(json.getJSONObject("by_user"));
		try {
			mType = Type.valueOf(json.getString("type").toUpperCase());
		}
		catch (IllegalArgumentException e) {
			
		}
		
		if (json.has("added_count")) {
			mAddedCount = json.getInt("added_count");
		}
		if (json.has("collection")) {
			mCollection = new SimpleFolder(json.getJSONObject("collection"));
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
		if (json.has("critique_text")) {
			mCritiqueText = json.getString("critique_text");
		}
		if (json.has("deviations")) {
			JSONArray jsonDeviations = json.getJSONArray("deviations");
			mDeviations = new Deviation[jsonDeviations.length()];
			for(int a=0; a<mDeviations.length; a++) {
				mDeviations[a] = new Deviation(jsonDeviations.getJSONObject(a));
			}
		}
		if (json.has("formerly")) {
			mFormerly = json.getString("formerly");
		}
		if (json.has("poll")) {
			mPoll = new Poll(json.getJSONObject("poll"));
		}
		if (json.has("status")) {
			mStatus = new Status(json.getJSONObject("status"));
		}
	}
	
	public final int getAddedCount() {
		return mAddedCount;
	}
	public final SimpleFolder getCollection() {
		return mCollection;
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
	public final String getCritique() {
		return mCritiqueText;
	}
	public final Deviation[] getDeviations() {
		return mDeviations;
	}
	public final String getFormerUserName() {
		return mFormerly;
	}
	public final Poll getPoll() {
		return mPoll;
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