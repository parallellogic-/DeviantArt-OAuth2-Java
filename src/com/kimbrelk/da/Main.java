package com.kimbrelk.da;

import com.kimbrelk.da.oauth2.OAuth2;
import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.AuthGrantType;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespUserWhoami;
import com.kimbrelk.da.oauth2.response.Response;
import java.util.Scanner;

public final class Main {
	private final static String URI_REDIRECT = "http://127.0.0.1/";
	private final static AuthGrantType GRANT_TYPE = AuthGrantType.AUTHORIZATION_CODE;
	
	public final static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Response resp;
		
		// Create a new OAuth2 session, Replace 'new MyCredentials()' with a ClientCredentials object with your client's credentials
		OAuth2 oAuth2 = new OAuth2(new MyCredentials(), "Java OAuth2 Demo");
		if (GRANT_TYPE == AuthGrantType.CLIENT_CREDENTIALS) {
			// Authenticate using the CLIENT_CREDENTIALS grant (no user login)
			resp = oAuth2.requestAuthToken(AuthGrantType.CLIENT_CREDENTIALS, null, null);
		}
		else if (GRANT_TYPE == AuthGrantType.AUTHORIZATION_CODE) {
			// Give the user the URL to authorize the app with
			System.out.println(oAuth2.getAuthorizeURL(URI_REDIRECT, Scope.values()));
			
			// Get the token from the user inputed auth code
			System.out.println("Copy the above URL into a browser and paste the resulting code from the URL.");
			resp = oAuth2.requestAuthToken(AuthGrantType.AUTHORIZATION_CODE, in.next(), URI_REDIRECT);
		}
		else if (GRANT_TYPE == AuthGrantType.REFRESH_TOKEN) {
			System.out.println("Input your refresh token.");
			resp = oAuth2.requestAuthToken(AuthGrantType.REFRESH_TOKEN, in.next(), null);
		}
		else {
			resp = RespError.INVALID_GRANT;
		}
		
		if (resp.isError()) {
			System.err.println("Authentication Failed:");
			System.err.println(resp);
		}
		else {
			System.out.println("Your access token: " + oAuth2.getToken().getToken());
			System.out.println("Your refresh token: " + oAuth2.getToken().getRefreshToken());
			
			// You are now authenticated and you can now do your OAuth2 requests
			printUserName(oAuth2);
		}
		in.close();
	}
	
	private final static void printUserName(OAuth2 oAuth2) {
		Response resp;
		resp = oAuth2.requestUserWhoami();
		if (resp.isSuccess()) {
			System.out.println("Your username is " + ((RespUserWhoami)resp).getUser().getName() + ".");
		}
		else {
			System.out.println("Failed to get user info:");
			System.out.println(resp);
		}
	}
}