package com.kimbrelk.da.oauth2.struct;

import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class StashMetadata {
	private String mArtistComments;
	private Camera mCamera;
	private String mCategory;
	private StashMetadata[] mContents;
	private Date mCreationTime;
	private String mDescription;
	private long mFolderId;
	private boolean mIsFolder;
	private String mKeywords;
	private String mOriginalURL;
	private long mParentId;
	private String mPath;
	private long mSize;
	private long mStashId;
	private Stats mStats;
	private Submission mSubmission;
	private String mThumb;
	private String mTitle;
	
	public StashMetadata(JSONObject json) throws JSONException {
		if (json.has("artist_comments")) {
			mArtistComments = json.getString("artist_comments");
		}
		if (json.has("camera")) {
			mCamera = new Camera(json.getJSONObject("camera"));
		}
		if (json.has("category")) {
			mCategory = json.getString("category");
		}
		if (json.has("contents")) {
			JSONArray jsonContents = json.getJSONArray("contents");
			mContents = new StashMetadata[jsonContents.length()];
			for(int a=0; a<mContents.length; a++) {
				mContents[a] = new StashMetadata(jsonContents.getJSONObject(a));
			}
		}
		if (json.has("creation_time")) {
			mCreationTime = new Date(json.getLong("creation_time") * 1000);
		}
		mDescription = json.optString("description");
		mFolderId = json.getLong("folderid");
		mIsFolder = json.getBoolean("is_folder");
		if (json.has("keywords")) {
			mKeywords = json.optString("keywords");
		}
		if (json.has("original_url")) {
			mOriginalURL = json.optString("original_url");
		}
		if (json.has("parentid")) {
			mParentId = json.getLong("parentid");
		}
		if (json.has("path")) {
			mPath = json.getString("path");
		}
		if (json.has("size")) {
			mSize = json.getLong("size");
		}
		if (json.has("stashid")) {
			mStashId = json.getLong("stashid");
		}
		if (json.has("stats")) {
			mStats = new Stats(json.getJSONObject("stats"));
		}
		if (json.has("submission")) {
			mSubmission = new Submission(json.getJSONObject("submission"));
		}
		if (json.has("thumb")) {
			mThumb = json.getString("thumb");
		}
		mTitle = json.getString("title");
	}
	
	public final boolean isFolder() {
		return mIsFolder;
	}
	
	public final String getArtistComments() {
		return mArtistComments;
	}
	public final Camera getCamera() {
		return mCamera;
	}
	public final String getCategory() {
		return mCategory;
	}
	public final StashMetadata[] getContents() {
		return mContents;
	}
	public final Date getCreationTime() {
		return mCreationTime;
	}
	public final String getDescription() {
		return mDescription;
	}
	public final long getFolderId() {
		return mFolderId;
	}
	public final String getKeywords() {
		return mKeywords;
	}
	public final String getOriginalURL() {
		return mOriginalURL;
	}
	public final long getParentId() {
		return mParentId;
	}
	public final String getPath() {
		return mPath;
	}
	public final long getSize() {
		return mSize;
	}
	public final long getStashId() {
		return mStashId;
	}
	public final Stats getStats() {
		return mStats;
	}
	public final Submission getSubmission() {
		return mSubmission;
	}
	public final String getThumb() {
		return mThumb;
	}
	public final String getTitle() {
		return mTitle;
	}
	
	public final class App {
		private String mName;
		private String mURL;
		
		public App(JSONObject json) throws JSONException {
			if (json.has("app")) {
				mName = json.getString("app");
			}
			if (json.has("url")) {
				mURL = json.getString("url");
			}
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
	public final class Files {
		private String mFullview;
		private String mOriginal;
		private String mPreview;
		private String mSize150;
		private String mSize200h;
		private String mSize300w;
		
		public Files(JSONObject json) throws JSONException {
			if (json.has("fullview")) {
				mFullview = json.getString("fullview");
			}
			if (json.has("original")) {
				mOriginal = json.getString("original");
			}
			if (json.has("preview")) {
				mPreview = json.getString("preview");
			}
			if (json.has("150")) {
				mSize150 = json.getString("150");
			}
			if (json.has("200H")) {
				mSize200h = json.getString("200H");
			}
			if (json.has("300W")) {
				mSize300w = json.getString("300W");
			}
		}

		public final String getFullview() {
			return mFullview;
		}
		public final String getOriginal() {
			return mOriginal;
		}
		public final String getPreview() {
			return mPreview;
		}
		public final String get150() {
			return mSize150;
		}
		public final String get200H() {
			return mSize200h;
		}
		public final String get300W() {
			return mSize300w;
		}
	}
	public final class Stats {
		private int mDownloads;
		private int mDownloadsToday;
		private int mViews;
		private int mViewsToday;
		
		public Stats(JSONObject json) throws JSONException {
			mDownloads = json.getInt("downloads");
			mDownloadsToday = json.getInt("downloads_today");
			mViews = json.getInt("views");
			mViewsToday = json.getInt("views_today");
		}

		public final int getDownloads() {
			return mDownloads;
		}
		public final int getDownloadsToday() {
			return mDownloadsToday;
		}
		public final int getViews() {
			return mViews;
		}
		public final int getViewsToday() {
			return mViewsToday;
		}
	}
	public final class Submission {
		private String mFileSize;
		private String mResolution;
		private App mSubmittedWith;
		
		public Submission(JSONObject json) throws JSONException {
			if (json.has("file_size")) {
				mFileSize = json.getString("file_size");
			}
			if (json.has("resolution")) {
				mResolution = json.getString("resolution");
			}
			mSubmittedWith = new App(json.getJSONObject("submitted_with"));
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