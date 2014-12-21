package com.kimbrelk.da.oauth2;

import com.kimbrelk.da.oauth2.response.RespError;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class OAuth2 {
	private final static Version VERSION = new Version(1, 20141204);
	private final static Endpoint_v1 ENDPOINT = new Endpoint_v1();
	
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
		String ret = ENDPOINT.OAUTH2_AUTHORIZE + "?";
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
		if (!hasAccessToken()) {
			return RespError.NO_AUTH;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINT.OAUTH2_REVOKE, params));
		try {
			System.out.println(json.toString(5));
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
		
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINT.OAUTH2_TOKEN, params));
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
							null, 
							Scope.BROWSE);
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
	
	public final Response requestUserDamntoken() {
		if (!hasAccessToken()) {
			return RespError.NO_AUTH;
		}
		if (!hasScopes(Scope.USER)) {
			return RespError.INSUFFICIANT_SCOPE;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINT.USER_DAMNTOKEN, params));
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
	
	public final Response requestStashSpace() {
		if (!hasAccessToken()) {
			return RespError.NO_AUTH;
		}
		if (!hasScopes(Scope.STASH)) {
			return RespError.INSUFFICIANT_SCOPE;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINT.STASH_SPACE, params));
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
	public final Response requestUserWhoami() {
		if (!hasAccessToken()) {
			return RespError.NO_AUTH;
		}
		if (!hasScopes(Scope.USER)) {
			return RespError.INSUFFICIANT_SCOPE;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINT.USER_WHOAMI, params));
		try {
			if (!json.has("error")) {
				return new RespUserWhoami(
					new User(
						json.getString("username"),
						json.getString("userid"),
						null,//json.getString("type"),
						json.getString("usericon")
					)
				);
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
		if (!hasAccessToken()) {
			return RespError.NO_AUTH;
		}
		if (!hasScopes(Scope.BROWSE)) {
			return RespError.INSUFFICIANT_SCOPE;
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
		JSONObject json = requestJSON(Verb.POST, createURL(ENDPOINT.USER_WHOIS, params), postData);
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
		if (!hasAccessToken()) {
			return RespError.NO_AUTH;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", mToken.getToken());
		JSONObject json = requestJSON(Verb.GET, createURL(ENDPOINT.UTIL_PLACEBO, params));
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
		return requestJSON(verb, url, null);
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
}