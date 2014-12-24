package com.kimbrelk.da.oauth2.struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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