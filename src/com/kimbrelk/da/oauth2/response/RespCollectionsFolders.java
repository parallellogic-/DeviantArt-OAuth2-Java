package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Deviation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespCollectionsFolders extends Response {
	private Result[] mResults;
	
	public RespCollectionsFolders(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new Result[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Result(jsonResults.getJSONObject(a));
		}
	}
	
	public final Result[] getResults() {
		return mResults;
	}
	
	public final class Result {
		private Deviation[] mDeviations;
		private String mId;
		private String mName;
		private int mSize;
		
		public Result(JSONObject json) throws JSONException {
			if (json.has("deviations")) {
				JSONArray jsonDeviations = json.getJSONArray("deviations");
				mDeviations = new Deviation[jsonDeviations.length()];
				for(int a=0; a<mDeviations.length; a++) {
					mDeviations[a] = new Deviation(jsonDeviations.getJSONObject(a));
				}
			}
			mId = json.getString("folderid");
			mName = json.getString("name");
			mSize = -1;
			if (json.has("size")) {
				mSize = json.getInt("size");
			}
		}
		
		public final Deviation[] getDeviations() {
			return mDeviations;
		}
		public final String getId() {
			return mId;
		}
		public final String getName() {
			return mName;
		}
		public final int getSize() {
			return mSize;
		}
	}
}