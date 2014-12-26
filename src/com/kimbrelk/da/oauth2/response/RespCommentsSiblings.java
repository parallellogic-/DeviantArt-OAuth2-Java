package com.kimbrelk.da.oauth2.response;

import com.kimbrelk.da.oauth2.struct.Comment;
import com.kimbrelk.da.oauth2.struct.Deviation;
import com.kimbrelk.da.oauth2.struct.Status;
import com.kimbrelk.da.oauth2.struct.User;
import org.json.JSONException;
import org.json.JSONObject;

public class RespCommentsSiblings extends RespComments {
	private Context mContext;
	
	public RespCommentsSiblings(JSONObject json) throws JSONException {
		super(json);
		mContext = new Context(json);
	}
	
	public final Context getContext() {
		return mContext;
	}
	
	public final static class Context {
		public enum Type {
			COMMENT,
			DEVIATION,
			NONE,
			PROFILE,
			STATUS
		}
		
		private Deviation mItemDeviation;
		private User mItemProfile;
		private Status mItemStatus;
		private Type mItemType;
		private Comment mParent;
		
		public Context(JSONObject json) throws JSONException {
			mItemType = Type.NONE;
			if (json.has("item_deviation")) {
				mItemDeviation = new Deviation(json.getJSONObject("item_deviation"));
				mItemType = Type.DEVIATION;
			}
			if (json.has("item_profile")) {
				mItemProfile = new User(json.getJSONObject("item_status"));
				mItemType = Type.PROFILE;
			}
			if (json.has("item_status")) {
				mItemStatus = new Status(json.getJSONObject("item_status"));
				mItemType = Type.STATUS;
			}
			if (json.has("parent")) {
				mParent = new Comment(json.getJSONObject("parent"));
				mItemType = Type.COMMENT;
			}
		}

		public final Deviation getDeviation() {
			return mItemDeviation;
		}
		public final Comment getParentComment() {
			return mParent;
		}
		public final User getProfile() {
			return mItemProfile;
		}
		public final Status getStatus() {
			return mItemStatus;
		}
		public final Type getType() {
			return mItemType;
		}
	}
}