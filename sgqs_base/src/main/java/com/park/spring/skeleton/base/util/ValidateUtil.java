package com.park.spring.skeleton.base.util;

import com.google.common.primitives.Longs;

public class ValidateUtil {
	
	/**
	 * 数字かどうかをチェックする Longまで
	 * @param checkVal
	 * @return
	 */
	public static boolean isNumber(String checkVal) {
		if (checkVal == null || Longs.tryParse(checkVal) == null) {
			return false;
		}
		return true;
	}
}
