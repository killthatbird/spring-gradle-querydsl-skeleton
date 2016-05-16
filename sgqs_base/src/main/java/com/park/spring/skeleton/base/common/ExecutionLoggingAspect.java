package com.park.spring.skeleton.base.common;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.park.spring.skeleton.base.annotation.DataSource;
import com.park.spring.skeleton.base.constants.DataSourceType;
import com.park.spring.skeleton.base.handler.datasource.ContextHolder;

/**
 * Service,Component前後に処理を挟む
 * データソースを操作するために使われている
 * @author park
 */
@Aspect
@Component
@Order(value = 2)
public class ExecutionLoggingAspect implements InitializingBean {
	private final static Logger logger = LoggerFactory.getLogger(ExecutionLoggingAspect.class);
	

	@Around("( execution(* com.park.spring.skeleton.*.service.*Service.*(..))  ) && !execution(* com.park.spring.skeleton.*.service.impl.UserDetailServiceImpl.*(..))")
	public Object doServiceProfiling(ProceedingJoinPoint joinPoint)
			throws Throwable {

		logger.debug("@Service start");

		// 設定されているAnnotationを確認するために、現在のmethodを読みこむ
		final String methodName = joinPoint.getSignature().getName();
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		if (method.getDeclaringClass().isInterface()) {
			method = joinPoint.getTarget().getClass().getDeclaredMethod(methodName, method.getParameterTypes());
		}
		// Annotationを読みこむ
		DataSource dataSource = (DataSource) method.getAnnotation(DataSource.class);

		if (dataSource != null) {
			// 該当の「Method」に dataSource関連設定(DataSourceのannotation)がある場合は、 該当のValueの値を設定する
			ContextHolder.setDataSourceType(dataSource.value());
		} else {
			//　特に設定されてない場合はDefaultで「master」を指定する。
			ContextHolder.setDataSourceType(DataSourceType.MASTER);
			
			
			/*
			// もし、Method毎に処理するデータベース分ける場合は以下のように対応する
			// get*, select* の場合は slave, それ以外の場合は MASTER
			if (!(method.getName().startsWith("get") || method.getName()
					.startsWith("select"))) {
				ContextHolder.setDataSourceType(DataSourceType.MASTER);
			}*/
		}
		logger.debug("DataSource ===> " + ContextHolder.getDataSourceType());

		Object returnValue = joinPoint.proceed();
		ContextHolder.clearDataSourceType();

		logger.debug("@Service end");

		return returnValue;

	}


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}