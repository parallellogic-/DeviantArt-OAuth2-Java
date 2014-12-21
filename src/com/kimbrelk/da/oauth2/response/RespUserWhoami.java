package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.User;

public final class RespUserWhoami extends Response {
	private User mUser;
	
	public RespUserWhoami(User user) {
		super();
		mUser = user;
	}
	
	public final User getUser() {
		return mUser;
	}
}