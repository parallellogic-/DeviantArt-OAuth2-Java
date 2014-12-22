package com.kimbrelk.da.oauth2;

import com.kimbrelk.da.oauth2.response.RespBrowseDailydeviations;
import com.kimbrelk.da.oauth2.response.RespError;
import com.kimbrelk.da.oauth2.response.RespStashPublishUserdata;
import com.kimbrelk.da.oauth2.response.RespStashSpace;
import com.kimbrelk.da.oauth2.response.RespToken;
import com.kimbrelk.da.oauth2.response.RespUserDamntoken;
import com.kimbrelk.da.oauth2.response.RespUserWhoami;
import com.kimbrelk.da.oauth2.response.RespUserWhois;
import com.kimbrelk.da.oauth2.response.Response;
import com.kimbrelk.da.oauth2.struct.User;
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
				return new RespBrowseDailydeviations(json);
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
	/**
	 * Update the authorized user's profile
	 * @param userIsArtist -1(ignore), 0(false), 1(true)
	 * @param artistLevel See ArtistLevel, may be -1 to ignore
	 * @param artistSpecialty See ArtistSpecialty, may be -1 to ignore
	 * @param realName May be null to ignore
	 * @param tagline May be null to ignore
	 * @param countryid See /user/profile/update dev docs for values, may be -1 to ignore
	 * @param website May be null to ignore
	 * @param bio May be null to ignore
	 * @return Response from dA
	 */
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