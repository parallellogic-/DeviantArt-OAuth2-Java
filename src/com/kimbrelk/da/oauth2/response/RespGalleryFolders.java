package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.GalleryFolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RespGalleryFolders extends Response {
	private GalleryFolder[] mResults;
	
	public RespGalleryFolders(JSONObject json) throws JSONException {
		super();
		JSONArray jsonResults = json.getJSONArray("results");
		mResults = new GalleryFolder[jsonResults.length()];
		for(int a=0; a<mResults.length; a++) {
			mResults[a] = new GalleryFolder(jsonResults.getJSONObject(a));
		}
	}
	
	public final GalleryFolder[] getFolders() {
		return mResults;
	}
}