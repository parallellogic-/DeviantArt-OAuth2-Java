package com.kimbrelk.da.oauth2;

public class Endpoint {
	public static int VER_MAJOR;
	private final static String PREFIX_OAUTH2 = 	"https://www.deviantart.com/oauth2/";
	public final static String OAUTH2_AUTHORIZE = 	PREFIX_OAUTH2 + "authorize";
	public final static String OAUTH2_REVOKE = 		PREFIX_OAUTH2 + "revoke";
	public final static String OAUTH2_TOKEN = 		PREFIX_OAUTH2 + "token";
}