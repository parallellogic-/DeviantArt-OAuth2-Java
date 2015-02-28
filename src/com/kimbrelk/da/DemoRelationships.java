package com.kimbrelk.da;

import com.kimbrelk.da.oauth2.AuthGrantType;
import com.kimbrelk.da.oauth2.ClientCredentials;
import com.kimbrelk.da.oauth2.OAuth2;
import com.kimbrelk.da.oauth2.Scope;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespUserFriends;
import com.kimbrelk.da.oauth2.response.RespUserWatchers;
import com.kimbrelk.da.oauth2.response.Response;
import com.kimbrelk.da.oauth2.struct.Friend;
import com.kimbrelk.da.oauth2.struct.Watcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public final class DemoRelationships extends Thread {
	private final static ClientCredentials CREDENTIALS = new MyCredentials();
	private final static AuthGrantType GRANT_TYPE = AuthGrantType.CLIENT_CREDENTIALS;
	private final static String URI_REDIRECT = "http://127.0.0.1/";
	
	// Boilerplate
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
		
		if (resp.isError()) {
			System.err.println("Authentication Failed:");
			System.err.println(resp);
		}
		else {
			// You are now authenticated and you can now do your OAuth2 requests
			new DemoRelationships(oAuth2).start();
		}
		
		in.close();
	}
	
	private final static int MIN_WAIT_TIME = 100;	// 100ms
	private int mWaitTime;
	private OAuth2 mOAuth;
	public DemoRelationships(OAuth2 oAuth) {
		super();
		mWaitTime = 1;
		mOAuth = oAuth;
	}
	
	public void run() {
		try {
			long timeStart = System.currentTimeMillis();
			HashMap<String, User> map = new HashMap<String, User>();
			getRelationships("pickley", 1, map);
			printRelationships(map);
			long timeEnd = System.currentTimeMillis();
			System.out.println("Done in " + ((timeEnd - timeStart) / 1000.0) + " seconds.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final static void printRelationships(HashMap<String, User> nodes) {
		//HashMap<String, Boolean> proccessedMap = new HashMap<String, Boolean>();
		int len = nodes.size();
		String[] keys = new String[len];
		nodes.keySet().toArray(keys);
		System.out.println("graph g {");
		for(int a=0; a<len; a++) {
			User user = nodes.get(keys[a]);
			if (user != null) {
				int len2;
				/*
				len2 = user.getWatchList().size();
				for(int b=0; b<len2; b++) {
					String user2 = user.getWatchList().get(b);
					System.out.println("    \"" + user.getName().replace('-', '_') + "\" -> \"" + user2.replace('-', '_') + "\";");
				}
				len2 = user.getWatchedByList().size();
				for(int b=0; b<len2; b++) {
					String user2 = user.getWatchedByList().get(b);
					System.out.println("    \"" + user2.replace('-', '_') + "\" -> \"" + user.getName().replace('-', '_') + "\";");
				}
				*/
				len2 = user.getWatchedByList().size();
				for(int b=0; b<len2; b++) {
					String user2 = user.getWatchedByList().get(b);
					if (user.getWatchList().contains(user2)) {
						System.out.println("    \"" + user.getName().replace('-', '_') + "\" -- \"" + user2.replace('-', '_') + "\";");
					}
				}
			}
		}
		System.out.println("}");
	}
	
	private final void getRelationships(String userName, int degrees, HashMap<String, User> map) throws Exception {
		PriorityQueue<QueueItem> queue = new PriorityQueue<QueueItem>();
		queue.add(new QueueItem(userName, 0));
		while(queue.size() > 0) {
			QueueItem item = queue.poll();
			if (item.degree <= degrees - 1) {
				if (!map.containsKey(item.name.toLowerCase())) {
					User user = new User(userName);
					int nextOffset = 0;
					while (nextOffset != -1) {
						Response resp = mOAuth.requestUserFriends(userName, null, nextOffset, 50);
						if (!resp.isError()) {
							RespUserFriends respWatching = (RespUserFriends)resp;
							nextOffset = respWatching.getNextOffset();
							Friend[] watching = respWatching.getResults();
							for(Friend watch: watching) {
								if (user.addWatchedUser(watch.getUser().getName(), map)) {
									queue.add(new QueueItem(watch.getUser().getName(), item.degree + 1));
								}
							}
							decreaseWaitTime();
						}
						else {
							if (((RespError)resp).equals(RespError.RATE_LIMIT)) {
								increaseWaitTime();
							}
							else {
								throw new Exception(resp.toString());
							}
						}
						sleep(getWaitTime());
					}
					nextOffset = 0;
					while (nextOffset != -1) {
						Response resp = mOAuth.requestUserWatchers(userName, null, nextOffset, 50);
						if (!resp.isError()) {
							RespUserWatchers respWatchers = (RespUserWatchers)resp;
							nextOffset = respWatchers.getNextOffset();
							Watcher[] watchers = respWatchers.getResults();
							for(Watcher watch: watchers) {
								if (user.addWatcher(watch.getUser().getName(), map)) {
									queue.add(new QueueItem(watch.getUser().getName(), item.degree + 1));
								}
							}
							decreaseWaitTime();
						}
						else {
							if (((RespError)resp).equals(RespError.RATE_LIMIT)) {
								increaseWaitTime();
							}
							else {
								throw new Exception(resp.toString());
							}
						}
						sleep(getWaitTime());
					}
					map.put(userName.toLowerCase(), user);
				}
			}
		}
	}

	public final void decreaseWaitTime() {
		mWaitTime /= 2;
		if (mWaitTime < MIN_WAIT_TIME) {
			mWaitTime = MIN_WAIT_TIME;
		}
		else {
			//System.out.println("Speeding up... (" + mWaitTime + "ms)");
		}
	}
	public final void increaseWaitTime() {
		mWaitTime *= 2;
		//System.out.println("Slowing down... (" + mWaitTime + "ms)");
	}
	public final int getWaitTime() {
		return mWaitTime;
	}
	
	private final class User {
		private String mName;
		private ArrayList<String> mWatching;
		private ArrayList<String> mWatchedBy;
		
		public User(String name) {
			mName = name.toLowerCase();
			mWatching = new ArrayList<String>();
			mWatchedBy = new ArrayList<String>();
		}
		
		public final boolean addWatchedUser(String userName, HashMap<String, User> map) {
			if (!mWatching.contains(userName.toLowerCase())) {
				mWatching.add(userName.toLowerCase());
				User watchedUser = map.get(userName.toLowerCase());
				if (watchedUser != null) {
					watchedUser.addWatcher(userName.toLowerCase(), map);
					return false;
				}
				else {
					return true;
				}
			}
			return false;
		}
		public final boolean addWatcher(String userName, HashMap<String, User> map) {
			if (!mWatchedBy.contains(userName.toLowerCase())) {
				mWatchedBy.add(userName.toLowerCase());
				User watchingUser = map.get(userName.toLowerCase());
				if (watchingUser != null) {
					watchingUser.addWatchedUser(userName.toLowerCase(), map);
					return false;
				}
				else {
					return true;
				}
			}
			return false;
		}
		
		public final String getName() {
			return mName;
		}
		public final ArrayList<String> getWatchList() {
			return mWatching;
		}
		public final ArrayList<String> getWatchedByList() {
			return mWatchedBy;
		}
	}
	
	private final class QueueItem implements Comparable<QueueItem> {
		public String name;
		public int degree;
		
		public QueueItem(String name, int degree) {
			this.name = name;
			this.degree = degree;
		}
		
		public int compareTo(QueueItem item) {
			return Integer.compare(this.degree, item.degree);
		}
	}
}