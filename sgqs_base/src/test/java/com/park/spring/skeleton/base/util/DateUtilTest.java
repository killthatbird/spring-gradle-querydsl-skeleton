package com.park.spring.skeleton.base.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DateUtilTest {
	
	@Test
	public void testGetDate() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 9, 11);
		
		// 日付を作成します。
		Date date = DateUtil.toDate("2016-10-11");
	    
	    assertEquals(DateUtil.toString(cal.getTime()), DateUtil.toString(date));
	}
	
	@Test
	public void testDiffDays30() {
		final int diffDay = 30;
		
	    Date dateTo = null;
	    Date dateFrom = null;
	 
	    // 日付を作成します。
        dateFrom = DateUtil.toDate("2015-08-01");
        dateTo = DateUtil.toDate("2015-08-31");
	    
	    

		assertEquals(diffDay, DateUtil.diffDays(dateFrom, dateTo));
	}
	
	
	@Test
	public void testDiffDays365() {
		final int diffDay = 365;
		
	    Date dateTo = null;
	    Date dateFrom = null;
	 
	    // 日付を作成します。
        dateFrom = DateUtil.toDate("2015-01-01");
        dateTo = DateUtil.toDate("2016-01-01");


		assertEquals(diffDay, DateUtil.diffDays(dateFrom, dateTo));
	}
	
	@Test
	public void testDiffDays0() {
		final int diffDay = 0;
		
	    Date dateTo = null;
	    Date dateFrom = null;
	 
	    // 日付を作成します。
        dateFrom = DateUtil.toDate("2016-01-01");
        dateTo = DateUtil.toDate("2016-01-01");
	 
		assertEquals(diffDay, DateUtil.diffDays(dateFrom, dateTo));
	}
	
	@Test
	public void testDiffTimes0() {
		
		final int diffDay = 0;
		
	    Date dateTo = null;
	    Date dateFrom = null;
	 
	    // 日付を作成します。
        dateFrom = DateUtil.toDate("2016-01-01 01:00:00", "yyyy-MM-dd HH:mm:ss");
        dateTo = DateUtil.toDate("2016-01-01 01:00:00", "yyyy-MM-dd HH:mm:ss");
    
		assertEquals(diffDay, DateUtil.diffTimes(dateFrom, dateTo));
	}
	
	@Test
	public void testDiffTimes15() {
		final int diffDay = 15;
		
	    Date dateTo = null;
	    Date dateFrom = null;
	 
	    // 日付を作成します。
        dateFrom = DateUtil.toDate("2016-01-01 01:00:00", "yyyy-MM-dd HH:mm:ss");
        dateTo = DateUtil.toDate("2016-01-01 16:00:00", "yyyy-MM-dd HH:mm:ss");
    
	    
		assertEquals(diffDay, DateUtil.diffTimes(dateFrom, dateTo));
	}
	
	@Test
	public void testDiffTimes24() {
		final int diffDay = 24;
		
	    Date dateTo = null;
	    Date dateFrom = null;
	 
	    // 日付を作成します。
        dateFrom = DateUtil.toDate("2016-01-01 01:00:00", "yyyy-MM-dd HH:mm:ss");
        dateTo = DateUtil.toDate("2016-01-02 01:00:00", "yyyy-MM-dd HH:mm:ss");
	    
	    
		assertEquals(diffDay, DateUtil.diffTimes(dateFrom, dateTo));
	}
	
	@Test
	public void testDiffTimes23() {
		final int diffDay = 23;
		
	    Date dateTo = null;
	    Date dateFrom = null;
	 
	    // 日付を作成します。
        dateFrom = DateUtil.toDate("2016-01-01 01:00:00", "yyyy-MM-dd HH:mm:ss");
        dateTo = DateUtil.toDate("2016-01-01 24:00:00", "yyyy-MM-dd HH:mm:ss");
	    
		assertEquals(diffDay, DateUtil.diffTimes(dateFrom, dateTo));

		
		// 日付を作成します。
        dateFrom = DateUtil.toDate("2016-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        dateTo = DateUtil.toDate("2016-01-01 23:00:00", "yyyy-MM-dd HH:mm:ss");
	    
		assertEquals(diffDay, DateUtil.diffTimes(dateFrom, dateTo));
	}
}
