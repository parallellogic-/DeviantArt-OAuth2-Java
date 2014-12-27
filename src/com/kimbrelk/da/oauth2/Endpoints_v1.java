package com.kimbrelk.da.oauth2;

public final class Endpoints_v1 extends Endpoints {
	public final static int VER_MAJOR = 1;
	private final static String PREFIX_ENDPOINT = 			"https://www.deviantart.com/api/v" + VER_MAJOR + "/oauth2/";

	private final static String PREFIX_BROWSE = 			PREFIX_ENDPOINT + "browse/";
	public final static String BROWSE_CATEGORYTREE = 		PREFIX_BROWSE + "categorytree";
	public final static String BROWSE_DAILYDEVIATIONS = 	PREFIX_BROWSE + "dailydeviations";
	public final static String BROWSE_HOT = 				PREFIX_BROWSE + "hot";
	public final static String BROWSE_MORELIKETHIS = 		PREFIX_BROWSE + "morelikethis";
	public final static String BROWSE_MORELIKETHIS_PREV =	BROWSE_MORELIKETHIS + "/preview";
	public final static String BROWSE_NEWEST = 				PREFIX_BROWSE + "newest";
	public final static String BROWSE_POPULAR = 			PREFIX_BROWSE + "popular";
	public final static String BROWSE_TAGS = 				PREFIX_BROWSE + "tags";
	public final static String BROWSE_TAGS_SEARCH = 		BROWSE_TAGS + "/search";
	public final static String BROWSE_UNDISCOVERED = 		PREFIX_BROWSE + "undiscovered";
	public final static String BROWSE_USER_JOURNALS = 		PREFIX_BROWSE + "user/journals";
	
	public final static String COLLECTIONS = 				PREFIX_ENDPOINT + "collections/";
	public final static String COLLECTIONS_FAVE = 			COLLECTIONS + "fave";
	public final static String COLLECTIONS_FOLDERS = 		COLLECTIONS + "folders";
	public final static String COLLECTIONS_UNFAVE = 		COLLECTIONS + "unfave";
	
	public final static String COMMENTS = 					PREFIX_ENDPOINT + "comments/";
	public final static String COMMENTS_DEVIATION = 		COMMENTS + "deviation/";
	public final static String COMMENTS_POST = 				COMMENTS + "post/";
	public final static String COMMENTS_POST_DEVIATION = 	COMMENTS_POST + "deviation/";
	public final static String COMMENTS_POST_PROFILE =	 	COMMENTS_POST + "profile/";
	public final static String COMMENTS_POST_STATUS =	 	COMMENTS_POST + "status/";
	public final static String COMMENTS_PROFILE =	 		COMMENTS + "profile/";
	public final static String COMMENTS_STATUS =	 		COMMENTS + "status/";
	
	public final static String CURATED = 					PREFIX_ENDPOINT + "curated";
	public final static String CURATED_TAGS = 				CURATED + "/tags";
	
	public final static String DEVIATION = 					PREFIX_ENDPOINT + "deviation/";
	public final static String DEVIATION_CONTENT = 			DEVIATION + "content";
	public final static String DEVIATION_EMBEDDED = 		DEVIATION + "embeddedcontent";
	public final static String DEVIATION_METADATA = 		DEVIATION + "metadata";
	public final static String DEVIATION_WHOFAVED = 		DEVIATION + "whofaved";
	
	private final static String PREFIX_FEED = 				PREFIX_ENDPOINT + "feed/";
	public final static String FEED_HOME = 					PREFIX_FEED + "home";
	public final static String FEED_NOTIFICATIONS = 		PREFIX_FEED + "notifications";
	public final static String FEED_PROFILE = 				PREFIX_FEED + "profile";
	
	public final static String GALLERY = 					PREFIX_ENDPOINT + "gallery/";
	public final static String GALLERY_FOLDERS = 			GALLERY + "folders";

	private final static String GROUP = 					PREFIX_ENDPOINT + "group/";
	private final static String GROUP_SUGGEST = 			GROUP + "suggest/";
	public final static String GROUP_SUGGEST_FAVE = 		GROUP_SUGGEST + "fave";
	
	private final static String PREFIX_STASH = 				PREFIX_ENDPOINT + "stash/";
	public final static String STASH_DELETE = 				PREFIX_STASH + "delete";
	public final static String STASH_DELTA = 				PREFIX_STASH + "delta";
	public final static String STASH_FOLDER = 				PREFIX_STASH + "folder";
	public final static String STASH_MEDIA = 				PREFIX_STASH + "media";
	public final static String STASH_METADATA = 			PREFIX_STASH + "metadata";
	public final static String STASH_MOVE_FILE = 			PREFIX_STASH + "move/file";
	public final static String STASH_MOVE_FOLDER = 			PREFIX_STASH + "move/folder";
	public final static String STASH_PUBLISH = 				PREFIX_STASH + "publish";
	public final static String STASH_PUBLISH_CATEGORYTREE = STASH_PUBLISH + "/categorytree";
	public final static String STASH_PUBLISH_USERDATA = 	STASH_PUBLISH + "/userdata";
	public final static String STASH_SPACE = 				PREFIX_STASH + "space";
	public final static String STASH_SUBMIT = 				PREFIX_STASH + "submit";
	
	private final static String PREFIX_USER = 				PREFIX_ENDPOINT + "user/";
	public final static String USER_DAMNTOKEN = 			PREFIX_USER + "damntoken";
	public final static String USER_FRIENDS = 				PREFIX_USER + "friends/";
	public final static String USER_FRIENDS_SEARCH = 		USER_FRIENDS + "search";
	public final static String USER_FRIENDS_UNWATCH = 		USER_FRIENDS + "unwatch/";
	public final static String USER_FRIENDS_WATCH = 		USER_FRIENDS + "watch/";
	public final static String USER_FRIENDS_WATCHING = 		USER_FRIENDS + "watching/";
	public final static String USER_PROFILE = 				PREFIX_USER + "profile/";
	public final static String USER_PROFILE_UPDATE = 		USER_PROFILE + "UPDATE";
	public final static String USER_STATUSES = 				PREFIX_USER + "statuses/";
	public final static String USER_STATUSES_POST = 		USER_STATUSES + "post";
	public final static String USER_WATCHERS = 				PREFIX_USER + "watchers/";
	public final static String USER_WHOAMI = 				PREFIX_USER + "whoami";
	public final static String USER_WHOIS = 				PREFIX_USER + "whois";
	
	public final static String UTIL_PLACEBO = 				PREFIX_ENDPOINT + "placebo";
}