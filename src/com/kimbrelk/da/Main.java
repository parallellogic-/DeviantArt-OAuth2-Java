package com.kimbrelk.da;

import com.kimbrelk.da.oauth2.ClientCredentials;
import com.kimbrelk.da.oauth2.OAuth2;
import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.AuthGrantType;
import com.kimbrelk.da.oauth2.response.RespBrowseMorelikethisPreview;
import com.kimbrelk.da.oauth2.response.RespBrowseTagsSearch;
import com.kimbrelk.da.oauth2.response.RespCategory;
import com.kimbrelk.da.oauth2.response.RespDeviation;
import com.kimbrelk.da.oauth2.response.RespDeviationContent;
import com.kimbrelk.da.oauth2.response.RespDeviations;
import com.kimbrelk.da.oauth2.response.RespDeviationsQuery;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespFriends;
import com.kimbrelk.da.oauth2.response.RespGallery;
import com.kimbrelk.da.oauth2.response.RespGalleryFolders;
import com.kimbrelk.da.oauth2.response.RespStashPublishUserdata;
import com.kimbrelk.da.oauth2.response.RespStashSpace;
import com.kimbrelk.da.oauth2.response.RespUserDamntoken;
import com.kimbrelk.da.oauth2.response.RespUserFriends;
import com.kimbrelk.da.oauth2.response.RespUserFriendsWatching;
import com.kimbrelk.da.oauth2.response.RespUserStatus;
import com.kimbrelk.da.oauth2.response.RespUserStatuses;
import com.kimbrelk.da.oauth2.response.RespUserWatchers;
import com.kimbrelk.da.oauth2.response.RespUserWhoami;
import com.kimbrelk.da.oauth2.response.RespUserWhois;
import com.kimbrelk.da.oauth2.response.Response;
import com.kimbrelk.da.oauth2.struct.ArtistLevel;
import com.kimbrelk.da.oauth2.struct.ArtistSpeciality;
import com.kimbrelk.da.oauth2.struct.GalleryMode;
import com.kimbrelk.da.oauth2.struct.TimeRange;
import com.kimbrelk.da.oauth2.struct.Watch;

import java.util.Scanner;

@SuppressWarnings("unused")
public final class Main {
	private final static ClientCredentials CREDENTIALS = new MyCredentials();
	private final static AuthGrantType GRANT_TYPE = AuthGrantType.CLIENT_CREDENTIALS;
	private final static String URI_REDIRECT = "http://127.0.0.1/";
	
	public final static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		// Create a new OAuth2 session
		OAuth2 oAuth2 = new OAuth2(CREDENTIALS, "Java OAuth2 Demo");
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
			//demoBrowseCategorytree(oAuth2);
			//demoBrowseDailydeviations(oAuth2);
			//demoBrowseHot(oAuth2);
			//demoBrowseMorelikethis(oAuth2);
			//demoBrowseMorelikethisPreview(oAuth2);
			//demoBrowseNewest(oAuth2);
			//demoBrowsePopular(oAuth2);
			//demoBrowseTags(oAuth2);
			//demoBrowseTagsSearch(oAuth2);
			//demoBrowseUndiscovered(oAuth2);
			//demoBrowseUserJournals(oAuth2);
			
			// Collections Demos
			// TODO
			
			// Comment Demos
			// TODO
			
			// Curated Demos
			// TODO
			
			// Deviation Demos
			// TODO
			//demoDeviation(oAuth2);
			//demoDeviationContent(oAuth2);
			
			// Feed Demos
			// TODO
			
			// Gallery Demos
			//demoGallery(oAuth2);
			//demoGalleryFolder(oAuth2);
			
			// Sta.sh Demos
			// TODO
			//demoStashPublishCategorytree(oAuth2);
			//demoStashPublishUserdata(oAuth2);
			//demoStashSpace(oAuth2);
			
			// User Demos
			// TODO
			//demoUserDamntoken(oAuth2);
			demoUserFriends(oAuth2);
			demoUserFriendsSearch(oAuth2);
			//demoUserFriendsUnwatch(oAuth2);
			//demoUserFriendsWatch(oAuth2);
			demoUserFriendsWatching(oAuth2);
			//demoUserProfileUpdate(oAuth2);
			demoUserStatus(oAuth2);
			//demoUserStatusPost(oAuth2);
			demoUserStatuses(oAuth2);
			demoUserWatchers(oAuth2);
			//demoUserWhoami(oAuth2);
			//demoUserWhois(oAuth2);
			
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
	
	private final static void demoBrowseCategorytree(OAuth2 oAuth2) {
		System.out.println("demoBrowseCategorytree()");
		Response resp = oAuth2.requestBrowseCategorytree("/flash/");
		if (resp.isSuccess()) {
			System.out.println("First category in \'/flash/\': \"" + 
				((RespCategory)resp).getCategories()[0].getTitle() + "\"");
		}
		else {
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
				((RespDeviationsQuery)resp).getResults()[0].getTitle() + "\"");
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
				((RespDeviationsQuery)resp).getResults()[0].getTitle() + "\"");
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
				((RespDeviationsQuery)resp).getResults()[0].getTitle() + "\"");
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
				((RespDeviationsQuery)resp).getResults()[0].getTitle() + "\"");
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
				((RespDeviationsQuery)resp).getResults()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoBrowseTagsSearch(OAuth2 oAuth2) {
		System.out.println("demoBrowseTagsSearch()");
		Response resp = oAuth2.requestBrowseTagsSearch("anim");
		if (resp.isSuccess()) {
			System.out.println("First tag from search of \'anim\': \"" + 
				((RespBrowseTagsSearch)resp).getTags()[0] + "\"");
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
				((RespDeviationsQuery)resp).getResults()[0].getTitle() + "\"");
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
				((RespDeviationsQuery)resp).getResults()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	
	private final static void demoDeviation(OAuth2 oAuth2) {
		System.out.println("demoDeviation()");
		Response resp = oAuth2.requestDeviation("AA4C62ED-1020-3DDA-66BE-C3DD17C52CA2");
		if (resp.isSuccess()) {
			System.out.println("Deviation title: \"" + 
				((RespDeviation)resp).getDeviation().getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoDeviationContent(OAuth2 oAuth2) {
		System.out.println("demoDeviationContent()");
		Response resp = oAuth2.requestDeviationContent("A2584B72-8F09-CC77-1950-A7558EFA73FA");
		if (resp.isSuccess()) {
			System.out.println("Deviation html: \"" + 
				((RespDeviationContent)resp).getHTML() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}

	private final static void demoGallery(OAuth2 oAuth2) {
		System.out.println("demoGallery()");
		Response resp = oAuth2.requestGallery("baronbeandip", "181CADE2-DB26-A091-0118-6516A45BCF3C", GalleryMode.NEWEST);
		if (resp.isSuccess()) {
			System.out.println("Name of first deviation in baronbeandip's gallery folder \'Featured\': \"" + 
				((RespGallery)resp).getResults()[0].getTitle() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoGalleryFolder(OAuth2 oAuth2) {
		System.out.println("demoGalleryFolder()");
		Response resp = oAuth2.requestGalleryFolders("baronbeandip");
		if (resp.isSuccess()) {
			System.out.println("baronbeandip's first gallery folder name and id: \"" + 
				((RespGalleryFolders)resp).getFolders()[0].getName() + " : " + ((RespGalleryFolders)resp).getFolders()[0].getId() + "\"");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	
	private final static void demoStashPublishCategorytree(OAuth2 oAuth2) {
		System.out.println("demoStashPublishCategorytree()");
		Response resp = oAuth2.requestStashPublishCategorytree("/", "png");
		if (resp.isSuccess()) {
			System.out.println("First \'png\' accepting category in \'/\': \"" + 
				((RespCategory)resp).getCategories()[0].getTitle() + "\"");
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
	private final static void demoUserFriends(OAuth2 oAuth2) {
		System.out.println("demoUserFriends()");
		Response resp;
		resp = oAuth2.requestUserFriends("baronbeandip");
		if (resp.isSuccess()) {
			System.out.println("baronbeandip's first friend's name: \"" + ((RespUserFriends)resp).getResults()[0].getUser().getName() + "\".");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserFriendsSearch(OAuth2 oAuth2) {
		System.out.println("demoUserFriendsSearch()");
		Response resp;
		resp = oAuth2.requestUserFriendsSearch("baronbeandip", "pic");
		if (resp.isSuccess()) {
			System.out.println("Name of baronbeandip's first friend close to \'pic\': \"" + ((RespFriends)resp).getFriends()[0].getName() + "\".");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserFriendsUnwatch(OAuth2 oAuth2) {
		System.out.println("demoUserFriendsUnwatch()");
		Response resp;
		resp = oAuth2.requestUserFriendsUnwatch("Pickley");
		if (resp.isSuccess()) {
			System.out.println("You have unwatched Pickley.");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserFriendsWatch(OAuth2 oAuth2) {
		System.out.println("demoUserFriendsWatch()");
		Response resp;
		resp = oAuth2.requestUserFriendsWatch("Pickley", new Watch(true, true, true, true, true, true, true, true));
		if (resp.isSuccess()) {
			System.out.println("Successfully watched Pickley.");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserFriendsWatching(OAuth2 oAuth2) {
		System.out.println("demoUserFriendsWatching()");
		Response resp;
		resp = oAuth2.requestUserFriendsWatching("Pickley");
		if (resp.isSuccess()) {
			if (((RespUserFriendsWatching)resp).isWatching()) {
				System.out.println("You are watching Pickley.");
			}
			else {
				System.out.println("You are not watching Pickley.");
			}
		}
		else {
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
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserStatus(OAuth2 oAuth2) {
		System.out.println("demoUserStatus()");
		Response resp;
		resp = oAuth2.requestUserStatus("DEFFEA2D-369C-5396-4DA9-92B2B2C6A337");
		if (resp.isSuccess()) {
			System.out.println("Status text: \"" + ((RespUserStatus)resp).getStatus().getBody() + "\".");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserStatusPost(OAuth2 oAuth2) {
		System.out.println("demoUserStatusPost()");
		Response resp;
		resp = oAuth2.requestUserStatusPost("I am bread.");
		if (resp.isSuccess()) {
			System.out.println("Successfully posted status.");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserStatuses(OAuth2 oAuth2) {
		System.out.println("demoUserStatuses()");
		Response resp;
		resp = oAuth2.requestUserStatuses("baronbeandip");
		if (resp.isSuccess()) {
			System.out.println("First status of baronbeandip: \"" + ((RespUserStatuses)resp).getStatuses()[0].getBody() + "\".");
		}
		else {
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserWatchers(OAuth2 oAuth2) {
		System.out.println("demoUserWatchers()");
		Response resp;
		resp = oAuth2.requestUserWatchers("baronbeandip");
		if (resp.isSuccess()) {
			System.out.println("baronbeandip's first watcher's name: \"" + ((RespUserWatchers)resp).getResults()[0].getUser().getName() + "\".");
		}
		else {
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
			System.out.println(resp);
		}
		System.out.println();
	}
	private final static void demoUserWhois(OAuth2 oAuth2) {
		System.out.println("demoUserWhois()");
		Response resp;
		String username = "baronbeandip";
		resp = oAuth2.requestUserWhois(username);
		if (resp.isSuccess()) {
			System.out.println(username + "'s real name is \'" + ((RespUserWhois)resp).getWhoisResults()[0].getRealName() + "\'.");
		}
		else {
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