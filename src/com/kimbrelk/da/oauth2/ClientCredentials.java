package com.kimbrelk.da.oauth2;

public class ClientCredentials {
	private int mClientId;
	private String mClientSecret;
	
	protected ClientCredentials(int clientId, String clientSecret) {
		mClientId = clientId;
		mClientSecret = clientSecret;
	}
	
	public final int getId() {
		return mClientId;
	}
	
	public final String getSecret() {
		return mClientSecret;
	}
}