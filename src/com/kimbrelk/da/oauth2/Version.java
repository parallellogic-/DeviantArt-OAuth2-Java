package com.kimbrelk.da.oauth2;

public final class Version {
	private int mMajor;
	private int mMinor;
	
	public Version(int major, int minor) {
		mMajor = major;
		mMinor = minor;
	}
	
	public final int getMajor() {
		return mMajor;
	}
	
	public final int getMinor() {
		return mMinor;
	}
}
