package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.Token;

public final class RespToken extends Response {
	/**
	 * The time in milliseconds the token expires
	 */
	private long mExpiresAt;
	private Scope[] mScopes;
	private Token mToken;
	private Token mTokenRefresh;
	
	public RespToken(int expiresIn, Token token, Token refreshToken, Scope... scopes) {
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
	public final Token getRefreshToken() {
		return mTokenRefresh;
	}
	public final Scope[] getScopes() {
		return mScopes;
	}
	public final Token getToken() {
		return mToken;
	}
	
	/**
	 * @return True if the token has expired
	 */
	public final boolean hasExpired() {
		return System.currentTimeMillis() < mExpiresAt;
	}
}