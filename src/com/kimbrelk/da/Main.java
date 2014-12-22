package com.kimbrelk.da;

import com.kimbrelk.da.oauth2.OAuth2;
import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.AuthGrantType;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespStashPublishUserdata;
import com.kimbrelk.da.oauth2.response.RespStashSpace;
import com.kimbrelk.da.oauth2.response.RespUserDamntoken;
import com.kimbrelk.da.oauth2.response.RespUserWhoami;
import com.kimbrelk.da.oauth2.response.RespUserWhois;
import com.kimbrelk.da.oauth2.response.Response;
import com.kimbrelk.da.oauth2.struct.ArtistLevel;
import com.kimbrelk.da.oauth2.struct.ArtistSpecialty;
import java.util.Scanner;

public final class Main {
	private final static String URI_REDIRECT = "http://127.0.0.1/";
	private final static AuthGrantType GRANT_TYPE = AuthGrantType.AUTHORIZATION_CODE;
	
	public final static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		// Create a new OAuth2 session, Replace 'new MyCredentials()' with a ClientCredentials 
		// object with your client's credentials
		OAuth2 oAuth2 = new OAuth2(new MyCredentials(), "Java OAuth2 Demo");
		Response resp;
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
			
			// Auth Demos
			demoAuthGetTokens(oAuth2);
			//demoAuthRevoke(oAuth2);
			
			// Browse Demos
			// TODO
			
			// Collections Demos
			// TODO
			
			// Comment Demos
			// TODO
			
			// Curated Demos
			// TODO
			
			// Deviation Demos
			// TODO
			
			// Feed Demos
			// TODO
			
			// Gallery Demos
			// TODO
			
			// Sta.sh Demos
			// TODO
			demoStashPublishUserdata(oAuth2);
			demoStashSpace(oAuth2);
			
			// User Demos
			// TODO
			demoUserDamntoken(oAuth2);
			//demoUserProfileUpdate(oAuth2);
			demoUserWhoami(oAuth2);
			demoUserWhois(oAuth2, "pickley");
			
			// Util Demos
			demoUtilPlacebo(oAuth2);
		}
		
		in.close();
	}
	
	private final static void demoAuthGetTokens(OAuth2 oAuth2) {
		System.out.println("demoAuthGetTokens()");
		System.out.println("Your access token:  " + oAuth2.getToken().getToken());
		System.out.println("Your refresh token: " + oAuth2.getToken().getRefreshToken());
		System.out.println();
	}
	private final static void demoAuthRevoke(OAuth2 oAuth2) {
		// Revoke access token and force user reauthorization
		System.out.println("demoAuthRevoke()");
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
	
	private final static void demoStashPublishUserdata(OAuth2 oAuth2) {
		System.out.println("demoStashPublishUserdata()");
		Response resp;
		resp = oAuth2.requestStashPublishUserdata();
		if (resp.isSuccess()) {
			System.out.println("Agreement[0]: " + ((RespStashPublishUserdata)resp).getAgreements()[0]);
		}
		else {
			System.out.println("Failed to get publishing user data.");
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoStashSpace(OAuth2 oAuth2) {
		System.out.println("demoStashSpace()");
		Response resp;
		resp = oAuth2.requestStashSpace();
		if (resp.isSuccess()) {
			System.out.println("Your Sta.sh is " + (int)((RespStashSpace)resp).getPercentUsed() + "% full.");
		}
		else {
			System.out.println("Failed to get your sta.sh space info:");
			System.out.println(resp);
		}
		System.out.println();
	}

	private final static void demoUserDamntoken(OAuth2 oAuth2) {
		System.out.println("demoUserDamntoken()");
		Response resp;
		resp = oAuth2.requestUserDamntoken();
		if (resp.isSuccess()) {
			System.out.println("Your dAmn token is " + ((RespUserDamntoken)resp).getDamnToken() + ".");
		}
		else {
			System.out.println("Failed to get your dAmn token:");
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserProfileUpdate(OAuth2 oAuth2) {
		System.out.println("demoUserProfileUpdate()");
		Response resp;
		resp = oAuth2.requestUserProfileUpdate(1, ArtistLevel.STUDENT, 
				ArtistSpecialty.DESIGN_INTERFACES, null, null, -1, 
				null, null);
		if (resp.isSuccess()) {
			System.out.println("Profile successfully updated!");
		}
		else {
			System.out.println("Failed to get your user info:");
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserWhoami(OAuth2 oAuth2) {
		System.out.println("demoUserWhoami()");
		Response resp;
		resp = oAuth2.requestUserWhoami();
		if (resp.isSuccess()) {
			System.out.println("Your username is " + ((RespUserWhoami)resp).getUser().getName() + ".");
		}
		else {
			System.out.println("Failed to get your user info:");
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserWhois(OAuth2 oAuth2, String username) {
		System.out.println("demoUserWhois()");
		Response resp;
		resp = oAuth2.requestUserWhois(username);
		if (resp.isSuccess()) {
			System.out.println(username + "'s real name is \'" + ((RespUserWhois)resp).getWhoisResults()[0].getRealName() + "\'.");
		}
		else {
			System.out.println("Failed to get " + username + "\'s user info:");
			System.out.println(resp);
		}
		System.out.println();
	}
	
	private final static void demoUtilPlacebo(OAuth2 oAuth2) {
		System.out.println("demoUtilPlacebo()");
		Response resp;
		resp = oAuth2.requestUtilPlacebo();
		if (resp.isSuccess()) {
			System.out.println("Your auth is valid.");
		}
		else {
			System.out.println("Your auth is invalid:");
			System.out.println(resp);
		}
		System.out.println();
	}
}