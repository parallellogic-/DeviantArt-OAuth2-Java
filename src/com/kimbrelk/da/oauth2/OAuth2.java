package com.kimbrelk.da.oauth2;

import com.kimbrelk.da.oauth2.response.RespBrowseMorelikethisPreview;
import com.kimbrelk.da.oauth2.response.RespBrowseTagsSearch;
import com.kimbrelk.da.oauth2.response.RespCategory;
import com.kimbrelk.da.oauth2.response.RespCollections;
import com.kimbrelk.da.oauth2.response.RespCollectionsFave;
import com.kimbrelk.da.oauth2.response.RespCollectionsFolders;
import com.kimbrelk.da.oauth2.response.RespCurated;
import com.kimbrelk.da.oauth2.response.RespCuratedTags;
import com.kimbrelk.da.oauth2.response.RespDeviation;
import com.kimbrelk.da.oauth2.response.RespDeviationContent;
import com.kimbrelk.da.oauth2.response.RespDeviationEmbeddedContent;
import com.kimbrelk.da.oauth2.response.RespDeviationMetadata;
import com.kimbrelk.da.oauth2.response.RespDeviationWhofaved;
import com.kimbrelk.da.oauth2.response.RespDeviations;
import com.kimbrelk.da.oauth2.response.RespDeviationsQuery;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespFeed;
import com.kimbrelk.da.oauth2.response.RespFeedNotifications;
import com.kimbrelk.da.oauth2.response.RespFriends;
import com.kimbrelk.da.oauth2.response.RespGallery;
import com.kimbrelk.da.oauth2.response.RespGalleryFolders;
import com.kimbrelk.da.oauth2.response.RespStashPublishUserdata;
import com.kimbrelk.da.oauth2.response.RespStashSpace;
import com.kimbrelk.da.oauth2.response.RespToken;
import com.kimbrelk.da.oauth2.response.RespUserDamntoken;
import com.kimbrelk.da.oauth2.response.RespUserFriends;
import com.kimbrelk.da.oauth2.response.RespUserFriendsWatching;
import com.kimbrelk.da.oauth2.response.RespUserProfile;
import com.kimbrelk.da.oauth2.response.RespUserStatus;
import com.kimbrelk.da.oauth2.response.RespUserStatuses;
import com.kimbrelk.da.oauth2.response.RespUserStatusPost;
import com.kimbrelk.da.oauth2.response.RespUserWatchers;
import com.kimbrelk.da.oauth2.response.RespUserWhoami;
import com.kimbrelk.da.oauth2.response.RespUserWhois;
import com.kimbrelk.da.oauth2.response.Response;
import com.kimbrelk.da.oauth2.struct.GalleryMode;
import com.kimbrelk.da.oauth2.struct.User;
import com.kimbrelk.da.oauth2.struct.Watch;
import com.kimbrelk.da.oauth2.struct.Whois;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("static-access")
public final class OAuth2 {
	private final static Version VERSION = new Version(1, 20141204);
	private final static Endpoints_v1 ENDPOINTS = new Endpoints_v1();
	private final static boolean SHOW_MATURE_DEFAULT = true;
	
	private RespToken mToken;
	private ClientCredentials mClientCredentials;
	private StringBuffer mLog;
	private String mUserAgent;
	
	public OAuth2(ClientCredentials clientCredentials, String userAgent) {
		mClientCredentials = clientCredentials;
		mLog = new StringBuffer("");
		mUserAgent = userAgent;
	}
	
	protected final static String createURL(String url, Map<String, String> parameters) {
		if (parameters == null) {
			return url;
		}
		else if (parameters.size() == 0) {
			return url;
		}
		else {
			String ret = url + "?";
			Iterator<String> itter = parameters.keySet().iterator();
			int numLeft = parameters.size();
			while (itter.hasNext()) {
				try {
					String varName = itter.next();
					numLeft--;
					ret += varName + "=" + URLEncoder.encode(parameters.get(varName), "UTF-8");
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (numLeft != 0) {
					ret += "&";
				}
			}
			return ret;
		}
	}
	
	public final String getAuthorizeURL(String redirectURI, Scope... scopes) {
		String ret = ENDPOINTS.OAUTH2_AUTHORIZE + "?";
		if (scopes.length > 0) {
			ret += "scope=";
			for(int a=0; a<scopes.length; a++) {
				ret += scopes[a].toString().replace("_", ".").toLowerCase();
				if (a < scopes.length - 1) {
					ret += " ";
				}
			}
			ret += "&";
		}
		ret += "client_id=" + mClientCredentials.getId() + "&response_type=code&redirect_uri=" + redirectURI;
		ret = ret.replace(" ", "%20");
		return ret;
	}
	public final RespToken getToken() {
		return mToken;
	}
	public final Scope[] getScopes() {
		if (mToken == null || mToken.getScopes() == null) {
			return new Scope[0];
		}
		else {
			return mToken.getScopes();
		}
	}
	public final Version getVersion() {
		return VERSION;
	}

	private final boolean hasAccessToken() {
		return !(mToken == null || mToken.getToken() == null);
	}
	private final boolean hasRefreshToken() {
		return !(mToken == null || mToken.getRefreshToken() == null);
	}
	public final boolean hasScopes(Scope... scopes) {
		Scope[] curScopes = getScopes();
		for(Scope scope: scopes) {
			boolean has = false;
			for(Scope tScope: curScopes) {
				if (scope == tScope) {
					has = true;
					break;
				}
			}
			if (!has) {
				return false;
			}
		}
		return true;
	}
	
	public final Response requestAuthRevoke() {
		Response respVerify = verifyScopesAndAuth();
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.OAUTH2_REVOKE, params));
		try {
			if (json.getString("status").equalsIgnoreCase("success")) {
				return new Response();
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestAuthToken(AuthGrantType grantType, String code, String redirectUri) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", mClientCredentials.getId() + "");
		params.put("client_secret", mClientCredentials.getSecret());
		params.put("grant_type", grantType.toString().toLowerCase());
		switch (grantType) {
			case CLIENT_CREDENTIALS: {
				break;
			}
			case AUTHORIZATION_CODE: {
				if (code == null) {
					throw new InvalidParameterException("Parameter code cannot be null when using the CODE grantType.");
				}
				if (redirectUri == null) {
					throw new InvalidParameterException("Parameter redirectUri cannot be null when using the CODE grantType.");
				}
				params.put("code", code);
				params.put("redirect_uri", redirectUri);
				break;
			}
			case REFRESH_TOKEN: {
				if (code == null) {
					throw new InvalidParameterException("Parameter code cannot be null when using the REFRESH_TOKEN grantType.");
				}
				params.put("refresh_token", code);
				break;
			}
			default: {
				throw new InvalidParameterException("Unsupported grantType: " + grantType);
			}
		}
		
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.OAUTH2_TOKEN, params));
		Response response = null;
		try {
			if (json.getString("status").equalsIgnoreCase("error")) {
				response = new RespError(json);
			}
			else {
				if (grantType == AuthGrantType.CLIENT_CREDENTIALS) {
					response = new RespToken(
							json.getInt("expires_in"), 
							json.getString("access_token"), 
							null);
				}
				else {
					String[] parse = json.getString("scope").replace(".", "_").toUpperCase().split("[ ]+");
					Scope scopes[] = new Scope[parse.length];
					for(int a=0; a<parse.length; a++) {
						scopes[a] = Scope.valueOf(parse[a]);
					}
					response = new RespToken(
							json.getInt("expires_in"), 
							json.getString("access_token"), 
							json.getString("refresh_token"),
							scopes);
				}
				mToken = (RespToken)response;
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public final Response requestBrowseCategorytree() {
		return requestBrowseCategorytree("/");
	}
	public final Response requestBrowseCategorytree(String categoryPath) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (categoryPath == null) {
			categoryPath = "/";
		}
		params.put("catpath", categoryPath);
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_CATEGORYTREE, params));
		try {
			if (!json.has("error")) {
				return new RespCategory(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseDailydeviations(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month-1, day);
		return requestBrowseDailydeviations(c.getTime());
	}
	public final Response requestBrowseDailydeviations(Date date) {
		final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return requestBrowseDailydeviations(dateFormat.format(date));
	}
	public final Response requestBrowseDailydeviations(String date) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (date != null) {
			params.put("date", date);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_DAILYDEVIATIONS, params));
		try {
			if (!json.has("error")) {
				return new RespDeviations(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseHot() {
		return requestBrowseHot(null, -1, -1);
	}
	public final Response requestBrowseHot(String categoryPath) {
		return requestBrowseHot(categoryPath, -1, -1);
	}
	public final Response requestBrowseHot(String categoryPath, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (categoryPath != null) {
			params.put("category_path", categoryPath);
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_HOT, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationsQuery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseMorelikethis(String deviationId) {
		return requestBrowseMorelikethis(deviationId, null);
	}
	public final Response requestBrowseMorelikethis(String deviationId, String categoryPath) {
		return requestBrowseMorelikethis(deviationId, categoryPath, -1, -1);
	}
	public final Response requestBrowseMorelikethis(String deviationId, String categoryPath, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE, Scope.BROWSE_MLT);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		params.put("seed", deviationId);
		if (categoryPath != null) {
			params.put("category_path", categoryPath);
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_MORELIKETHIS, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationsQuery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseMorelikethisPreview(String deviationId) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE, Scope.BROWSE_MLT);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		params.put("seed", deviationId);
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_MORELIKETHIS_PREV, params));
		try {
			if (!json.has("error")) {
				return new RespBrowseMorelikethisPreview(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseNewest() {
		return requestBrowseNewest(null, null, -1, -1);
	}
	public final Response requestBrowseNewest(String categoryPath, String query) {
		return requestBrowseNewest(categoryPath, query, -1, -1);
	}
	public final Response requestBrowseNewest(String categoryPath, String query, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (categoryPath != null) {
			params.put("category_path", categoryPath);
		}
		if (query != null) {
			params.put("q", query);
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_NEWEST, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationsQuery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowsePopular() {
		return requestBrowsePopular(null, null, -1, -1, null);
	}
	public final Response requestBrowsePopular(String categoryPath, String query, String timeRange) {
		return requestBrowsePopular(categoryPath, query, -1, -1, timeRange);
	}
	public final Response requestBrowsePopular(String categoryPath, String query, int offset, int limit, String timeRange) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (categoryPath != null) {
			params.put("category_path", categoryPath);
		}
		if (query != null) {
			params.put("q", query);
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		if (timeRange != null) {
			params.put("timerange", timeRange);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_POPULAR, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationsQuery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseTags(String tag) {
		return requestBrowseTags(tag, -1, -1);
	}
	public final Response requestBrowseTags(String tag, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (tag == null) {
			return RespError.REQUEST_FAILED;
		}
		params.put("tag", tag);
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_TAGS, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationsQuery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseTagsSearch(String tag) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (tag != null) {
			params.put("tag_name", tag);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_TAGS_SEARCH, params));
		try {
			if (!json.has("error")) {
				return new RespBrowseTagsSearch(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseUndiscovered() {
		return requestBrowseUndiscovered(null);
	}
	public final Response requestBrowseUndiscovered(String categoryPath) {
		return requestBrowseUndiscovered(categoryPath, -1, -1);
	}
	public final Response requestBrowseUndiscovered(String categoryPath, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (categoryPath != null) {
			params.put("category_path", categoryPath);
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_UNDISCOVERED, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationsQuery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestBrowseUserJournals(String userName) {
		return requestBrowseUserJournals(userName, true);
	}
	public final Response requestBrowseUserJournals(String userName, boolean featuredOnly) {
		return requestBrowseUserJournals(userName, featuredOnly, -1, -1);
	}
	public final Response requestBrowseUserJournals(String userName, boolean featuredOnly, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (userName == null) {
			return RespError.INVALID_REQUEST;
		}
		params.put("username", userName);
		params.put("featured", featuredOnly + "");
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.BROWSE_USER_JOURNALS, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationsQuery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestCollections(String folderId) {
		return requestCollections(null, folderId, -1, -1);
	}
	public final Response requestCollections(String userName, String folderId) {
		return requestCollections(userName, folderId, -1, -1);
	}
	public final Response requestCollections(String userName, String folderId, int offset, int limit) {
		return requestCollections(userName, folderId, offset, limit, SHOW_MATURE_DEFAULT);
	}
	public final Response requestCollections(String userName, String folderId, int offset, int limit, boolean showMature) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null && hasRefreshToken()) {
			return RespError.INVALID_REQUEST;
		}
		if (folderId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (userName != null) {
			params.put("username", userName);
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		params.put("mature_content", showMature + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.COLLECTIONS + folderId, params));
		try {
			if (!json.has("error")) {
				return new RespCollections(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestCollectionsFave(String deviationId, String... folderIds) {
		Response respVerify = verifyScopesAndAuth(Scope.BROWSE, Scope.COLLECTION);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("deviationid", deviationId);
		if (folderIds != null) {
			for(int a=0; a<folderIds.length; a++) {
				postParams.put("folderid%5B" + a + "%5D", folderIds[a]);
			}
		}
		JSONObject json = requestJSON(Verb.POST, createURL(ENDPOINTS.COLLECTIONS_FAVE, params), postParams);
		try {
			if (!json.has("error")) {
				return new RespCollectionsFave(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestCollectionsFolders() {
		return requestCollectionsFolders(null, false, false);
	}
	public final Response requestCollectionsFolders(String userName) {
		return requestCollectionsFolders(userName, false, false);
	}
	public final Response requestCollectionsFolders(String userName, boolean calculateSize, boolean preloadDeviations) {
		return requestCollectionsFolders(userName, calculateSize, preloadDeviations, SHOW_MATURE_DEFAULT);
	}
	public final Response requestCollectionsFolders(String userName, boolean calculateSize, boolean preloadDeviations, boolean showMature) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null && hasRefreshToken()) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (userName != null) {
			params.put("username", userName);
		}
		params.put("calculate_size", calculateSize + "");
		params.put("ext_preload", preloadDeviations + "");
		params.put("mature_content", showMature + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.COLLECTIONS_FOLDERS, params));
		try {
			if (!json.has("error")) {
				return new RespCollectionsFolders(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestCollectionsUnFave(String deviationId, String... folderIds) {
		Response respVerify = verifyScopesAndAuth(Scope.BROWSE, Scope.COLLECTION);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("deviationid", deviationId);
		if (folderIds != null) {
			for(int a=0; a<folderIds.length; a++) {
				postParams.put("folderid%5B" + a + "%5D", folderIds[a]);
			}
		}
		JSONObject json = requestJSON(Verb.POST, createURL(ENDPOINTS.COLLECTIONS_UNFAVE, params), postParams);
		try {
			if (!json.has("error")) {
				return new RespCollectionsFave(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestCurated() {
		return requestCurated(0, null);
	}
	public final Response requestCurated(int offset) {
		return requestCurated(offset, null);
	}
	public final Response requestCurated(String expansions) {
		return requestCurated(0, expansions);
	}
	public final Response requestCurated(int offset, String expansions) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		else {
			params.put("offset", "0");
		}
		if (expansions != null) {
			params.put("expand", expansions);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.CURATED, params));
		try {
			if (!json.has("error")) {
				return new RespCurated(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestCuratedTags() {
		return requestCuratedTags(false);
	}
	public final Response requestCuratedTags(boolean getDeviations) {
		return requestCuratedTags(getDeviations, SHOW_MATURE_DEFAULT);
	}
	public final Response requestCuratedTags(boolean getDeviations, boolean showMatureContent) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("ext_preload", getDeviations + "");
		params.put("mature_content", getDeviations + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.CURATED_TAGS, params));
		try {
			if (!json.has("error")) {
				return new RespCuratedTags(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestDeviation(String deviationId) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.DEVIATION + deviationId, params));
		try {
			if (!json.has("error")) {
				return new RespDeviation(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestDeviationContent(String deviationId) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("deviationid", deviationId);
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.DEVIATION_CONTENT, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationContent(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestDeviationEmbeddedContent(String deviationId) {
		return requestDeviationEmbeddedContent(deviationId, null);
	}
	public final Response requestDeviationEmbeddedContent(String deviationId, String offsetDeviationId) {
		return requestDeviationEmbeddedContent(deviationId, offsetDeviationId, -1, -1);
	}
	public final Response requestDeviationEmbeddedContent(String deviationId, int offset, int limit) {
		return requestDeviationEmbeddedContent(deviationId, null, offset, limit);
	}
	public final Response requestDeviationEmbeddedContent(String deviationId, String offsetDeviationId, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("deviationid", deviationId);
		if (offsetDeviationId != null) {
			params.put("offset_deviationid", offsetDeviationId);
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.DEVIATION_EMBEDDED, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationEmbeddedContent(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestDeviationMetadata(String... deviationIds) {
		return requestDeviationMetadata(false, false, false, false, deviationIds);
	}
	public final Response requestDeviationMetadata(boolean includeCameraData, boolean includeSubmissionData, boolean includeStats, boolean includeCollectionData, String... deviationIds) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (deviationIds == null || deviationIds.length == 0) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		for(int a=0; a<deviationIds.length; a++) {
			params.put("deviationids%5B" + a + "%5D", deviationIds[a]);
		}
		params.put("ext_camera", includeCameraData + "");
		params.put("ext_collection", includeCollectionData + "");
		params.put("ext_stats", includeStats + "");
		params.put("ext_submission", includeSubmissionData + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.DEVIATION_METADATA, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationMetadata(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestDeviationWhofaved(String deviationId) {
		return requestDeviationWhofaved(deviationId, null);
	}
	public final Response requestDeviationWhofaved(String deviationId, String expand) {
		return requestDeviationWhofaved(deviationId, -1, -1, expand);
	}
	public final Response requestDeviationWhofaved(String deviationId, int offset, int limit) {
		return requestDeviationWhofaved(deviationId, offset, limit, null);
	}
	public final Response requestDeviationWhofaved(String deviationId, int offset, int limit, String expansions) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (deviationId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("deviationid", deviationId);
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		if (expansions != null) {
			params.put("expand", expansions);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.DEVIATION_WHOFAVED, params));
		try {
			if (!json.has("error")) {
				return new RespDeviationWhofaved(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestFeedHome() {
		return requestFeedHome(null);
	}
	public final Response requestFeedHome(String cursor) {
		return requestFeedHome(cursor, SHOW_MATURE_DEFAULT);
	}
	public final Response requestFeedHome(boolean showMatureContent) {
		return requestFeedHome(null, showMatureContent);
	}
	public final Response requestFeedHome(String cursor, boolean showMatureContent) {
		Response respVerify = verifyScopesAndAuth(Scope.FEED);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (cursor != null) {
			params.put("cursor", cursor);
		}
		params.put("mature_content", showMatureContent + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.FEED_HOME, params));
		try {
			if (!json.has("error")) {
				return new RespFeed(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestFeedNotifications() {
		return requestFeedNotifications(null);
	}
	public final Response requestFeedNotifications(String cursor) {
		Response respVerify = verifyScopesAndAuth(Scope.FEED);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (cursor != null) {
			params.put("cursor", cursor);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.FEED_NOTIFICATIONS, params));
		try {
			if (!json.has("error")) {
				return new RespFeedNotifications(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestFeedProfile() {
		return requestFeedProfile(null);
	}
	public final Response requestFeedProfile(String cursor) {
		Response respVerify = verifyScopesAndAuth(Scope.FEED);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (cursor != null) {
			params.put("cursor", cursor);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.FEED_PROFILE, params));
		try {
			if (!json.has("error")) {
				return new RespFeed(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestGallery(String folderId) {
		return requestGallery(null, folderId);
	}
	public final Response requestGallery(String userName, String folderId) {
		return requestGallery(userName, folderId, null);
	}
	public final Response requestGallery(String userName, String folderId, GalleryMode mode) {
		return requestGallery(userName, folderId, mode, -1, -1);
	}
	public final Response requestGallery(String userName, String folderId, GalleryMode mode, int offset, int limit) {
		return requestGallery(userName, folderId, mode, offset, limit, SHOW_MATURE_DEFAULT);
	}
	public final Response requestGallery(String userName, String folderId, GalleryMode mode, int offset, int limit, boolean showMatureContent) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (folderId == null) {
			return RespError.INVALID_REQUEST;
		}
		if (userName == null && mToken.getRefreshToken() == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (userName != null) {
			params.put("username", userName);
		}
		if (mode != null) {
			params.put("mode", mode.toString().toLowerCase());
		}
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		params.put("mature_content", showMatureContent + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.GALLERY + folderId, params));
		try {
			if (!json.has("error")) {
				return new RespGallery(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestGalleryFolders() {
		return requestGalleryFolders(null);
	}
	public final Response requestGalleryFolders(String userName) {
		return requestGalleryFolders(userName, false, false, SHOW_MATURE_DEFAULT);
	}
	public final Response requestGalleryFolders(String userName, boolean calculateSize, boolean preloadDeviations, boolean showMatureContent) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null && mToken.getRefreshToken() == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("username", userName);
		params.put("calculate_size", calculateSize + "");
		params.put("ext_preload", preloadDeviations + "");
		params.put("mature_content", showMatureContent + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.GALLERY_FOLDERS, params));
		try {
			if (!json.has("error")) {
				return new RespGalleryFolders(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestStashPublishCategorytree() {
		return requestStashPublishCategorytree("/");
	}
	public final Response requestStashPublishCategorytree(String categoryPath) {
		return requestStashPublishCategorytree(categoryPath, null);
	}
	public final Response requestStashPublishCategorytree(String categoryPath, String fileType) {
		Response respVerify = verifyScopesAndAuth(true, Scope.STASH, Scope.PUBLISH);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (categoryPath == null) {
			categoryPath = "/";
		}
		params.put("catpath", categoryPath);
		if (fileType != null) {
			params.put("filetype", fileType);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.STASH_PUBLISH_CATEGORYTREE, params));
		try {
			if (!json.has("error")) {
				return new RespCategory(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestStashPublishUserdata() {
		Response respVerify = verifyScopesAndAuth(Scope.STASH, Scope.PUBLISH);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.STASH_PUBLISH_USERDATA, params));
		try {
			if (!json.has("error")) {
				return new RespStashPublishUserdata(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestStashSpace() {
		Response respVerify = verifyScopesAndAuth(Scope.STASH);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.STASH_SPACE, params));
		try {
			if (!json.has("error")) {
				return new RespStashSpace(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestUserDamntoken() {
		Response respVerify = verifyScopesAndAuth(Scope.USER);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_DAMNTOKEN, params));
		try {
			if (!json.has("error")) {
				return new RespUserDamntoken(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserFriends(String userName) {
		return requestUserFriends(userName, null);
	}
	public final Response requestUserFriends(String userName, String expansions) {
		return requestUserFriends(userName, expansions, -1, -1);
	}
	public final Response requestUserFriends(String userName, String expansions, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		if (expansions != null) {
			params.put("expand", expansions);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_FRIENDS + userName, params));
		try {
			if (!json.has("error")) {
				return new RespUserFriends(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserFriendsSearch(String query) {
		return requestUserFriendsSearch(null, query);
	}
	public final Response requestUserFriendsSearch(String userName, String query) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null && mToken.getRefreshToken() == null) {
			return RespError.INVALID_REQUEST;
		}
		if (query == null || query.length() == 0) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("username", userName);
		params.put("query", query);
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_FRIENDS_SEARCH, params));
		try {
			if (!json.has("error")) {
				return new RespFriends(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserFriendsUnwatch(String userName) {
		Response respVerify = verifyScopesAndAuth(Scope.BROWSE, Scope.USER_MANAGE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_FRIENDS_UNWATCH + userName, params));
		try {
			if (!json.has("error")) {
				return new Response();
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserFriendsWatch(String userName, Watch watch) {
		Response respVerify = verifyScopesAndAuth(Scope.BROWSE, Scope.USER_MANAGE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null || watch == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.POST, createURL(ENDPOINTS.USER_FRIENDS_WATCH + userName, params), watch.parameterize());
		try {
			if (!json.has("error")) {
				return new Response();
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserFriendsWatching(String userName) {
		Response respVerify = verifyScopesAndAuth(Scope.BROWSE, Scope.USER);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_FRIENDS_WATCHING + userName, params));
		try {
			if (!json.has("error")) {
				return new RespUserFriendsWatching(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserProfile(String userName) {
		return requestUserProfile(userName, null);
	}
	public final Response requestUserProfile(String userName, String expansions) {
		return requestUserProfile(userName, expansions, false, false);
	}
	public final Response requestUserProfile(String userName, String expansions, boolean getCollections, boolean getGalleries) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (expansions != null) {
			params.put("expand", expansions);
		}
		params.put("ext_collections", getCollections + "");
		params.put("ext_galleries", getGalleries + "");
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_PROFILE + userName, params));
		try {
			if (!json.has("error")) {
				return new RespUserProfile(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserProfileUpdate(int userIsArtist, int artistLevel, 
			int artistSpecialty, String realName, String tagline, int countryid, String website, 
			String bio) {
		Response respVerify = verifyScopesAndAuth(Scope.BROWSE, Scope.USER_MANAGE);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		Map<String, String> paramsPost = new HashMap<String, String>();
		if (userIsArtist == 0 || userIsArtist == 1) {
			paramsPost.put("user_is_artist", userIsArtist + "");
		}
		if (artistLevel != -1) {
			paramsPost.put("artist_level", "" + artistLevel);
		}
		if (artistSpecialty != -1) {
			paramsPost.put("artist_specialty", "" + artistSpecialty);
		}
		if (realName != null) {
			paramsPost.put("real_name", realName);
		}
		if (tagline != null) {
			paramsPost.put("tagline", tagline);
		}
		if (countryid != -1) {
			paramsPost.put("countryid", "" + countryid);
		}
		if (website != null) {
			paramsPost.put("website", website);
		}
		if (bio != null) {
			paramsPost.put("bio", bio);
		}
		JSONObject json = requestJSON(Verb.POST, createURL(ENDPOINTS.USER_PROFILE_UPDATE, params), paramsPost);
		try {
			if (!json.has("error")) {
				return new Response();
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserStatus(String statusId) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (statusId == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_STATUSES + statusId, params));
		try {
			if (!json.has("error")) {
				return new RespUserStatus(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserStatusPost(String body) {
		return requestUserStatusPost(body, null, null, null);
	}
	public final Response requestUserStatusPost(String body, String objectId, String parentId, String stashId) {
		Response respVerify = verifyScopesAndAuth(Scope.USER_MANAGE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (body == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("body", body);
		if (objectId != null) {
			postParams.put("id", objectId);
		}
		if (parentId != null) {
			postParams.put("parentid", parentId);
		}
		if (stashId != null) {
			postParams.put("stashid", stashId);
		}
		JSONObject json = requestJSON(Verb.POST, createURL(ENDPOINTS.USER_STATUSES_POST, params), postParams);
		try {
			if (!json.has("error")) {
				return new RespUserStatusPost(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserStatuses(String userName) {
		return requestUserStatuses(userName, -1, -1);
	}
	public final Response requestUserStatuses(String userName, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		params.put("username", userName);
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_STATUSES, params));
		try {
			if (!json.has("error")) {
				return new RespUserStatuses(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserWatchers(String userName) {
		return requestUserWatchers(userName, null, -1, -1);
	}
	public final Response requestUserWatchers(String userName, String expansions) {
		return requestUserWatchers(userName, expansions, -1, -1);
	}
	public final Response requestUserWatchers(String userName, String expansions, int offset, int limit) {
		Response respVerify = verifyScopesAndAuth(true, Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (userName == null) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		if (offset != -1) {
			params.put("offset", offset + "");
		}
		if (limit != -1) {
			params.put("limit", limit + "");
		}
		if (expansions != null) {
			params.put("expand", expansions);
		}
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_WATCHERS + userName, params));
		try {
			if (!json.has("error")) {
				return new RespUserWatchers(json);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserWhoami() {
		Response respVerify = verifyScopesAndAuth(Scope.USER);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.USER_WHOAMI, params));
		try {
			if (!json.has("error")) {
				return new RespUserWhoami(new User(json));
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public final Response requestUserWhois(String... users) {
		Response respVerify = verifyScopesAndAuth(Scope.BROWSE);
		if (respVerify.isError()) {
			return respVerify;
		}
		if (users.length == 0) {
			return RespError.INVALID_REQUEST;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		String postData = "";
		for(int a=0; a<users.length; a++) {
			postData += "usernames[" + a + "]=" + users[a] + "\n";
		}
		JSONObject json = requestJSON(Verb.POST, createURL(ENDPOINTS.USER_WHOIS, params), postData);
		try {
			if (!json.has("error")) {
				JSONArray jsonResults = json.getJSONArray("results");
				Whois[] results = new Whois[jsonResults.length()];
				for(int a=0; a<results.length; a++) {
					results[a] = new Whois(jsonResults.getJSONObject(a));
				}
				return new RespUserWhois(results);
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public final Response requestUtilPlacebo() {
		Response respVerify = verifyScopesAndAuth(true);
		if (respVerify.isError()) {
			return respVerify;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINTS.UTIL_PLACEBO, params));
		try {
			if (json.getString("status").equalsIgnoreCase("success")) {
				return new Response();
			}
			else {
				return new RespError(json);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected final JSONObject requestJSON(Verb verb, String url) {
		return requestJSON(verb, url, (String)null);
	}
	protected final JSONObject requestJSON(Verb verb, String url, Map<String, String> postParams) {
		String postData = "";
		Set<String> keys = postParams.keySet();
		int len = keys.size();
		int a=0;
		for(String key: keys) {
			postData += key + "=" + postParams.get(key);
			if (a < len - 1) {
				postData += "&";
			}
			a++;
		}
		return requestJSON(verb, url, postData);
	}
	protected final JSONObject requestJSON(Verb verb, String url, String postData) {
		try {
			mLog.append("SEND:\n");
			HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
			connection.setRequestProperty("User-Agent", mUserAgent);
			connection.setRequestProperty("dA-minor-version", "" + VERSION.getMinor());
			connection.setReadTimeout(30000);
			connection.setConnectTimeout(30000);
			mLog.append(verb.toString() + " ");
			mLog.append(url);
			mLog.append("\n");
			connection.setRequestMethod(verb.toString());
			if (verb == Verb.POST) {
				mLog.append(postData);
				mLog.append("\n");
				connection.setDoOutput(true);
				connection.setDoInput(true);
				//connection.setRequestProperty("Authorization", "Basic " + base64(CLIENT_ID + ":" + loadLast()));
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length", "" + postData.length());
				connection.connect();
				OutputStream os = connection.getOutputStream();
				os.write(postData.getBytes());
				os.flush();
			}
			else if (verb == Verb.GET) {
				connection.setDoOutput(false);
				connection.setDoInput(true);
				connection.connect();
			}
			mLog.append("\nRECV:\n");
			
			try {
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader in = new BufferedReader(isr);
				String line = "";
				String page = "";
				while ( (line = in.readLine()) != null) {
					page += line;
				}
				mLog.append(page);
				mLog.append("\n\n");
				return new JSONObject(page);
			}
			catch (Exception e) {
				try {
					JSONObject json = new JSONObject();
					json.put("status", "error");
					try {
						InputStream is = connection.getErrorStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader in = new BufferedReader(isr);
						String line = "";
						String page = "";
						while( (line = in.readLine()) != null) {
							page += line;
						}
						mLog.append(page);
						mLog.append("\n\n");
						return new JSONObject(page);
					}
					catch (Exception err) {
						
					}
					String str = "";
					str += "URL: " + url.split("[?]+")[0] + "\n";
					//str += "POST Data: " + postData + "\n";
					str += "\n";
					for(int a=0; a<connection.getHeaderFields().size()-1; a++) {
						str += connection.getHeaderFieldKey(a) + ": " + connection.getHeaderField(a) + "\n";
					}
					json.put("error_description", str);
					return json;
				}
				catch (Exception er) {
					throw e;
				}
			}
			finally {
				connection.disconnect();
			}
		}
		catch (Exception e) {
			try {
				e.printStackTrace();
				JSONObject json = new JSONObject();
				json.put("status", "error");
				json.put("error", RespError.REQUEST_FAILED.getType());
				json.put("error_description", RespError.REQUEST_FAILED.getDescription() + " : " + e);
				return json;
			}
			catch (JSONException er) {
				er.printStackTrace();
				return null;
			}
		}
	}
	
	public final Response verifyScopesAndAuth(Scope... scopes) {
		return verifyScopesAndAuth(false, scopes);
	}
	public final Response verifyScopesAndAuth(boolean canUseAsUnauthedUser, Scope... scopes) {
		if (!hasAccessToken()) {
			return RespError.NO_AUTH;
		}
		else if (!hasRefreshToken() && !canUseAsUnauthedUser) {
			return RespError.INVALID_TOKEN;
		}
		else if (hasRefreshToken() && !hasScopes(scopes)) {
			return RespError.INSUFFICIANT_SCOPE;
		}
		return new Response();
	}
}