package com.kimbrelk.da.oauth2.struct;

import com.kimbrelk.da.util.Util;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class DeviationMetadata {
	private boolean mAllowsComments;
	private User mAuthor;
	private Camera mCamera;
	private SimpleFolder[] mCollections;
	private String mDescription;
	private boolean mIsFavourited;
	private boolean mIsWatching;
	private String mLicense;
	private String mPrintId;
	private Stats mStats;
	private Submission mSubmission;
	private Tag[] mTags;
	private String mTitle;
	private String mId;
	
	public DeviationMetadata(JSONObject json) throws JSONException {
		mAllowsComments = json.getBoolean("allows_comments");
		mAuthor = new User(json.getJSONObject("author"));
		if (json.has("camera")) {
			mCamera = new Camera(json.getJSONObject("camera"));
		}
		if (json.has("collections")) {
			JSONArray jsonCollections = json.getJSONArray("collections");
			mCollections = new SimpleFolder[jsonCollections.length()];
			for(int a=0; a<mCollections.length; a++) {
				mCollections[a] = new SimpleFolder(jsonCollections.getJSONObject(a));
			}
		}
		mDescription = json.getString("description");
		mId = json.getString("deviationid");
		mIsFavourited = json.getBoolean("is_favourited");
		mIsWatching = json.getBoolean("is_watching");
		mLicense = json.getString("license");
		mPrintId = json.optString("printid");
		if (json.has("stats")) {
			mStats = new Stats(json.getJSONObject("stats"));
		}
		if (json.has("submission")) {
			mSubmission = new Submission(json.getJSONObject("submission"));
		}
		JSONArray jsonTags = json.getJSONArray("tags");
		mTags = new Tag[jsonTags.length()];
		for(int a=0; a<mTags.length; a++) {
			mTags[a] = new Tag(jsonTags.getJSONObject(a));
		}
		mTitle = json.getString("title");
	}

	public final boolean allowsComments() {
		return mAllowsComments;
	}
	
	public final boolean isFavourited() {
		return mIsFavourited;
	}
	public final boolean isWatching() {
		return mIsWatching;
	}
	
	public final User getAuthor() {
		return mAuthor;
	}
	public final Camera getCamera() {
		return mCamera;
	}
	public final String getDescription() {
		return mDescription;
	}
	public final String getId() {
		return mId;
	}
	public final String getLicense() {
		return mLicense;
	}
	public final String getPrintId() {
		return mPrintId;
	}
	public final Stats getStats() {
		return mStats;
	}
	public final Submission getSubmission() {
		return mSubmission;
	}
	public final Tag[] getTags() {
		return mTags;
	}
	public final String getTitle() {
		return mTitle;
	}
	
	public final class App {
		private String mName;
		private String mURL;
		
		public App(JSONObject json) throws JSONException {
			mName = json.getString("app");
			mURL = json.getString("url");
		}

		public final String getName() {
			return mName;
		}
		public final String getURL() {
			return mURL;
		}
	}
	public final class Camera {
		
		public Camera(JSONObject json) throws JSONException {
			// TODO
		}
	}
	public final class Stats {
		private int mComments;
		private int mDownloads;
		private int mDownloadsToday;
		private int mFavourites;
		private int mViews;
		private int mViewsToday;
		
		public Stats(JSONObject json) throws JSONException {
			mComments = json.getInt("comments");
			mDownloads = json.getInt("downloads");
			mDownloadsToday = json.getInt("downloads_today");
			mFavourites = json.getInt("favourites");
			mViews = json.getInt("views");
			mViewsToday = json.getInt("views_today");
		}

		public final int getComments() {
			return mComments;
		}
		public final int getDownloads() {
			return mDownloads;
		}
		public final int getDownloadsToday() {
			return mDownloadsToday;
		}
		public final int getFavourites() {
			return mFavourites;
		}
		public final int getViews() {
			return mViews;
		}
		public final int getViewsToday() {
			return mViewsToday;
		}
	}
	public final class Submission {
		private String mCategory;
		private Date mCreationTime;
		private String mFileSize;
		private String mResolution;
		private App mSubmittedWith;
		
		public Submission(JSONObject json) throws JSONException {
			mCategory = json.getString("category");
			mCreationTime = Util.stringToDate(json.getString("creation_time"));
			if (json.has("file_size")) {
				mFileSize = json.getString("file_size");
			}
			if (json.has("resolution")) {
				mResolution = json.getString("resolution");
			}
			mSubmittedWith = new App(json.getJSONObject("submitted_with"));
		}

		public final String getCategory() {
			return mCategory;
		}
		public final Date getCreationTime() {
			return mCreationTime;
		}
		public final String getFileSize() {
			return mFileSize;
		}
		public final String getResolution() {
			return mResolution;
		}
		public final App getSubmitionApp() {
			return mSubmittedWith;
		}
	}
}