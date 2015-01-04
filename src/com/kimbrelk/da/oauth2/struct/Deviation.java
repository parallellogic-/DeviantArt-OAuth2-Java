package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Deviation {
	private boolean mAllowsComments;
	private User mAuthor;
	private String mCategory;
	private String mCategoryPath;
	private Image mContent;
	private DailyDeviation mDailyDeviation;
	private String mExcerpt;
	private Flash mFlash;
	private boolean mIsDeleted;
	private boolean mIsFavourited;
	private boolean mIsMature;
	private Image mPreview;
	private String mPrintId;
	private long mPublishedTime;
	private Stats mStats;
	private Image[] mThumbs;
	private String mTitle;
	private String mURL;
	private String mUUID;
	private Video[] mVideos;
	
	public Deviation(JSONObject json) throws JSONException {
		mAllowsComments = json.getBoolean("allows_comments");
		mAuthor = new User(json.getJSONObject("author"));
		mCategory = json.getString("category");
		mCategoryPath = json.getString("category_path");
		if (json.has("content")) {
			mContent = new Image(json.getJSONObject("content"));
		}
		if (json.has("daily_deviation")) {
			mDailyDeviation = new DailyDeviation(json.getJSONObject("daily_deviation"));
		}
		if (json.has("excerpt")) {
			mExcerpt = json.getString("excerpt");
		}
		if (json.has("flash")) {
			mFlash = new Flash(json.getJSONObject("flash"));
		}
		mIsDeleted = json.getBoolean("is_deleted");
		mIsFavourited = json.getBoolean("is_favourited");
		mIsMature = json.getBoolean("is_mature");
		if (json.has("preview")) {
			mPreview = new Image(json.getJSONObject("preview"));
		}
		mPrintId = json.optString("printid");
		mPublishedTime = json.getLong("published_time");
		mStats = new Stats(json.getJSONObject("stats"));
		if (json.has("thumbs")) {
			JSONArray jsonThumbs = json.getJSONArray("thumbs");
			mThumbs = new Image[jsonThumbs.length()];
			for(int a=0; a<mThumbs.length; a++) {
				mThumbs[a] = new Image(jsonThumbs.getJSONObject(a));
			}
		}
		mTitle = json.getString("title");
		mURL = json.getString("url");
		mUUID = json.getString("deviationid");
		if (json.has("videos")) {
			JSONArray jsonVideos = json.getJSONArray("videos");
			mVideos = new Video[jsonVideos.length()];
			for(int a=0; a<mVideos.length; a++) {
				mVideos[a] = new Video(jsonVideos.getJSONObject(a));
			}
		}
	}

	public final boolean allowsComments() {
		return mAllowsComments;
	}

	public final boolean isDeleted() {
		return mIsDeleted;
	}
	public final boolean isFavourited() {
		return mIsFavourited;
	}
	public final boolean isMature() {
		return mIsMature;
	}
	
	public final User getAuthor() {
		return mAuthor;
	}
	public final String getCategory() {
		return mCategory;
	}
	public final String getCategoryPath() {
		return mCategoryPath;
	}
	public final Image getContent() {
		return mContent;
	}
	public final DailyDeviation getDailyDeviation() {
		return mDailyDeviation;
	}
	public final String getExerpt() {
		return mExcerpt;
	}
	public final Flash getFlash() {
		return mFlash;
	}
	public final Image getPreview() {
		return mPreview;
	}
	public final String getPrintId() {
		return mPrintId;
	}
	public final long getPublishedTime() {
		return mPublishedTime;
	}
	public final Stats getStats() {
		return mStats;
	}
	public final Image[] getThumbs() {
		return mThumbs;
	}
	public final String getTitle() {
		return mTitle;
	}
	public final String getURL() {
		return mURL;
	}
	public final String getId() {
		return mUUID;
	}
	public final Video[] getVideos() {
		return mVideos;
	}
	
	public final class DailyDeviation {
		private String mBody;
		private User mGiver;
		private User mSuggester;
		private Date mTime;
		
		public DailyDeviation(JSONObject json) throws JSONException {
			mBody = json.getString("body");
			mGiver = new User(json.getJSONObject("giver"));
			if (json.has("suggester")) {
				mSuggester = new User(json.getJSONObject("suggester"));
			}
			mTime = Util.stringToDate(json.getString("time"));
		}

		public final String getBody() {
			return mBody;
		}
		public final User getGiver() {
			return mGiver;
		}
		public final User getSuggester() {
			return mSuggester;
		}
		public final Date getTime() {
			return mTime;
		}
	}
	public final class Flash {
		private int mHeight;
		private String mSource;
		private int mWidth;
		
		public Flash(JSONObject json) throws JSONException {
			mHeight = json.getInt("height");
			mSource = json.getString("src");
			mWidth = json.getInt("width");
		}

		public final int getHeight() {
			return mHeight;
		}
		public final String getSource() {
			return mSource;
		}
		public final int getWidth() {
			return mWidth;
		}
	}
	public final class Image {
		private int mHeight;
		private boolean mIsTransparent;
		private String mSource;
		private int mWidth;
		
		public Image(JSONObject json) throws JSONException {
			mHeight = json.getInt("height");
			mIsTransparent = json.getBoolean("transparency");
			mSource = json.getString("src");
			mWidth = json.getInt("width");
		}

		public final int getHeight() {
			return mHeight;
		}
		public final String getSource() {
			return mSource;
		}
		public final int getWidth() {
			return mWidth;
		}
		
		public final boolean isTransparent() {
			return mIsTransparent;
		}
	}
	public final class Stats {
		private int mComments;
		private int mFavourites;
		
		public Stats(JSONObject json) throws JSONException {
			mComments = json.getInt("comments");
			mFavourites = json.getInt("favourites");
		}

		public final int getComments() {
			return mComments;
		}
		public final int getFavourites() {
			return mFavourites;
		}
	}
	public final class Video {
		private int mDuration;
		private long mFileSize;
		private String mQuality;
		private String mSource;
		
		public Video(JSONObject json) throws JSONException {
			mDuration = json.getInt("duration");
			mFileSize = json.getLong("filesize");
			mQuality = json.getString("quality");
			mSource = json.getString("src");
		}

		public final int getDuration() {
			return mDuration;
		}
		public final long getFileSize() {
			return mFileSize;
		}
		public final String getQuality() {
			return mQuality;
		}
		public final String getSource() {
			return mSource;
		}
	}
}