package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public class RespCollectionsFave extends Response {
	private int mFavourites;
	
	public RespCollectionsFave(JSONObject json) throws JSONException {
		super();
		mFavourites = json.getInt("favourites");
	}
	
	public final int getFavourites() {
		return mFavourites;
	}
}