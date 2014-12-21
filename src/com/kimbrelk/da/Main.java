package com.kimbrelk.da;

import com.kimbrelk.da.oauth2.OAuth2;
import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.AuthGrantType;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespStashSpace;
import com.kimbrelk.da.oauth2.response.Response;
import java.security.InvalidParameterException;
import java.util.Scanner;

public final class Main {
	private final static String URI_REDIRECT = "http://127.0.0.1/";
	private final static AuthGrantType GRANT_TYPE = AuthGrantType.AUTHORIZATION_CODE;
	
	public final static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		OAuth2 oAuth2 = new OAuth2(new MyCredentials(), "Java OAuth2 Demo");
		Response resp;
		if (GRANT_TYPE == AuthGrantType.CLIENT_CREDENTIALS) {
			resp = oAuth2.requestAuthToken(AuthGrantType.CLIENT_CREDENTIALS, null, null);
		}
		else if (GRANT_TYPE == AuthGrantType.AUTHORIZATION_CODE) {
			System.out.println(oAuth2.getAuthorizeURL(URI_REDIRECT, Scope.values()));
			//System.out.println(oAuth2.getAuthorizeURL(URI_REDIRECT, Scope.STASH));
			resp = oAuth2.requestAuthToken(AuthGrantType.AUTHORIZATION_CODE, in.next(), URI_REDIRECT);
		}
		else {
			throw new InvalidParameterException("Unsupported grantType: " + GRANT_TYPE);
		}
		
		if (resp.isError()) {
			System.err.println(((RespError)resp));
		}
		else {
			oAuth2.requestUtilPlacebo();
			System.err.println(oAuth2.requestAuthRevoke());
			System.out.println(oAuth2.requestUtilPlacebo().getStatus());
			resp = oAuth2.requestStashSpace();
			if (resp.isError()) {
				System.err.println(((RespError)resp));
			}
			else {
				System.out.println(((RespStashSpace)resp).getPercentFree() * 100 + "%");
			}
		}
	}
}