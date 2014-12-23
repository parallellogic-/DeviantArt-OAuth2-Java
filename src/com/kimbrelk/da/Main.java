package com.kimbrelk.da;

import com.kimbrelk.da.oauth2.OAuth2;
import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.AuthGrantType;
import com.kimbrelk.da.oauth2.response.RespBrowseMorelikethisPreview;
import com.kimbrelk.da.oauth2.response.RespDeviations;
import com.kimbrelk.da.oauth2.response.RespDeviationsQuery;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespStashPublishUserdata;
import com.kimbrelk.da.oauth2.response.RespStashSpace;
import com.kimbrelk.da.oauth2.response.RespUserDamntoken;
import com.kimbrelk.da.oauth2.response.RespUserWhoami;
import com.kimbrelk.da.oauth2.response.RespUserWhois;
import com.kimbrelk.da.oauth2.response.Response;
import com.kimbrelk.da.oauth2.struct.ArtistLevel;
import com.kimbrelk.da.oauth2.struct.ArtistSpeciality;
import com.kimbrelk.da.oauth2.struct.TimeRange;
import java.util.Scanner;

public final class Main {
	private final static String URI_REDIRECT = "http://127.0.0.1/";
	private final static AuthGrantType GRANT_TYPE = AuthGrantType.CLIENT_CREDENTIALS;
	
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
			//demoAuthRevoke(oAuth2);	// /revoke is broken on dA's side at this time.
			
			// Browse Demos
			// TODO
			//demoBrowseDailydeviations(oAuth2);
			//demoBrowseHot(oAuth2);
			//demoBrowseMorelikethis(oAuth2);
			//demoBrowseMorelikethisPreview(oAuth2);
			//demoBrowseNewest(oAuth2);
			//demoBrowsePopular(oAuth2);
			//demoBrowseTags(oAuth2);
			//demoBrowseUndiscovered(oAuth2);
			demoBrowseUserJournals(oAuth2);
			
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
			//demoStashPublishUserdata(oAuth2);
			//demoStashSpace(oAuth2);
			
			// User Demos
			// TODO
			//demoUserDamntoken(oAuth2);
			//demoUserProfileUpdate(oAuth2);
			//demoUserWhoami(oAuth2);
			//demoUserWhois(oAuth2, "baronbeandip");
			
			// Util Demos
			//demoUtilPlacebo(oAuth2);
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
	
	private final static void demoBrowseDailydeviations(OAuth2 oAuth2) {
		System.out.println("demoBrowseDailydeviations()");
		Response resp = oAuth2.requestBrowseDailydeviations("2001-12-25");
		if (resp.isSuccess()) {
			System.out.println("Title of first DD on 2001-12-25: \"" + 
				((RespDeviations)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseHot(OAuth2 oAuth2) {
		System.out.println("demoBrowseHot()");
		Response resp = oAuth2.requestBrowseHot();
		if (resp.isSuccess()) {
			System.out.println("Title of first \'hot\' deviation: \"" + 
				((RespDeviationsQuery)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseMorelikethis(OAuth2 oAuth2) {
		System.out.println("demoBrowseMorelikethis()");
		Response resp = oAuth2.requestBrowseMorelikethis("AA4C62ED-1020-3DDA-66BE-C3DD17C52CA2");
		if (resp.isSuccess()) {
			System.out.println("First deviation that is like \'Hair Step by step Winter edition tutorial\': \"" + 
				((RespDeviationsQuery)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseMorelikethisPreview(OAuth2 oAuth2) {
		System.out.println("demoBrowseMorelikethisPreview()");
		Response resp = oAuth2.requestBrowseMorelikethisPreview("AA4C62ED-1020-3DDA-66BE-C3DD17C52CA2");
		if (resp.isSuccess()) {
			System.out.println("First deviation by different user similar to \'Hair Step by step Winter edition tutorial\': \"" + 
				((RespBrowseMorelikethisPreview)resp).getMoreFromDeviantArt()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseNewest(OAuth2 oAuth2) {
		System.out.println("demoBrowseNewest()");
		Response resp = oAuth2.requestBrowseNewest(null, "java");
		if (resp.isSuccess()) {
			System.out.println("Title of newest \'java\' deviation: \"" + 
				((RespDeviationsQuery)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowsePopular(OAuth2 oAuth2) {
		System.out.println("demoBrowsePopular()");
		Response resp = oAuth2.requestBrowsePopular(null, "java", -1, -1, TimeRange.ALLTIME);
		if (resp.isSuccess()) {
			System.out.println("Title most popular \'java\' deviation of all time: \"" + 
				((RespDeviationsQuery)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseTags(OAuth2 oAuth2) {
		System.out.println("demoBrowseTags()");
		Response resp = oAuth2.requestBrowseTags("android");
		if (resp.isSuccess()) {
			System.out.println("Title of first \'android\' tagged deviation: \"" + 
				((RespDeviationsQuery)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseUndiscovered(OAuth2 oAuth2) {
		System.out.println("demoBrowseUndiscovered()");
		Response resp = oAuth2.requestBrowseUndiscovered();
		if (resp.isSuccess()) {
			System.out.println("Title of first undiscovered deviation: \"" + 
				((RespDeviationsQuery)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseUserJournals(OAuth2 oAuth2) {
		System.out.println("demoBrowseUserJournals()");
		Response resp = oAuth2.requestBrowseUserJournals("baronbeandip");
		if (resp.isSuccess()) {
			System.out.println("Title of baronbeandip's newest featured journal: \"" + 
				((RespDeviationsQuery)resp).getDeviations()[0].getTitle() + "\"");
		}
		else {
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
				ArtistSpeciality.DESIGN_INTERFACES, null, null, -1, 
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