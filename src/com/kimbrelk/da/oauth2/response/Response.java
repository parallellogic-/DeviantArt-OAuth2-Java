package com.kimbrelk.da.oauth2.response;

public class Response {
	protected String mStatus;
	
	public Response() {
		mStatus = "success";
	}
	public Response(String status) {
		mStatus = status;
	}
	
	public final String getStatus() {
		return mStatus;
	}
	
	public final boolean isError() {
		return mStatus.equalsIgnoreCase("error");
	}
}