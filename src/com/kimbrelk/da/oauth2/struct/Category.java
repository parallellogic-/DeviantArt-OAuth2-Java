package com.kimbrelk.da.oauth2.struct;

import org.json.JSONException;
import org.json.JSONObject;

public final class Category {
	private boolean mHasSubcategory;
	private String mParentPath;
	private String mPath;
	private String mTitle;
	
	public Category(JSONObject json) throws JSONException {
		mHasSubcategory = json.getBoolean("has_subcategory");
		mParentPath = json.getString("parent_catpath");
		mPath = json.getString("catpath");
		mTitle = json.getString("title");
	}

	public final boolean hasSubCategory() {
		return mHasSubcategory;
	}
	public final String getParentPath() {
		return mParentPath;
	}
	public final String getPath() {
		return mPath;
	}
	public final String getTitle() {
		return mTitle;
	}
}