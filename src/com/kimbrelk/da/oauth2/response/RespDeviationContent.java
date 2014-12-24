package com.kimbrelk.da.oauth2.response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespDeviationContent extends Response {
	private String mCSS;
	private String mFonts[];
	private String mHTML;
	
	public RespDeviationContent(JSONObject json) throws JSONException {
		super();
		if (json.has("css")) {
			mCSS = json.getString("css");
		}
		if (json.has("css_fonts")) {
			JSONArray jsonFonts = json.getJSONArray("css_fonts");
			mFonts = new String[jsonFonts.length()];
			for(int a=0; a<mFonts.length; a++) {
				mFonts[a] = jsonFonts.getString(a);
			}
		}
		if (json.has("html")) {
			mHTML = json.getString("html");
		}
	}
	
	public final String getCSS() {
		return mCSS;
	}
	public final String[] getFonts() {
		return mFonts;
	}
	public final String getHTML() {
		return mHTML;
	}
}