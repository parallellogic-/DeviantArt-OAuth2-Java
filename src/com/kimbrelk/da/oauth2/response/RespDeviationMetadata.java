package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.DeviationMetadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespDeviationMetadata extends Response {
	private final DeviationMetadata[] mResults;
	
	public RespDeviationMetadata(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("metadata");
		mResults = new DeviationMetadata[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new DeviationMetadata(jsonResults.getJSONObject(a));
		}
	}
	
	public final DeviationMetadata[] getResults() {
		return mResults;
	}
}