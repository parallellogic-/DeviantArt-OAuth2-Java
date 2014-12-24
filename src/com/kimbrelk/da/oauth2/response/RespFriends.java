package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespFriends extends Response {
	private User[] mResults;
	
	public RespFriends(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new User[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new User(jsonResults.getJSONObject(a));
		}
	}
	
	public final User[] getFriends() {
		return mResults;
	}
}