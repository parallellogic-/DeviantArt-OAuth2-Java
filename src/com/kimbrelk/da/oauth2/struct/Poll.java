package com.kimbrelk.da.oauth2.struct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Poll {
	private Answer[] mAnswers;
	private int mNumVotes;
	private String mQuestion;
	
	public Poll(JSONObject json) throws JSONException {
		JSONArray jsonAnswers = json.getJSONArray("answers");
		mAnswers = new Answer[jsonAnswers.length()];
		for(int a=0; a<mAnswers.length; a++) {
			mAnswers[a] = new Answer(jsonAnswers.getJSONObject(a));
		}
		mNumVotes = json.getInt("total_votes");
		mQuestion = json.getString("question");
	}
	
	public final Answer[] getAnswers() {
		return mAnswers;
	}
	public final int getTotalVotes() {
		return mNumVotes;
	}
	public final String getQuestion() {
		return mQuestion;
	}
	
	public final class Answer {
		private String mAnswer;
		private int mNumVotes;
		
		public Answer(JSONObject json) throws JSONException {
			mAnswer = json.getString("answer");
			mNumVotes = json.getInt("votes");
		}

		public final String getAnswer() {
			return mAnswer;
		}
		public final int getNumVotes() {
			return mNumVotes;
		}
	}
}