package com.kimbrelk.da.oauth2.struct;

import org.json.JSONException;
import org.json.JSONObject;

public final class Watch {
	private boolean mActivity;
	private boolean mCollections;
	private boolean mCritiques;
	private boolean mDeviations;
	private boolean mForums;
	private boolean mIsFriend;
	private boolean mJournals;
	private boolean mScraps;
	
	public Watch(boolean activity, boolean collections, boolean critiques, 
			boolean deviations, boolean forums, boolean isFriend, 
			boolean journals, boolean scraps) {
		mActivity = activity;
		mCollections = collections;
		mCritiques = critiques;
		mDeviations = deviations;
		mForums = forums;
		mIsFriend = isFriend;
		mJournals = journals;
		mScraps = scraps;
	}
	public Watch(JSONObject json) throws JSONException {
		mActivity = json.getBoolean("activity");
		mCollections = json.getBoolean("collections");
		mCritiques = json.getBoolean("critiques");
		mDeviations = json.getBoolean("deviations");
		mForums = json.getBoolean("forum_threads");
		mIsFriend = json.getBoolean("friend");
		mJournals = json.getBoolean("journals");
		mScraps = json.getBoolean("scraps");
	}
	
	public final String parameterize() {
		String ret = "";
		ret += "watch[activity]=" + mActivity + "&";
		ret += "watch[collections]=" + mCollections + "&";
		ret += "watch[critiques]=" + mCritiques + "&";
		ret += "watch[deviations]=" + mDeviations + "&";
		ret += "watch[forum_threads]=" + mForums + "&";
		ret += "watch[friend]=" + mIsFriend + "&";
		ret += "watch[journals]=" + mJournals + "&";
		ret += "watch[scraps]=" + mScraps;
		return ret;
	}
	
	public final boolean activity() {
		return mActivity;
	}
	public final boolean collections() {
		return mCollections;
	}
	public final boolean critiques() {
		return mCritiques;
	}
	public final boolean deviations() {
		return mDeviations;
	}
	public final boolean forums() {
		return mForums;
	}
	public final boolean isFriend() {
		return mIsFriend;
	}
	public final boolean journals() {
		return mJournals;
	}
	public final boolean scraps() {
		return mScraps;
	}
}