package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Status {
	private User mAuthor;
	private String mBody;
	private int mCommentCount;
	private String mId;
	private boolean mIsDeleted;
	private boolean mIsShare;
	private Date mTimeStamp;
	private String mURL;
	private Item[] mItems;
	
	public Status(JSONObject json) throws JSONException {
		if (json.has("author")) {
			mAuthor = new User(json.getJSONObject("author"));
		}
		if (json.has("body")) {
			mBody = json.getString("body");
		}
		mCommentCount = -1;
		if (json.has("comments_count")) {
			mCommentCount = json.getInt("comments_count");
		}
		if (json.has("statusid")) {
			mId = json.getString("statusid");
		}
		mIsDeleted = json.getBoolean("is_deleted");
		if (json.has("is_share")) {
			mIsShare = json.getBoolean("is_share");
		}
		if (json.has("ts")) {
			mTimeStamp = Util.stringToDate(json.getString("ts"));
		}
		if (json.has("url")) {
			mURL = json.getString("url");
		}
		mItems = new Item[0];
		if (json.has("items")) {
			JSONArray jsonItems = json.getJSONArray("items");
			mItems = new Item[jsonItems.length()];
			for(int a=0; a<mItems.length; a++) {
				mItems[a] = new Item(jsonItems.getJSONObject(a));
			}
		}
	}

	public final boolean isDeleted() {
		return mIsDeleted;
	}
	public final boolean isShare() {
		return mIsShare;
	}
	
	public final User getAuthor() {
		return mAuthor;
	}
	public final String getBody() {
		return mBody;
	}
	public final int getCommentCount() {
		return mCommentCount;
	}
	public final String getId() {
		return mId;
	}
	public final Date getTimeStamp() {
		return mTimeStamp;
	}
	public final String getURL() {
		return mURL;
	}
	public final Item[] getItems() {
		return mItems;
	}
	
	public final static class Item {
		public enum Type {
			DEVIATION,
			STATUS
		}

		private Deviation mDeviation;
		private Status mStatus;
		private Type mType;
		
		public Item(JSONObject json) throws JSONException {
			try {
				mType = Type.valueOf(json.getString("type").toUpperCase());
				if (mType == Type.DEVIATION) {
					mDeviation = new Deviation(json.getJSONObject("deviation"));
				}
				else if (mType == Type.STATUS) {
					mStatus = new Status(json.getJSONObject("status"));
				}
			}
			catch (IllegalArgumentException e) {
				
			}
		}

		public Deviation getDeviation() {
			return mDeviation;
		}
		public Status getStatus() {
			return mStatus;
		}
		public Type getType() {
			return mType;
		}
	}
}