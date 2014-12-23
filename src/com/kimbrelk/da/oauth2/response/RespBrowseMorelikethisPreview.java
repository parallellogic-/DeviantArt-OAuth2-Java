package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Deviation;
import com.kimbrelk.da.oauth2.struct.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespBrowseMorelikethisPreview extends Response {
	public final static int ERROR_INVALID_SEED = 0;
	
	private User mAuthor;
	private Deviation[] mMoreFromArtist;
	private Deviation[] mMoreFromDA;
	private String mSeedDeviation;
	
	public RespBrowseMorelikethisPreview(JSONObject json) throws JSONException {
		super();
		mAuthor = new User(json.getJSONObject("author"));
		JSONArray jsonMoreArtist = json.getJSONArray("more_from_artist");
		mMoreFromArtist = new Deviation[jsonMoreArtist.length()];
		for(int a=0; a<mMoreFromArtist.length; a++) {
			mMoreFromArtist[a] = new Deviation(jsonMoreArtist.getJSONObject(a));
		}
		JSONArray jsonMoreDA = json.getJSONArray("more_from_da");
		mMoreFromDA = new Deviation[jsonMoreDA.length()];
		for(int a=0; a<mMoreFromDA.length; a++) {
			mMoreFromDA[a] = new Deviation(jsonMoreDA.getJSONObject(a));
		}
		mSeedDeviation = json.getString("seed");
	}

	public final User getAuthor() {
		return mAuthor;
	}
	public final Deviation[] getMoreFromAuthor() {
		return mMoreFromArtist;
	}
	public final Deviation[] getMoreFromDeviantArt() {
		return mMoreFromDA;
	}
	public final String getSeedUUID() {
		return mSeedDeviation;
	}
}