package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Comment;
import org.json.JSONException;
import org.json.JSONObject;

public class RespComment extends Response {
	private Comment mResult;
	
	public RespComment(JSONObject json) throws JSONException {
		super();
		mResult = new Comment(json);
	}
	
	public final Comment getComment() {
		return mResult;
	}
}