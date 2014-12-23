package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Category;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespCategory extends Response {
	private final Category[] mCategories;
	
	public RespCategory(JSONObject json) throws JSONException {
		super();
		JSONArray jsonCategories = json.getJSONArray("categories");
		mCategories = new Category[jsonCategories.length()];
		for(int a=0; a<mCategories.length; a++) {
			mCategories[a] = new Category(jsonCategories.getJSONObject(a));
		}
	}
	
	public final Category[] getCategories() {
		return mCategories;
	}
}