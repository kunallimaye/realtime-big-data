package com.redhat.example.framework.rule;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.jboss.logging.Logger;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.ReleaseId;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.runtime.StatefulKnowledgeSession;

/**
 * ${jboss.server.base.dir}/ruleディレクトリに配置されたルールファイルを元にKieBaseを作成し、ルールセッションを提供する。<br>
 * ${jboss.server.base.dir}は、例えば、EAPの$JBOSS_HOME/standalone である。
 * 
 * @author mkobayas
 * 
 */
@Startup
@Singleton
public class RuleSessionServiceBean {

	private static final Logger logger = Logger.getLogger(RuleSessionServiceBean.class);
	
	public static final String JNDI_NAME = "java:global/realtime-big-data-application-ear/realtime-big-data-framework/RuleSessionServiceBean";
	
	/**
	 * KieSessionのインスタンスプール
	 */
	private ConcurrentLinkedQueue<Holder> pool;
	
	/**
	 * セッション貸出し管理
	 */
	private ConcurrentHashMap<KieSession, Holder> borrowed;

	/**
	 * 現在利用中のKieのバージョン(ビルドバージョンとは関係ない)
	 */
	private AtomicLong currentVersion;
	
	/**
	 * ビルド済みKieのリリースID
	 */
	private ReleaseId releaseId;
	
	/**
	 * 現在有効なKieBase
	 */
	private KieBase kBase;
	private File ruleBaseDirFile;

	private static final String groupId = "com.redhat.example";
	private static final String artifactId = "rule";
	private static long releaseCounter = 1;
	
	/**
	 * 内部利用するビルド用のPOMテンプレート
	 */
	private static String pomTemplete = "<project xmlns='http://maven.apache.org/POM/4.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd'>"
			+ "<modelVersion>4.0.0</modelVersion>"
			+ "<groupId>com.redhat.example</groupId>"
			+ "<artifactId>rule</artifactId>"
			+ "<version>VERSION</version>" + "</project>";
	
	/**
	 * 初期化時にコンパイルされたルールファイルのパッケージを取り込みます。
	 */
	@PostConstruct
	void init() {
		logger.info("##################### init start ");

		pool = new ConcurrentLinkedQueue<>();
		borrowed = new ConcurrentHashMap<>();
		currentVersion = new AtomicLong(0);
		
		String ruleBaseDir = System.getProperty("jboss.server.base.dir", ".");
		
		ruleBaseDirFile = new File(ruleBaseDir);
		// ディレクトリが見つからない場合はシステムプロパティへ
		if (!ruleBaseDirFile.exists()) {
			ruleBaseDirFile = new File(System.getProperty("jboss.server.base.dir", ".") + "/rule");
		}
		logger.infov("## ruleBaseDir={0}", ruleBaseDir);

		if( !ruleBaseDirFile.exists() ) {
			logger.infov("ルール配置用のディレクトリを作成します。{0}", ruleBaseDirFile);
			ruleBaseDirFile.mkdir();
		}
				
		try {
			// Linux環境でMVELがStrictモードになる問題を回避
			System.setProperty("drools.dialect.mvel.strict", "false");
			
			// KieBaseのインスタンスを取得し、保持します。
			kBase = createNewKBase();
			logKBase();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		logger.info("##################### init end ");
	}


	private KieBase createNewKBase() {
		KieServices kieServices = KieServices.Factory.get();
		KieFileSystem kfs = kieServices.newKieFileSystem();

		// リリース番号
		String next = Long.toString(releaseCounter++);

		// ルールファイル読み込み
		writeAllRule(kfs, ruleBaseDirFile);

		// POM作成
		String pom = pomTemplete.replace("VERSION", next);
		kfs.writePomXML(pom);

		// ルールファイルコンパイル
		KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
		if (kieBuilder.getResults().getMessages(Level.WARNING).size() > 0) {
			logger.warnv("kieBuilder warn!: {0}", kieBuilder.getResults().getMessages(Level.WARNING));
		}
		if (kieBuilder.getResults().getMessages(Level.ERROR).size() > 0) {
			logger.errorv("kieBuilder error!: {0}", kieBuilder.getResults().getMessages(Level.ERROR));
			// エラーが有る場合はリリースしない。
			return null;
		}

		ReleaseId oldReleaseId = releaseId;

		releaseId = kieServices.newReleaseId(groupId, artifactId, next);
		KieContainer kieContainer = kieServices.newKieContainer(releaseId);

		// 前回のリリースがあれば、削除を実施する
		if (oldReleaseId != null) {
			kieServices.getRepository().removeKieModule(oldReleaseId);
		}

		logger.infov("RELESE ID={0}", kieContainer.getReleaseId());

		// KieContainerからKieBaseのインスタンスを取得。
		return kieContainer.getKieBase();
	}
	
	/**
	 * ディレクトリ配下のルールファイルをKieFileSystemに読み込む
	 * 
	 * @param kfs
	 * @param file
	 */
	private void writeAllRule(KieFileSystem kfs, File file) {
		if (!file.exists() || file.isHidden()) {
			return;
		}

		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				writeAllRule(kfs, subFile);
			}
			return;
		}

		if (file.canRead()) {
			KieServices kieServices = KieServices.Factory.get();
			String fullPath = file.getAbsolutePath();

			if (File.separator.equals("\\")) {
				// Windows対策
				fullPath = fullPath.replaceAll("\\\\", "/");
			}
			kfs.write("src/main/resources/" + fullPath, kieServices.getResources().newFileSystemResource(fullPath, "UTF-8"));

		}
	}
	
	@PreDestroy
	void destory() {
		logger.infov("##################### {0} destory start ", this.getClass().getSimpleName());
		
		// プール内の古いバージョンをdispose()
		while(true) {
			Holder holder = pool.poll();
			if(holder == null) {
				break;
			} else {
				holder.session.dispose();
    			if(logger.isDebugEnabled()) {
    				logger.debugv("destory() Dispose RuleSession {0}={1}", ruleBaseDirFile, holder.session);
    			}
			}
		}

		// レポジトリcleanup
		if (releaseId != null) {
			KieServices.Factory.get().getRepository().removeKieModule(releaseId);
			releaseId = null;
		}
		
		logger.infov("##################### {0} destory end ", this.getClass().getSimpleName());
	}
	
	/**
	 * 現在のルール定義ファイルで、KieBaseを再構築し保持します。<br>
	 * このメソッド呼出のタイミングで、アプリケーションに最新のルールが適用されます。
	 */
	@Lock(LockType.WRITE)
	public boolean refreshKBase() {
		logger.info("refreshKBase()");
		
		// 最新のKieBase
		KieBase newKieBase = createNewKBase();
		if(newKieBase == null) {
			logger.error("refreshRule() Fail.");
			return false;
		}
		
		kBase = newKieBase;
		currentVersion.incrementAndGet();
		
		// プール内の古いバージョンをdispose()
		while(true) {
			Holder holder = pool.poll();
			if(holder == null) {
				break;
			} else {
				holder.session.dispose();
    			if(logger.isDebugEnabled()) {
    				logger.debugv("refreshKBase() Dispose RuleSession {0}={1}", ruleBaseDirFile, holder.session);
    			}
			}
		}
		
		logKBase();
		return true;
	}
	
	/**
	 * KnowledgeBase内のルールの一覧をログ出力します。
	 * @param kbase
	 */
	private void logKBase() {
		logger.infov(" kbase = {0}", kBase);

		Collection<KiePackage> packages = kBase.getKiePackages();

		for (KiePackage pkg : packages) {
			logger.infov("  pkg = {0}", pkg);

			Collection<Rule> rules = pkg.getRules();
			for (Rule rule : rules) {
				logger.debugv("   rule = {0}", rule.getName());
			}
		}
	}

	/**
	 * ステートレスセッションをプールから借ります。プールが枯渇していた場合は新規にステートレスセッションを生成します。
	 * @return ステートレスセッション。このインスタンスはルール実行後に必ず{@link #release(StatefulKnowledgeSession)}で返却すること。
	 */
	@Lock(LockType.READ)
    public KieSession borrow() {
		Holder holder;
    	while(true) {
    		// プールから1件取得
    		holder = pool.poll();
    		
    		if( holder == null) {
    			// プールが枯渇した場合は新規作成
    			holder = new Holder();
    			holder.version = currentVersion.get();
    			holder.session = kBase.newKieSession();

    			// ルール実行時のログを出力するためのEventListenerを渡す。
//    			holder.session.addEventListener(new RuleRuntimeLoggingEventListener());
//    			holder.session.addEventListener(new AgendaLoggingEventListener());
    			
    			if(logger.isDebugEnabled()) {
    				logger.debugv("borrow() New RuleSession {0}={1}", ruleBaseDirFile, holder.session);
    			}
    			break;
    		}
    		
    		if(holder.version == currentVersion.get()) {
        		// 最新バージョンの場合は、それを採用
    			break;
    		} else {
    			// 古いバージョンは破棄
    			holder.session.dispose();

    			if(logger.isDebugEnabled()) {
    				logger.debugv("borrow() Dispose RuleSession {0}={1}", ruleBaseDirFile, holder.session);
    			}
    			
    			continue;
    		}
    	}
    	
    	// 貸出管理に追加
    	borrowed.put(holder.session, holder);
    	
    	return holder.session;
    }
	
	/**
	 * 
	 * @param session
	 */
	@Lock(LockType.READ)
    public void release(KieSession session) {

    	// 貸出管理から取得
		Holder holder = borrowed.remove(session);
		
		// バージョンチェック
		if(holder.version != currentVersion.get() ) {
			// バージョンが古い場合は破棄
			session.dispose();
			
			if(logger.isDebugEnabled()) {
				logger.debugv("release() Dispose RuleSession {0}={1}", ruleBaseDirFile, session);
			}
			
		} else {
			// 最新バージョンの場合は、WorkingMemoryを空にしてプールに返却
			Collection<FactHandle> facts = session.getFactHandles();
			for(FactHandle fact : facts) {
				session.delete(fact);
			}
			pool.offer(holder);
		}
	}
    
    private static class Holder {
    	long version=0;
    	KieSession session=null;
    }
    
    public static RuleSessionServiceBean lookup() {
		int retryCount = 0;
		while(true) {
			Context ctx = null;
			try {
				ctx = new InitialContext();
				return (RuleSessionServiceBean) ctx.lookup(JNDI_NAME);
			} catch (NameNotFoundException e) {
				retryCount++;
				if(retryCount <= 10) {
					// 起動の最中に呼ばれた場合。
					logger.infov("RuleSessionServiceBeanを取得できません。リトライします。{0}/10", retryCount);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
				} else {
					throw new RuntimeException("RuleSessionServiceBeanを取得できません。", e);
				}
			} catch (Exception e) {
				throw new RuntimeException("RuleSessionServiceBeanを取得できません。", e);
			} finally {
				if (ctx != null) {
					try {
						ctx.close();
					} catch (Exception e) {
					}
				}
			}
		}
    }
}
