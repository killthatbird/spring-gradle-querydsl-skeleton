package com.park.spring.skeleton.front.config;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Context;
import org.apache.catalina.session.StandardManager;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.park.spring.skeleton.base.common.GlobalException;

/**
 * MVCの全体を設定する
 * @author park
 */
@EnableWebMvc
@ComponentScan(basePackages = {"com.park.spring.skeleton.front", "com.park.spring.skeleton.base.dao", "com.park.spring.skeleton.base.validator", "com.park.spring.skeleton.base.common"})
@Import({JpaConfiguration.class, JavaMailConfiguration.class})
@PropertySource({ "classpath:config.properties" })
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment env;

	// **********************************************************************
	// URLとVIEWページのマッピング設定
	// **********************************************************************
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/error/error").setViewName("error/error");
		//registry.addViewController("/info/voice.html").setViewName("info/voice");
		//registry.addViewController("/login/").setViewName("login/login");
	}

	// **********************************************************************
	// ページング処理のDefault値設定
	// **********************************************************************
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(new PageRequest(0, 20)); 	// Default page
																// list 20
		resolver.setPageParameterName("page.page");
		resolver.setSizeParameterName("page.size");
		argumentResolvers.add(resolver);
	}
	
	// **********************************************************************
	// リソースのURLと実際の経路のマッチングやキャッシュなどを指定
	// APサーバの指定で実際はWEBサーバ側で処理を行うこと
	// **********************************************************************
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/").setCachePeriod(31556926);
		registry.addResourceHandler("/font-awesome/**").addResourceLocations("classpath:/static/font-awesome/").setCachePeriod(31556926);
		registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/").setCachePeriod(31556926);
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/").setCachePeriod(31556926);
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/").setCachePeriod(31556926);
		registry.addResourceHandler("/pdf/**").addResourceLocations("classpath:/static/pdf/").setCachePeriod(31556926);
		
		//set Upload Path
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + env.getProperty("upload.path") + File.separatorChar);
	    
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	
	// **********************************************************************
	// 国際化関連設定
	// **********************************************************************
	// Provides internationalization of messages
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasename("messages");
		return source;
	}
	
	//Validatorのメッセージ
	@Override
	public Validator getValidator() {

		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}
	
	// **********************************************************************
	// Exception Handler追加
	// 予期してないエラーが発生した場合
	// **********************************************************************
	@Bean
	public GlobalException handlerExceptionResolver() {
		GlobalException handler = new GlobalException();
		return handler;
	}
	
	
	// **********************************************************************
	// エンコード設定
	// **********************************************************************
	// JSON(@RestController、@ResponseBody)時の文字化け対応
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}
	
	// リクエストパラメタのエンコードを設定する
	@Bean
	public FilterRegistrationBean getFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new CharacterEncodingFilter());
		return filterRegistrationBean;
	}
	
	private static class CharacterEncodingFilter implements Filter {
		protected String encoding;
		public void init(FilterConfig filterConfig) throws ServletException {
			encoding = "UTF-8";
		}
		public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			request.setCharacterEncoding(encoding);
			filterChain.doFilter(servletRequest, servletResponse);
		}
		public void destroy() { encoding = null; }
	}
	

	@Bean
	public TomcatEmbeddedServletContainerFactory tomcatFactory() {
		return new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
					Tomcat tomcat) {
				tomcat.enableNaming();
				return super.getTomcatEmbeddedServletContainer(tomcat);
			}
		};
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				if (container instanceof TomcatEmbeddedServletContainerFactory) {
					TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory = (TomcatEmbeddedServletContainerFactory) container;
					tomcatEmbeddedServletContainerFactory
							.addContextCustomizers(new TomcatContextCustomizer() {
								@Override
								public void customize(Context context) {
									
									/**
									 * Tomcat再起動時にセッション情報保持
									 */
									StandardManager m = new StandardManager();
									m.setPathname(env.getProperty("session.persistence.path"));
									context.setManager(m);

								}
							});
				}
			}
		};
	}

	
	// **********************************************************************
	// ファイルアップロードダウンロード関連設定
	// **********************************************************************
	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("5MB");
		factory.setMaxRequestSize("5MB");
		return factory.createMultipartConfig();
	}
	
	
	// **********************************************************************
	// RMI設定
	// **********************************************************************
	/*
	@Bean(name="testRmi")
	public RmiProxyFactoryBean buyerMatchingService() {
		RmiProxyFactoryBean proxy = new RmiProxyFactoryBean();
		proxy.setServiceInterface(TestRmi.class);
		proxy.setServiceUrl(env.getProperty("http.invoker.test.url"));
		proxy.setLookupStubOnStartup(false);
		proxy.setRefreshStubOnConnectFailure(true);
		return proxy;
	}
	*/
	
}
