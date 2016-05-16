package com.park.spring.skeleton.front.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.park.spring.skeleton.base.common.MstMasterCode;
import com.park.spring.skeleton.base.util.SecurityUtil;
import com.park.spring.skeleton.front.service.impl.UserDetailServiceImpl;

/**
 * Spring Security関連設定
 * Login・Access制限などを設定する。
 * @author park
 */
@Configuration
//@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*
	 * ログイン成功、失敗、ロックアウトの為のHandler [LoginAuthenticationProvider.java]
	 */
	@Autowired @Qualifier("authenticationProvider")
	DaoAuthenticationProvider authenticationProvider;
	
	
	@Autowired DataSource dataSource;
	@Autowired UserDetailServiceImpl userDetailsService;
	
	/**
	 * パスワードの暗号化方法を指定する
	 * encoder, Salt..
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		
		/*
		固定文字列の場合
		SystemWideSaltSource salt = new SystemWideSaltSource();
        salt.setSystemWideSalt("username");
        */
		ReflectionSaltSource salt = new ReflectionSaltSource();
		salt.setUserPropertyToUse("username");
		
		//DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setSaltSource(salt);
		
		auth.authenticationProvider(authenticationProvider);
	}
	
	/**
	 * パスワードの暗号化方式を決める
	 * @return　ShaPasswordEncoder
	 */
	@Bean
	public ShaPasswordEncoder passwordEncoder() {
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(SecurityUtil.STRENGTH);
		shaPasswordEncoder.setIterations(SecurityUtil.ITERATIONS);
		return shaPasswordEncoder;
	}
	
	/**
	 * Spring Securityの全体の設定を行う
	 * @param http 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.authorizeRequests()
				.antMatchers("/*", "/css/**", "/js/**", "/fonts/**", "/font-awesome/**", "/img/**", 
						"/login/**", "/template/**", "/signup/*", "/common/*"
						).permitAll()
				.anyRequest().permitAll()
				.antMatchers("/mypage-normal/**").hasRole(MstMasterCode.MEM_TYPE_NORMAL+"")
				.antMatchers("/mypage-special/**").hasRole(MstMasterCode.MEM_TYPE_SPECIAL+"")
				
				.and()
			.formLogin()
				.loginPage("/")
				.defaultSuccessUrl("/mypage/")
				.loginProcessingUrl("/login/check")
				.usernameParameter("username")
				.passwordParameter("password")
				.and()
			.logout()
				.logoutRequestMatcher(logoutRequestMatcher())
				.logoutSuccessUrl("/?logout")
				.invalidateHttpSession(true)
				.and()
			.csrf()
				.and()
			.exceptionHandling()
				// ajax -> 401
				.defaultAuthenticationEntryPointFor(
						ajaxAuthenticationEntryPoint(), ajaxRequestMatcher());
			//rememberMe使用しないため
			//	.and()
			//.rememberMe()
			//	.tokenRepository(persistentTokenRepository())
			//.tokenValiditySeconds(1209600);
		
		/*
		 * ファイルアップロード時にiFrameを使うために
		 * XFrameOptionsMode.SAMEORIGINに指定
		 * Default DENY
		 */
		http
			.headers()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN));
		
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

	@Bean
	public AuthenticationEntryPoint ajaxAuthenticationEntryPoint() {
		return new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request,
					HttpServletResponse response,
					AuthenticationException authException) throws IOException,
					ServletException {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		};
	}

	@Bean
	public RequestMatcher ajaxRequestMatcher() {
		return new RequestHeaderRequestMatcher("X-Requested-With",
				"XMLHttpRequest");
	}

	@Bean
	public RequestMatcher logoutRequestMatcher() {
		return new AntPathRequestMatcher("/logout");
	}
}
