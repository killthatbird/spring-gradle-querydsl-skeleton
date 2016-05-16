package com.park.spring.skeleton.front.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.conditionalcomments.dialect.ConditionalCommentsDialect;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Thymeleafの設定ファイル
 * @author park
 */
@Configuration
public class ThymeleafConfig {
	
	@Bean
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setPrefix("/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML5");
		resolver.setOrder(1);
		resolver.setCharacterEncoding("UTF-8");
		
		//Test
		//resolver.setCacheable(false);
		
		return resolver;
	}
	
	/**
	 * ThymeleafのDialect
	 * IEのHack対応、コメントの中にもThymeleaf使えるようにする。
	 * <!--[if IE]>
	 * <![endif]-->
	 * 
	 * @return
	 */
	@Bean
	public ConditionalCommentsDialect conditionalCommentDialect() {
		return new ConditionalCommentsDialect();
	}
	
}