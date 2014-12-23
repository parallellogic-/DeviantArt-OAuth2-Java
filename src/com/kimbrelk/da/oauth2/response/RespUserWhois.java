package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Whois;

public final class RespUserWhois extends Response {
	public final static int ERROR_TOO_MANY_USERS = 0;
	public final static int ERROR_UNKNOWN_USER = 1;
	
	private Whois[] mWhoisResults;
	
	public RespUserWhois(Whois[] whoisResults) {
		super();
		mWhoisResults = whoisResults;
	}
	
	public final Whois[] getWhoisResults() {
		return mWhoisResults;
	}
}