package com.kimbrelk.da.oauth2.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashPublishUserdata extends Response {
	private String[] mAgreements;
	private String[] mFeatures;
	
	public RespStashPublishUserdata(String[] agreements, String[] features) {
		super(true);
		mAgreements = agreements;
		mFeatures = features;
	}
	public RespStashPublishUserdata(JSONObject json) throws JSONException {
		super(true);
		JSONArray jsonAgreements = json.getJSONArray("agreements");
		mAgreements = new String[jsonAgreements.length()];
		for(int a=0; a<mAgreements.length; a++) {
			mAgreements[a] = jsonAgreements.getString(a);
		}
		JSONArray jsonFeatures = json.getJSONArray("features");
		mFeatures = new String[jsonFeatures.length()];
		for(int a=0; a<mFeatures.length; a++) {
			mFeatures[a] = jsonFeatures.getString(a);
		}
	}
	
	public final String[] getAgreements() {
		return mAgreements;
	}
	public final String[] getFeatures() {
		return mFeatures;
	}
}