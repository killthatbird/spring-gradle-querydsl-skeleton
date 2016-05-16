package com.park.spring.skeleton.front.config;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.collect.ImmutableMap;
import com.park.spring.skeleton.base.constants.DataSourceType;
import com.park.spring.skeleton.base.handler.datasource.RoutingDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * JPAのデータベースの接続の設定
 * @author park
 */
@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:connection.properties" })
public class JpaConfiguration {

	@Autowired
	private Environment env;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
		PropertySourcesPlaceholderConfigurer configHolder = new PropertySourcesPlaceholderConfigurer();
		return configHolder;
	}
	
	// **********************************************************************
	// DataSource (master)
	// **********************************************************************
	public DataSource masterDataSource() {
		
		HikariConfig config = new HikariConfig();

		config.setDataSourceClassName(env.getProperty("dataSource.className"));
		config.addDataSourceProperty("serverName", env.getProperty("dataSource.serverName"));
		config.addDataSourceProperty("port", env.getProperty("dataSource.port"));
		config.addDataSourceProperty("databaseName", env.getProperty("dataSource.databaseName"));
		config.addDataSourceProperty("user", env.getProperty("dataSource.user"));
		config.addDataSourceProperty("password", env.getProperty("dataSource.password"));
		
		config.addDataSourceProperty("zeroDateTimeBehavior", "convertToNull");
		config.addDataSourceProperty("useUnicode", "true");
		config.addDataSourceProperty("characterEncoding", "utf8");
		config.addDataSourceProperty("autoReconnect", "true");
		
		config.addDataSourceProperty("cachePrepStmts", env.getProperty("dataSource.cachePrepStmts"));
		config.addDataSourceProperty("prepStmtCacheSize", env.getProperty("dataSource.prepStmtCacheSize"));
		config.addDataSourceProperty("prepStmtCacheSqlLimit", env.getProperty("dataSource.prepStmtCacheSqlLimit"));
		config.addDataSourceProperty("useServerPrepStmts", env.getProperty("dataSource.useServerPrepStmts"));
		
		int minConnection = Integer.parseInt(env.getProperty("dataSource.minimumIdle"));
		config.setMinimumIdle(minConnection);
		int maxConnection = Integer.parseInt(env.getProperty("dataSource.maximumPoolSize"));
		config.setMaximumPoolSize(maxConnection);
		
		config.setJdbc4ConnectionTest(env.getProperty("dataSource.jdbc4ConnectionTest").equals("true"));
		//config.setConnectionTestQuery(env.getProperty("dataSource.testQuery"));
		config.setConnectionInitSql(env.getProperty("dataSource.testQuery"));
		
		HikariDataSource dataSource = new HikariDataSource(config);
		
		return dataSource;
	}
	
	// **********************************************************************
	// DataSource (slave)
	// **********************************************************************
	public DataSource slaveDataSource() {
		
		HikariConfig config = new HikariConfig();
		
		config.setDataSourceClassName(env.getProperty("dataSource.className"));
		config.addDataSourceProperty("serverName", env.getProperty("dataSource.slave.serverName"));
		config.addDataSourceProperty("port", env.getProperty("dataSource.slave.port"));
		config.addDataSourceProperty("databaseName", env.getProperty("dataSource.slave.databaseName"));
		
		config.addDataSourceProperty("user", env.getProperty("dataSource.user"));
		config.addDataSourceProperty("password", env.getProperty("dataSource.password"));
		
		config.addDataSourceProperty("zeroDateTimeBehavior", "convertToNull");
		config.addDataSourceProperty("useUnicode", "true");
		config.addDataSourceProperty("characterEncoding", "utf8");
		config.addDataSourceProperty("autoReconnect", "true");
		
		config.addDataSourceProperty("cachePrepStmts", "false");
		config.addDataSourceProperty("useServerPrepStmts", "false");
		
		/*
		メモリ消費を抑える。
		config.addDataSourceProperty("cachePrepStmts", env.getProperty("dataSource.cachePrepStmts"));
		config.addDataSourceProperty("prepStmtCacheSize", env.getProperty("dataSource.prepStmtCacheSize"));
		config.addDataSourceProperty("prepStmtCacheSqlLimit", env.getProperty("dataSource.prepStmtCacheSqlLimit"));
		config.addDataSourceProperty("useServerPrepStmts", env.getProperty("dataSource.useServerPrepStmts"));
		*/
		
		
		int minConnection = Integer.parseInt(env.getProperty("dataSource.minimumIdle"));
		config.setMinimumIdle(minConnection);
		int maxConnection = Integer.parseInt(env.getProperty("dataSource.maximumPoolSize"));
		config.setMaximumPoolSize(maxConnection);
		
		config.setJdbc4ConnectionTest(env.getProperty("dataSource.jdbc4ConnectionTest").equals("true"));
		//config.setConnectionTestQuery(env.getProperty("dataSource.testQuery"));
		config.setConnectionInitSql(env.getProperty("dataSource.testQuery"));
		
		HikariDataSource dataSource = new HikariDataSource(config);
		
		return dataSource;
	}
	
	// **********************************************************************
	// EntityManagerFactory
	// **********************************************************************
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(new String[] { "com.park.spring.skeleton.base.entity" });
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(hibernateProperties());
		
		/*
		ConstraintValidator 中で　RepositoryをInjectionするため
		Map<String, Object> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put("javax.persistence.validation.factory", localValidatorFactoryBean());
        em.setJpaPropertyMap(jpaPropertyMap);
        */
        
		return em;
	}
	
	// **********************************************************************
	// AbstractRoutingDataSource 継承してDataSourceを選択できるようにする
	// **********************************************************************
	@Bean
	@Order(value = 2)
	public RoutingDataSource dataSource() {
		RoutingDataSource targetDataSources = new RoutingDataSource();
		
		targetDataSources.setTargetDataSources(
			ImmutableMap.of(DataSourceType.MASTER, masterDataSource(),
							DataSourceType.SLAVE, slaveDataSource())
		);
		
		//基本的にma_baseを見るようにDefault
		targetDataSources.setDefaultTargetDataSource(masterDataSource());
		return targetDataSources;
	}
	
	/*
	ConstraintValidator 中で　RepositoryをInjectionするため
	@Bean
	public javax.validation.Validator localValidatorFactoryBean() {
	   return new LocalValidatorFactoryBean();
	}
	*/
	
	// **********************************************************************
	// Transaction annotationを使えるように修正
	// **********************************************************************
	@Bean
	@Order(value = 3)
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}
	
	/**
	 * Data access技術に依存しないException Handleringが可能。
	 * 汎用のExceptionに変換してくれる。
	 * 
	 * CannotAcquireLockException ロックの取得失敗
	 * ConcurrencyFailureException 同時実行
	 * DataAccessResourceFailureException データソースと接続失敗
	 * DataIntegrityViolationException  整合性違反
	 * DeadlockLoserDataAccessException デッドロック
	 * EmptyResultDataAccessException データが存在しない
	 * IncorrectResultSizeDataAccessException レコードサイズが正しくない
	 * OptimisticLockingFailureException  楽観的ロックに失敗
	 * perissionDeniedDateAccessException 権限 
	 * @return
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	

	Properties hibernateProperties() {
		return new Properties() {
			private static final long serialVersionUID = -7718263191124882805L;

			{
				setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
				setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
				setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
				
				setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
				setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
				
				// NOTE : Hibernate static analysis tool
				// hibernate.generate_statistics————————– true
				// hibernate.cache.use_structured_entrie————– true
				setProperty("hibernate.generate_statistics",Boolean.TRUE.toString());
				setProperty("hibernate.cache.use_structured_entire",Boolean.TRUE.toString());
				// properties.setProperty("hibernate.cache.use_query_cache", "true");
			}
		};
	}
	
}