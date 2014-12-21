package com.kimbrelk.da.oauth2;

public final class Token {
	
	public enum Type {
		AUTHENTICATION,
		CLIENT_CREDENTIALS,
		REFRESH
	}
	
	private String mToken;
	private Type mType;
	
	public Token() {
		
	}
	
	public Token(Type type, String token) {
		mType = type;
		mToken = token;
	}
	
	public final String getToken() {
		return mToken;
	}
	
	public final Type getType() {
		return mType;
	}
	
	public final void setToken(String token) {
		mToken = token;
	}
	
	public final void setType(Type type) {
		mType = type;
	}
}