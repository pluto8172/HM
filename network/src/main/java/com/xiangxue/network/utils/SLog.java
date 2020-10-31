package com.xiangxue.network.utils;

import android.util.Log;

public class SLog {
	private final static String TAG=SLog.class.getName();
	public final static boolean debug = true;

    /**
	 * log输出
	 */
	public static void d(String msg){
		Log.d(TAG, msg);
	}
	public static void e(String msg){
		Log.e(TAG, msg);
	}
	public static void v(String msg){
		for(int i=0;i<msg.length();i+=2000){
			if(i+2000<msg.length()){
				Log.v(TAG,msg.substring(i, i+2000));
			} else {
				Log.v(TAG,msg.substring(i));
			}
		}
	}
	public static void w(String msg){
		Log.w(TAG, msg);
	}
	public static void i(String msg){
		Log.i(TAG, msg);
	}
}
