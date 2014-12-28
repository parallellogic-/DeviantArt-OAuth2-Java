package com.kimbrelk.da.oauth2.struct;

public final class Maturity {
	public enum Classification {
		GORE,
		IDEOLOGY,
		LANGUAGE,
		NONE,
		NUDITY,
		SEXUAL
	}
	public enum Level {
		NONE,
		MODERATE,
		STRICT
	}
	
	private Level mLevel;
	private Classification[] mClassifications;

	public Maturity() {
		mLevel = Level.NONE;
	}
	public Maturity(Level level, Classification... classifications) {
		mLevel = level;
		mClassifications = classifications;
	}

	public final Classification[] getClassifications() {
		return mClassifications;
	}
	public final Level getLevel() {
		return mLevel;
	}
}