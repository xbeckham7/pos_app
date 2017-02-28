package com.example.xuyiwei.myapplication.util;

import android.util.Log;

public class LogCat {

	private static final boolean VERBOSE_ISLOG = true;
	private static final boolean DEBUG_ISLOG = true;
	private static final boolean INFO_ISLOG = true;
	private static final boolean WARN_ISLOG = true;
	private static final boolean ERROR_ISLOG = true;

	private static final String DEFAULT_TAG = "SSLTest";

	public static void v(String message) {

		if (VERBOSE_ISLOG)
			Log.v(DEFAULT_TAG, message);
	}

	public static void v(String tag, String message) {

		if (VERBOSE_ISLOG)
			Log.v(tag, message);
	}

	public static void d(String message) {

		if (DEBUG_ISLOG)
			Log.d(DEFAULT_TAG, message);
	}

	public static void d(String tag, String message) {

		if (DEBUG_ISLOG)
			Log.d(tag, message);
	}

	public static void i(String message) {

		if (INFO_ISLOG)
			Log.i(DEFAULT_TAG, message);
	}

	public static void i(String tag, String message) {

		if (INFO_ISLOG)
			Log.i(tag, message);
	}

	public static void w(String message) {

		if (WARN_ISLOG)
			Log.w(DEFAULT_TAG, message);
	}

	public static void w(String tag, String message) {

		if (WARN_ISLOG)
			Log.w(tag, message);
	}

	public static void e(String message) {

		if (ERROR_ISLOG)
			Log.e(DEFAULT_TAG, message);
	}

	public static void e(String tag, String message) {

		if (ERROR_ISLOG)
			Log.e(tag, message);
	}
}
