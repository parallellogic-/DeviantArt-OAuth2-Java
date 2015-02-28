package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Country;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespDataCountries extends RespPagination<Country> {
	public RespDataCountries(JSONObject json) throws JSONException {
		super(json, "results");
	}
	
	@Override
	protected final void getResultsFromJsonArray(JSONArray json) throws JSONException {
		mResults = new Country[json.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new Country(json.getJSONObject(a));
		}
	}
}