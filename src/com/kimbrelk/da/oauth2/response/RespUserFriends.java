package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Friend;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespUserFriends extends RespPagination<Friend> {
	public RespUserFriends(JSONObject json) throws JSONException {
		super(json);
	}
	
	@Override
	protected final void getResultsFromJsonArray(JSONArray json) throws JSONException {
		mResults = new Friend[json.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Friend(json.getJSONObject(a));
		}
	}
}