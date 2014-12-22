package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
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

	private Details mDetails;
	private Geo mGeo;
	private String mIcon;
	private String mId;
	private String mName;
	private Profile mProfile;
	private Stats mStats;
	private Type mType;
	
	public User(JSONObject json) throws JSONException {
		mIcon = json.getString("usericon");
		mId = json.getString("userid");
		mName = json.getString("username");
		mType = null;
		if (json.has("type")) {
			try {
				mType = Type.valueOf(json.getString("type").toUpperCase().replace("-", "_"));
			}
			catch (IllegalArgumentException e) {
				
			}
		}
		if (json.has("details")) {
			mDetails = new Details(json.getJSONObject("details"));
		}
		if (json.has("geo")) {
			mGeo = new Geo(json.getJSONObject("details"));
		}
		if (json.has("profile")) {
			mProfile = new Profile(json.getJSONObject("details"));
		}
		if (json.has("stats")) {
			mStats = new Stats(json.getJSONObject("details"));
		}
	}
	
	public final Details getDetails() {
		return mDetails;
	}
	public final Geo getGeo() {
		return mGeo;
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
	public final Profile getProfile() {
		return mProfile;
	}
	public final Stats getStats() {
		return mStats;
	}
	/**
	 * @return User type
	 *  Will return null when using /user/whoami
	 */
	public final Type getType() {
		return mType;
	}
	
	public final class Details {
		private int mAge;
		private String mGender;
		private Date mJoinDate;
		
		public Details(String gender, int age, String joinDate) {
			mGender = gender;
			mAge = age;
			mJoinDate = Util.stringToDate(joinDate);
		}
		public Details(JSONObject json) throws JSONException {
			mGender = json.getString("sex");
			mAge = json.getInt("age");
			mJoinDate = Util.stringToDate(json.getString("joindate"));
		}
		
		public final int getAge() {
			return mAge;
		}
		public final String getGender() {
			return mGender;
		}
		public final Date getJoinDate() {
			return mJoinDate;
		}
		
		public final boolean isMale() {
			return mGender != null && 
				(mGender.equalsIgnoreCase("m") || mGender.equalsIgnoreCase("male"));
		}
		public final boolean isFemale() {
			return mGender != null && 
				(mGender.equalsIgnoreCase("f") || mGender.equalsIgnoreCase("female"));
		}
	}
	public final class Geo {
		private String mCountry;
		private int mCountryId;
		private String mTimezone;
		
		public Geo(String country, int countryId, String timezone) {
			mCountry = country;
			mCountryId = countryId;
			mTimezone = timezone;
		}
		public Geo(JSONObject json) throws JSONException {
			mCountry = json.getString("country");
			mCountryId = json.getInt("countryid");
			mTimezone = json.getString("timezone");
		}

		public final String getCountry() {
			return mCountry;
		}
		public final int getCountryId() {
			return mCountryId;
		}
		public final String getTimezone() {
			return mTimezone;
		}
	}
	public final class Profile {
		private String mArtistLevel;
		private String mArtistSpeciality;
		private String mCoverPhoto;
		private boolean mIsArtist;
		private Deviation mProfilePic;
		private String mRealName;
		private String mTagline;
		private String mWebsite;
		
		public Profile(JSONObject json) throws JSONException {
			mArtistLevel = json.getString("artist_level");
			mArtistSpeciality = json.getString("artist_speciality");
			mCoverPhoto = json.getString("cover_photo");
			mIsArtist = json.getBoolean("user_is_artist");
			mProfilePic = new Deviation(json.getJSONObject("profile_pic"));
			mRealName = json.getString("real_name");
			mTagline = json.getString("tagline");
			mWebsite = json.getString("website");
		}

		public final boolean isArtist() {
			return mIsArtist;
		}

		public final String getArtistLevel() {
			return mArtistLevel;
		}
		public final String getArtistSpeciality() {
			return mArtistSpeciality;
		}
		public final String getCoverPhoto() {
			return mCoverPhoto;
		}
		public final Deviation getProfilePic() {
			return mProfilePic;
		}
		public final String getRealName() {
			return mRealName;
		}
		public final String getTagline() {
			return mTagline;
		}
		public final String get() {
			return mWebsite;
		}
	}
	public final class Stats {
		private int mFriends;
		private int mWatchers;
		
		public Stats(int watchers, int friends) {
			mFriends = friends;
			mWatchers = watchers;
		}
		public Stats(JSONObject json) throws JSONException {
			mFriends = json.getInt("friends");
			mWatchers = json.getInt("watchers");
		}
		
		public final int getFriends() {
			return mFriends;
		}
		public final int getWatchers() {
			return mWatchers;
		}
	}
}