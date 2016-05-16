・概要<br/>
 このプロジェクトはSpringを利用したサンプルプロジェクトです。<br/>
 データベースへの接続はQueryDSLを利用していてcom.park.spring.skeleton.base.dao.impl.AbstractQueryDslDaoでコントロールしています。<br/>
 また、複数のデータベースがある場合は切り替えが可能になっていて、<br/>
 その切り替えは以下のファイルを参照してください。<br/>
 com.park.spring.skeleton.base.common.ExecutionLoggingAspect<br/>
 com.park.spring.skeleton.base.handler.datasource.*<br/>
 com.park.spring.skeleton.front.config.JpaConfiguration<br/>
 
 ログイン処理は<br/>
 com.park.spring.skeleton.front.config.WebSecurityConfig<br/>
 com.park.spring.skeleton.front.service.impl.UserDetailServiceImpl<br/>
 com.park.spring.skeleton.front.web.handler.LoginAuthenticationProvider<br/>
 を参照してください。<br/>
 <br/><br/>
 
・主なフレームワーク<br/>
 spring-boot-*-1.3.5<br/><br/>
 
 thymeleaf-2.1.4<br/>
 spring-*-4.2.6<br/>
 spring-security-*-4.0.4<br/>
 queryDSL-*-3.7.2<br/>
 　      <br/><br/

・プロジェクト構成<br/>
 sgqs_base			共通モジュール格納「util, dao, dto」<br/>
 sgqs_batch			バッチプロジェクト(使われてない)<br/>
 sgqs_front			フロントWEBプロジェクト(ログイン、データの追加、修正ができるサンプルプロジェクト)<br/>
 sgqs_manager		フロントを管理するマネージャーWEBプロジェクト(使われてない)<br/>
 spring-gradle-queryDSL-skeleton Gradleプロジェクト<br/>
<br/><br/>
・プロジェクトRUNまでの流れ<br/>
 (私はSTS3.6.1を使っていますので、この基準で説明します。)<br/>
 - IDEへのプロジェクトImport<br/>
   File -> Import -> Gradle -> Gradle Project -> Browse...(Select Project) -> Build Model -> Select All -> Finish<br/><br/>
 
 - データベース初期化<br/>
   spring-gradle-queryDSL-skeletonのdocument -> sql -> init.sql　参照<br/><br/>
 
 - Gradle Command<br/>
   :Eclipse 更新時<br/>
    gradlew.bat clean mkdirs eclipse<br/><br/>
    
   :コード生成、Build時<br/>
    gradlew.bat clean build<br/><br/>
    
   :リアルサーバ用のBuild時<br/>
    gradlew.bat -Penv=prod clean build<br/><br/>
 
   *ディレクトリの生成が必要なときには　mkdirs　タスク<br/><br/>

 - Run <br/>
   .該当のプロジェクトの上でマウス右 -> Run As -> Spring Boot App<br/><br/>
   
 - Login<br/>
   ID: test1234<br/>
   PW: test1234<br/>
   <br/><br/>
   
 <img border="0" src="https://2.bp.blogspot.com/-IiiXTvyK4ig/VzkhTVC2DCI/AAAAAAAAAac/AsSXYpvpU0gvZAUy1dUisCZlc1JcDpbVwCLcB/s320/login.png" /><br/>
 <img border="0" src="https://2.bp.blogspot.com/-vf95T7BUE90/VzkhTlGpR9I/AAAAAAAAAag/FItxPJJci1UkFaQDHP1U8aqn5__OL28HQCLcB/s320/main.png" /><br/>
 <img border="0" src="https://2.bp.blogspot.com/-Qeurd0IV6Pw/VzkhTXLhkgI/AAAAAAAAAaY/j3JBBog2UTguCf6QtFFKla3Rdem_BsHsACLcB/s320/modify.png" /><br/>
 
 
