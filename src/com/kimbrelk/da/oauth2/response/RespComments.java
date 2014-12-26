package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Comment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespComments extends RespPaginationOffsetFull<Comment> {
	public RespComments(JSONObject json) throws JSONException {
		super(json, "thread");
	}
	
	@Override
	protected final void getResultsFromJsonArray(JSONArray json) throws JSONException {
		mResults = new Comment[json.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Comment(json.getJSONObject(a));
		}
	}
}