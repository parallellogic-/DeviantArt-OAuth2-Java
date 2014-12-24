package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Deviation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespCuratedTags extends Response {
	private Tag[] mResults;
	
	public RespCuratedTags(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new Tag[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Tag(jsonResults.getJSONObject(a));
		}
	}
	
	public final Tag[] getResults() {
		return mResults;
	}
	
	public final class Tag {
		private boolean mIsSponsored;
		private String mName;
		private Deviation[] mDeviations;
		private String mSponsor;
		
		public Tag(JSONObject json) throws JSONException {
			mIsSponsored = json.getBoolean("sponsored");
			mName = json.getString("tag_name");
			if (json.has("deviations")) {
				JSONArray jsonDeviations = json.getJSONArray("deviations");
				mDeviations = new Deviation[jsonDeviations.length()];
				for(int a=0; a<mDeviations.length; a++) {
					mDeviations[a] = new Deviation(jsonDeviations.getJSONObject(a));
				}
			}
			mSponsor = json.getString("sponsor");
		}

		public final boolean isSponsored() {
			return mIsSponsored;
		}
		
		public final String getName() {
			return mName;
		}
		public final Deviation[] getDeviations() {
			return mDeviations;
		}
		public final String getSponsor() {
			return mSponsor;
		}
	}
}