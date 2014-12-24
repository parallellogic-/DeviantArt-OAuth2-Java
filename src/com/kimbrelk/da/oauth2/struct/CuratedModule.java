package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class CuratedModule {
	public enum Type {
		COLLECTION,
		DEVIATION,
		EXPLORE,
		JOURNAL,
		VIDEO
	}
	
	private String mCampaign;
	private Deviation mCoverImage;
	private Deviation[] mDeviations;
	private Tag[] mExploreTags;
	private String mFolderId;
	private String mHeadline;
	private String mImageURL;
	private Tag[] mTags;
	private String mText;
	private Date mTimeStamp;
	private Type mType;
	private String mURL;
	private User mUser;
	private String mYoutubeId;
	
	public CuratedModule(JSONObject json) throws JSONException {
		try {
			mType = Type.valueOf(json.getString("module_name").toUpperCase());
		}
		catch (IllegalArgumentException e) {
			
		}
		mTimeStamp = Util.stringToDate(json.getString("publication_date"));
		if (json.has("campaign")) {
			mCampaign = json.getString("campaign");
		}
		if (json.has("cover_image")) {
			mCoverImage = new Deviation(json.getJSONObject("cover_image"));
		}
		if (json.has("deviations")) {
			JSONArray jsonDeviations = json.getJSONArray("deviations");
			mDeviations = new Deviation[jsonDeviations.length()];
			for(int a=0; a<mDeviations.length; a++) {
				mDeviations[a] = new Deviation(jsonDeviations.getJSONObject(a));
			}
		}
		if (json.has("explore_tags")) {
			JSONArray jsonExploreTags = json.getJSONArray("explore_tags");
			mExploreTags = new Tag[jsonExploreTags.length()];
			for(int a=0; a<mExploreTags.length; a++) {
				mExploreTags[a] = new Tag(jsonExploreTags.getJSONObject(a));
			}
		}
		if (json.has("folderid")) {
			mFolderId = json.getString("folderid");
		}
		if (json.has("headline")) {
			mHeadline = json.getString("headline");
		}
		if (json.has("image_url")) {
			mImageURL = json.getString("image_url");
		}
		if (json.has("tags")) {
			JSONArray jsonTags = json.getJSONArray("tags");
			mTags = new Tag[jsonTags.length()];
			for(int a=0; a<mTags.length; a++) {
				mTags[a] = new Tag(jsonTags.getJSONObject(a));
			}
		}
		if (json.has("text")) {
			mText = json.getString("text");
		}
		if (json.has("url")) {
			mURL = json.getString("url");
		}
		if (json.has("user")) {
			mUser = new User(json.getJSONObject("user"));
		}
		if (json.has("youtubeid")) {
			mYoutubeId = json.getString("youtubeid");
		}
	}

	public final String getCampaign() {
		return mCampaign;
	}
	public final Deviation getCoverImage() {
		return mCoverImage;
	}
	public final Deviation[] getDeviations() {
		return mDeviations;
	}
	public final Tag[] getExporeTags() {
		return mExploreTags;
	}
	public final String getFolderId() {
		return mFolderId;
	}
	public final String getHeadline() {
		return mHeadline;
	}
	public final String getImageURL() {
		return mImageURL;
	}
	public final Tag[] getTags() {
		return mTags;
	}
	public final String getText() {
		return mText;
	}
	public final Date getTimeStamp() {
		return mTimeStamp;
	}
	public final Type getType() {
		return mType;
	}
	public final String getURL() {
		return mURL;
	}
	public final User getUser() {
		return mUser;
	}
	public final String getYoutubeId() {
		return mYoutubeId;
	}
}