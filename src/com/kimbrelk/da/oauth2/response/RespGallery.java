package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespGallery extends RespDeviationsQuery {
	private String mName;
	
	public RespGallery(JSONObject json) throws JSONException {
		super(json);
		if (json.has("name")) {
			mName = json.getString("name");
		}
	}
	
	public final String getName() {
		return mName;
	}
}