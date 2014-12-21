package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Whois;

public final class RespUserWhois extends Response {
	private Whois[] mWhoisResults;
	
	public RespUserWhois(Whois[] whoisResults) {
		super();
		mWhoisResults = whoisResults;
	}
	
	public final Whois[] getWhoisResults() {
		return mWhoisResults;
	}
}