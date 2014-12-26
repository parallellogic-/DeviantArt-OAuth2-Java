package com.kimbrelk.da.oauth2.response;

import org.json.JSONException;
import org.json.JSONObject;

public final class RespStashMedia extends Response {
	private String mCSS;
	private String mCSSFonts;
	private int mHeight;
	private String mHTML;
	private long mSize;
	private int mWidth;
	private String mURL;
	
	public RespStashMedia(JSONObject json) throws JSONException {
		if (json.has("css")) {
			mCSS = json.getString("css");
		}
		if (json.has("css_fonts")) {
			mCSSFonts = json.getString("css_fonts");
		}
		if (json.has("height")) {
			mHeight = json.getInt("height");
		}
		if (json.has("html")) {
			mHTML = json.getString("html");
		}
		if (json.has("size")) {
			mSize = json.getLong("size");
		}
		if (json.has("width")) {
			mWidth = json.getInt("width");
		}
		if (json.has("url")) {
			mURL = json.getString("url");
		}
	}

	public final String getCSS() {
		return mCSS;
	}
	public final String getCSSFonts() {
		return mCSSFonts;
	}
	public final int getHeight() {
		return mHeight;
	}
	public final String getHTML() {
		return mHTML;
	}
	public final long getSize() {
		return mSize;
	}
	public final int getWidth() {
		return mWidth;
	}
	public final String getURL() {
		return mURL;
	}
}