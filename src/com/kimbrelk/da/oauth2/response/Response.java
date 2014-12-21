package com.kimbrelk.da.oauth2.response;

public class Response {
	protected boolean mIsSuccess;
	
	public Response() {
		mIsSuccess = true;
	}
	public Response(boolean isSuccess) {
		mIsSuccess = isSuccess;
	}

	public final boolean isError() {
		return !mIsSuccess;
	}
	public final boolean isSuccess() {
		return mIsSuccess;
	}
}