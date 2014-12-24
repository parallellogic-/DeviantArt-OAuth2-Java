package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Deviation;
import com.kimbrelk.da.oauth2.struct.SimpleFolder;
import com.kimbrelk.da.oauth2.struct.Status;
import com.kimbrelk.da.oauth2.struct.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespUserProfile extends Response {
	private String mArtistLevel;
	private String mArtistSpecialty;
	private String mBio;
	private SimpleFolder[] mCollections;
	private String mCountry;
	private int mCountryId;
	private String mCoverPhoto;
	private SimpleFolder[] mGalleries;
	private boolean mIsArtist;
	private boolean mIsWatching;
	private Status mLastStatus;
	private Deviation mProfilePic;
	private String mRealName;
	private Stats mStats;
	private String mTagline;
	private String mURL;
	private User mUser;
	private String mWebsite;
	
	public RespUserProfile(JSONObject json) throws JSONException {
		super();
		mArtistLevel = json.optString("artist_level");
		mArtistSpecialty = json.optString("artist_specialty");
		mBio = json.getString("bio");
		if (json.has("collections")) {
			JSONArray jsonCollections = json.getJSONArray("collections");
			mCollections = new SimpleFolder[jsonCollections.length()];
			for(int a=0; a<mCollections.length; a++) {
				mCollections[a] = new SimpleFolder(jsonCollections.getJSONObject(a));
			}
		}
		mCountry = json.getString("country");
		mCountryId = json.getInt("countryid");
		mCoverPhoto = json.optString("cover_photo");
		if (json.has("galleries")) {
			JSONArray jsonGalleries = json.getJSONArray("galleries");
			mGalleries = new SimpleFolder[jsonGalleries.length()];
			for(int a=0; a<mGalleries.length; a++) {
				mGalleries[a] = new SimpleFolder(jsonGalleries.getJSONObject(a));
			}
		}
		mIsArtist = json.getBoolean("user_is_artist");
		mIsWatching = json.getBoolean("is_watching");
		mLastStatus = new Status(json.getJSONObject("last_status"));
		mProfilePic = new Deviation(json.getJSONObject("profile_pic"));
		mRealName = json.getString("real_name");
		mStats = new Stats(json.getJSONObject("stats"));
		mTagline = json.getString("tagline");
		mURL = json.getString("profile_url");
		mUser = new User(json.getJSONObject("user"));
		mWebsite = json.getString("website");
	}

	public final String getArtistLevel() {
		return mArtistLevel;
	}
	public final String getArtistSpecialty() {
		return mArtistSpecialty;
	}
	public final String getBio() {
		return mBio;
	}
	public final SimpleFolder[] getCollections() {
		return mCollections;
	}
	public final String getCountry() {
		return mCountry;
	}
	public final int getCountryId() {
		return mCountryId;
	}
	public final String getCoverPhoto() {
		return mCoverPhoto;
	}
	public final SimpleFolder[] getGalleries() {
		return mGalleries;
	}
	public final Status getLastStatus() {
		return mLastStatus;
	}
	public final Deviation getProfilePic() {
		return mProfilePic;
	}
	public final String getRealName() {
		return mRealName;
	}
	public final Stats getStats() {
		return mStats;
	}
	public final String getTagline() {
		return mTagline;
	}
	public final String getURL() {
		return mURL;
	}
	public final User getUser() {
		return mUser;
	}
	public final String getWebsite() {
		return mWebsite;
	}
	
	public final boolean isArtist() {
		return mIsArtist;
	}
	public final boolean isWatching() {
		return mIsWatching;
	}
	
	public final class Stats {
		private int mNumProfilePageviews;
		private int mNumProfileComments;
		private int mNumUserComments;
		private int mNumUserDeviations;
		private int mNumUserFavourites;
		
		public Stats(JSONObject json) throws JSONException {
			mNumProfilePageviews = json.getInt("profile_pageviews");
			mNumProfileComments = json.getInt("profile_comments");
			mNumUserComments = json.getInt("user_comments");
			mNumUserDeviations = json.getInt("user_deviations");
			mNumUserFavourites = json.getInt("user_favourites");
		}

		public final int getProfileViews() {
			return mNumProfilePageviews;
		}
		public final int getProfileComments() {
			return mNumProfileComments;
		}
		public final int getUserComments() {
			return mNumUserComments;
		}
		public final int getUserDeviations() {
			return mNumUserDeviations;
		}
		public final int getUserFavourites() {
			return mNumUserFavourites;
		}
	}
}