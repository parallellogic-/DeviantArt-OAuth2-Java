package com.kimbrelk.da.oauth2.struct;

public final class License {
	private boolean mAllowsAttribution;
	private boolean mAllowsCommercialUse;
	private boolean mAllowsModification;

	public License() {
		
	}
	public License(boolean allowsAttribution, boolean allowsCommercialUse, boolean allowsModification) {
		mAllowsAttribution = allowsAttribution;
		mAllowsCommercialUse = allowsCommercialUse;
		mAllowsModification = allowsModification;
	}

	public final boolean allowsAttribution() {
		return mAllowsAttribution;
	}
	public final boolean allowsCommercialUse() {
		return mAllowsCommercialUse;
	}
	public final boolean allowsModification() {
		return mAllowsModification;
	}
}