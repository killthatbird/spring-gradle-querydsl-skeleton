package com.park.spring.skeleton.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.park.spring.skeleton.base.exception.ApplicationException;

/**
 * 日付に関する Utils
 * @author park
 *
 */
public class DateUtil {
	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public final static String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	
	/**
	 * ログ、などに使われる
	 */
	public final static String DEFAULT_DETAIL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/**
	 * Dateタイプを
	 * yyyy-MM-dd形式の文字列に変換する
	 * @param date Dateタイプ 
	 * @return
	 */
	public static String toString(Date date) {
		return toString(date, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * Dateタイプを指定形式に変換する
	 * @param strDate 		日付String
	 * @param dateFormat	日付のフォーマット
	 * @return
	 */
	public static String toString(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}
	
	/**
	 * yyyy-MM-dd形式の日付文字列を
	 * Dateに変換する
	 * @param strDate yyyy-MM-dd形式の文字列 
	 * @return
	 * @throws ParseException 形式が間違っている場合
	 */
	public static Date toDate(String strDate) throws ApplicationException {
		return toDate(strDate, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 日付StringをDateに変換する
	 * @param strDate 		日付String
	 * @param dateFormat	日付の形式
	 * @return
	 * @throws ParseException 形式が一致してない場合
	 */
	public static Date toDate(String strDate, String dateFormat) throws ApplicationException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return sdf.parse(strDate);
		} catch (ParseException pe) {
			throw new ApplicationException("DataType Exception", pe);
		}
	}
	
	/**
	* 日付の差分日数を取得。
	* @param dateFrom 開始日付
	* @param dateTo   終了日付
	* @return int 差分日数
	*/
	public static int diffDays(Date dateFrom, Date dateTo){
		// 日付をlong値に変換
		long dateTimeFrom = dateFrom.getTime();
		long dateTimeTo   = dateTo.getTime();
		
		// 差分の日数を計算
		final long DAY_MILLISECONDS = (1000 * 60 * 60 * 24 );
		long diff = dateTimeTo - dateTimeFrom ;
		int dayDiff = (int)(diff / DAY_MILLISECONDS);
		
		return dayDiff;
	}
	
	/**
	* 時間の差分を取得。
	* @param dateFrom 開始日付
	* @param dateTo   終了日付
	* @return int 差分時間
	*/
	public static int diffTimes(Date dateFrom, Date dateTo){
		// 日付をlong値に変換
		long dateTimeFrom = dateFrom.getTime();
		long dateTimeTo   = dateTo.getTime();
		
		// 差分の時間を計算
		final long TIME_MILLISECONDS = (1000 * 60 * 60);
		long diff = dateTimeTo - dateTimeFrom ;
		int dayDiff = (int)(diff / TIME_MILLISECONDS);
		
		return dayDiff;
	}
	
	/**
	* 日付を追加する
	* @param date 日付
	* @param cnt 追加する日付
	* @return Date 結果
	*/
	public static Date addDay(Date date, int cnt){
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, cnt);
		return c.getTime();
	}
	
	/**
	* 日付を減算する。
	* @param date 日付
	* @param cnt 減算する日付
	* @return Date 結果
	*/
	public static Date minusDay(Date date, int cnt){
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, (cnt < 0 ? cnt : cnt * -1));
		return c.getTime();
	}
	
	/**
	 * その日の最後の時分秒のＤａｔｅを取得する
	 * @param date
	 * @return
	 */
	public static Date toLastTime(Date date) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	
	/**
	 * その日の最初の時分秒のＤａｔｅを取得する
	 * @param date
	 * @return
	 */
	public static Date toFirstTime(Date date) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
}
