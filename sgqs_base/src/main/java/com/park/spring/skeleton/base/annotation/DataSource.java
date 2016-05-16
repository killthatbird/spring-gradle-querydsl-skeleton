package com.park.spring.skeleton.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.park.spring.skeleton.base.constants.DataSourceType;

/**
 * <pre>
 * DataSourceのTypeが定義されているAnnotation
 * DataSourceType　参照
 * Default　：　MA_BASE
 * </pre>
 * @author park
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
	DataSourceType value() default DataSourceType.MASTER;
}