package pl.net.szafraniec.NFCKey;

import android.util.Log;

public class log {
	private static final String LOG_TAG = R.class.getPackage().getName();
	private static final boolean AppDEBUG = false;

	public static void D(String message) {

		if (AppDEBUG) {
			Log.d(LOG_TAG, message);
		}
	}

	public static void E(String message) {
		Log.e(LOG_TAG, message);
	}

	public static void W(String message) {
		Log.w(LOG_TAG, message);
	}

}
