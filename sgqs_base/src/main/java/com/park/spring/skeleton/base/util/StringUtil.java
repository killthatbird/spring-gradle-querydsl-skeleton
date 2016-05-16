package com.park.spring.skeleton.base.util;

import java.util.Date;

import com.google.common.base.Strings;

public class StringUtil {
	
	/**
	 * 文字列に現在の時間と改行を追加する
	 * @param str
	 * @return
	 */
	public static synchronized String addCurrentDateTimeBr(final String str) {
		
		StringBuilder sb = new StringBuilder();
		sb
			.append("[")
			.append(DateUtil.toString(new Date(), DateUtil.DEFAULT_DETAIL_DATETIME_FORMAT))
			.append("] ")
			.append(Strings.nullToEmpty(str))
			.append("\r\n");
		
		
		return sb.toString();
	}
	
	/**
	 * 文字列の最後の文字を削除する
	 * @param str		対象の文字列
	 * @param suffix	最後の文字
	 * @return
	 */
	public static synchronized String endStringDelete(final String str, final String suffix) {
		if (str != null && str.endsWith(suffix)) {
			int last = str.lastIndexOf(suffix);
			
			return str.substring(0, last);
		}
		return null;
	}
}
