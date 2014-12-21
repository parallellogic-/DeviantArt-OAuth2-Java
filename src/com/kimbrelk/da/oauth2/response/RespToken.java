package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.Scope;

public final class RespToken extends Response {
	/**
	 * The time in milliseconds the token expires
	 */
	private long mExpiresAt;
	private Scope[] mScopes;
	private String mToken;
	private String mTokenRefresh;
	
	public RespToken(int expiresIn, String token, String refreshToken, Scope... scopes) {
		super();
		mExpiresAt = System.currentTimeMillis() + (expiresIn * 1000) - 5000;
		mToken = token;
		mTokenRefresh = refreshToken;
		mScopes = scopes;
	}
	
	public final long getExpiration() {
		return mExpiresAt;
	}
	/**
	 * @return The refresh token
	 *  Will be null in the case of the CLIENT_CREDENTIALS grant
	 */
	public final String getRefreshToken() {
		return mTokenRefresh;
	}
	public final Scope[] getScopes() {
		return mScopes;
	}
	public final String getToken() {
		return mToken;
	}
	
	/**
	 * @return True if the token has expired
	 */
	public final boolean hasExpired() {
		return System.currentTimeMillis() < mExpiresAt;
	}
}