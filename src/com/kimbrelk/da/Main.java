package com.kimbrelk.da;

import com.kimbrelk.da.oauth2.OAuth2;
import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.AuthGrantType;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespUserWhoami;
import com.kimbrelk.da.oauth2.response.RespUserWhois;
import com.kimbrelk.da.oauth2.response.Response;
import java.util.Scanner;

public final class Main {
	private final static String URI_REDIRECT = "http://127.0.0.1/";
	private final static AuthGrantType GRANT_TYPE = AuthGrantType.CLIENT_CREDENTIALS;
	
	public final static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Response resp;
		
		// Create a new OAuth2 session, Replace 'new MyCredentials()' with a ClientCredentials 
		// object with your client's credentials
		OAuth2 oAuth2 = new OAuth2(new MyCredentials(), "Java OAuth2 Demo");
		if (GRANT_TYPE == AuthGrantType.CLIENT_CREDENTIALS) {
			// Authenticate using the CLIENT_CREDENTIALS grant (no user login)
			resp = oAuth2.requestAuthToken(AuthGrantType.CLIENT_CREDENTIALS, null, null);
		}
		else if (GRANT_TYPE == AuthGrantType.AUTHORIZATION_CODE) {
			// Give the user the URL to authorize the app with
			System.out.println(oAuth2.getAuthorizeURL(URI_REDIRECT, Scope.values()));
			
			// Get the access token from the user inputed auth code
			System.out.println("Copy the above URL into a browser and paste the resulting code from the URL.");
			resp = oAuth2.requestAuthToken(AuthGrantType.AUTHORIZATION_CODE, in.next(), URI_REDIRECT);
		}
		else if (GRANT_TYPE == AuthGrantType.REFRESH_TOKEN) {
			// Get the access token from the user inputed access token
			System.out.println("Input your refresh token.");
			resp = oAuth2.requestAuthToken(AuthGrantType.REFRESH_TOKEN, in.next(), null);
		}
		else {
			resp = RespError.INVALID_GRANT;
		}
		System.out.println();
		
		if (resp.isError()) {
			System.err.println("Authentication Failed:");
			System.err.println(resp);
		}
		else {
			// You are now authenticated and you can now do your OAuth2 requests
			demoGetTokens(oAuth2);
			demoUserWhoami(oAuth2);
			demoUserWhois(oAuth2, "pickley");
			demoAuthRevoke(oAuth2);
		}
		
		in.close();
	}
	
	private final static void demoAuthRevoke(OAuth2 oAuth2) {
		// Revoke access token and force user reauthorization
		Response resp = oAuth2.requestAuthRevoke();
		if (resp.isSuccess()) {
			System.out.println("Your authorization has been revoked.");
		}
		else {
			System.out.println("Auth Revoke Failed:");
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoGetTokens(OAuth2 oAuth2) {
		System.out.println("Your access token:  " + oAuth2.getToken().getToken());
		System.out.println("Your refresh token: " + oAuth2.getToken().getRefreshToken());
		System.out.println();
	}
	private final static void demoUserWhoami(OAuth2 oAuth2) {
		Response resp;
		resp = oAuth2.requestUserWhoami();
		if (resp.isSuccess()) {
			System.out.println("Your username is " + ((RespUserWhoami)resp).getUser().getName() + ".");
		}
		else {
			System.out.println("Failed to get user info:");
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserWhois(OAuth2 oAuth2, String username) {
		Response resp;
		resp = oAuth2.requestUserWhois(username);
		if (resp.isSuccess()) {
			System.out.println(username + "'s real name is \'" + ((RespUserWhois)resp).getWhoisResults()[0].getRealName() + "\'.");
		}
		else {
			System.out.println("Failed to get user info:");
			System.out.println(resp);
		}
		System.out.println();
	}
}