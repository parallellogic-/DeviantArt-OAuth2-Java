package com.kimbrelk.da.oauth2.response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public final class RespError extends Response {
	public final static RespError INSUFFICIANT_SCOPE = new RespError("insufficiant_scope", "The requested resource requires a higher scope than the user has allowed the client to access, client needs to re-authorize.");
	public final static RespError INVALID_GRANT = new RespError("invalid_grant", "Unsupported grantType.");
	public final static RespError INVALID_REQUEST = new RespError("invalid_request", "Request field validation failed.");
	public final static RespError INVALID_TOKEN = new RespError("invalid_request", "Expired oAuth2 user token. The client should request a new one with an access code or a refresh token.");
	public final static RespError NO_AUTH = new RespError("no_auth", "The client does not have an access_token.");
	public final static RespError RATE_LIMIT = new RespError("rate_limit", "Rate limit reached or service overloaded.");
	public final static RespError REQUEST_FAILED = new RespError("request_failed", "The client failed to make the request.");
	
	protected String mDesc;
	protected String mType;
	protected int mErrorCode;
	protected Map<String, String> mErrorDetails;

	public RespError(String type, String description) {
		super(false);
		mType = type;
		mDesc = description;
		mErrorCode = -1;
		mErrorDetails = new HashMap<String, String>();
	}
	public RespError(JSONObject json) throws JSONException {
		super(false);
		mType = json.getString("error");
		mDesc = json.getString("error_description");
		mErrorCode = -1;
		if (json.has("error_code")) {
			mErrorCode = json.getInt("error_code");
		}
		mErrorDetails = new HashMap<String, String>();
		if (json.has("error_details")) {
			JSONObject jsonDetails = json.getJSONObject("error_details");
			String[] keys = JSONObject.getNames(jsonDetails);
			for(int a=0; a<keys.length; a++) {
				mErrorDetails.put(keys[a], jsonDetails.getString(keys[a]));
			}
		}
	}
	
	public boolean equals(RespError error) {
		return error.getType().equals(mType);
	}
	
	public final String getDescription() {
		return mDesc;
	}
	public final String getType() {
		return mType;
	}
	
	public final String toString() {
		String ret = getType() + " : " + getDescription();
		if (mErrorCode != -1) {
			ret += "\n Error Code: " + mErrorCode;
		}
		if (mErrorDetails.size() != 0) {
			Set<String> keys = mErrorDetails.keySet();
			for(String key: keys) {
				ret += "\n " + key + " : " + mErrorDetails.get(key);
			}
		}
		return ret;
	}
}