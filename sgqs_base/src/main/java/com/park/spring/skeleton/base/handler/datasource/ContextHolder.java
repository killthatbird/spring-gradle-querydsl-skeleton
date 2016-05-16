package com.park.spring.skeleton.base.handler.datasource;

import com.park.spring.skeleton.base.constants.DataSourceType;


/**
 * ThreadLocalを利用して現在のDatasourceを判断する。
 * @author park
 */
public class ContextHolder {

	private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<DataSourceType>();

	public static void setDataSourceType(DataSourceType dataSourceType) {
		contextHolder.set(dataSourceType);
	}

	public static DataSourceType getDataSourceType() {
		return contextHolder.get();
	}

	public static void clearDataSourceType() {
		contextHolder.remove();
	}

}