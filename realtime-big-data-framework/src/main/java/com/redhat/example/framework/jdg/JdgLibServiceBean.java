package com.redhat.example.framework.jdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.distexec.DefaultExecutorService;
import org.infinispan.distexec.DistributedExecutorService;
import org.infinispan.distexec.DistributedTask;
import org.infinispan.distexec.DistributedTaskBuilder;
import org.infinispan.distexec.DistributedTaskExecutionPolicy;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;
import org.infinispan.manager.DefaultCacheManager;
import org.jboss.logging.Logger;


/**
 * JDG ライブラリモード用のサービス
 * @author mkobayas
 *
 */
@Startup
@Singleton
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JdgLibServiceBean {
	
	/** JDG 設定ファイル framework-infinispan.xml **/
	static public final String CONFIG = "framework-infinispan.xml";

	/** JDG 制御用キャッシュ status<br> MapReduceの排他制御等を実施する為のREPL-CACHE **/
	public static final String CACHE_STATUS = "status";

	/** MapReduceの排他制御を実施する為のkey **/
	public static final String MR_LOCK_KEY = "map-reduce-lock";
	
	/** ロガー **/
	private static final Logger logger = Logger.getLogger(JdgLibServiceBean.class);
	
	/** キャッシュマネージャー **/
	private DefaultCacheManager manager;

	/** JNDIに登録したキャッシュのリスト<br> このサービスの停止時に利用される。 **/
	private List<String> availableCacheList = new ArrayList<String>();
	
	/**
	 * 初期化<br>
	 * JDGの初期化と、{@link #CONFIG}に設定されたNamedCacheの開始を実施する。<br>
	 * 開始されたNamedCacheをJNDIに登録する。<br>
	 * MapReduce排他制御用のエントリを{@link #CACHE_STATUS}に登録する。<br>
	 * CacheManagerをJDNIに登録する。<br>
	 */
	@PostConstruct
	void init() {
		logger.info("##################### init start ");
		
		Context ctx = null;
		try {
			ctx = new InitialContext();
			
			manager = new DefaultCacheManager(CONFIG);
			manager.start();
			for(String cacheName : manager.getCacheNames()) {
				manager.startCache(cacheName);
				logger.info("## Started cache " + cacheName);
				ctx.bind("java:/" + cacheName, manager.getCache(cacheName).getAdvancedCache());
				availableCacheList.add(cacheName);
			}
			
			manager.getCache(CACHE_STATUS).put(MR_LOCK_KEY, "");
			
			ctx.bind("java:/framework-infinispan", manager);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		logger.info("##################### init end ");
	}
	
	@PreDestroy
	void destory() {
		logger.info("##################### destory start ");

		Context ctx;

		try {
			ctx = new InitialContext();
			for(String cacheName : availableCacheList) {
				safeUnbind(ctx, "java:/" + cacheName);
			}
			
			for(String cacheName : manager.getCacheNames()) {
				manager.getCache(cacheName).stop();
				logger.info("## Stopped cache " + cacheName);
			}
			safeUnbind(ctx, "java:/framework-infinispan");
			
			manager.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		logger.info("##################### destory end ");
	}

	@Lock(LockType.READ)
	public DefaultCacheManager getManager() {
		return manager;
	}
	
	@Lock(LockType.READ)
	public AdvancedCache<Object, Object> get(String cacheName) {
		return manager.getCache(cacheName, false).getAdvancedCache();
	}

	
	/**
	 * MapReduceを実行する
	 * @param cacheName
	 * @param mapper
	 * @param reducer
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Lock(LockType.READ)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Map execMapReduce(String cacheName, Mapper mapper, Reducer reducer, Reducer combiner, boolean exclusive) {

		logger.debug("exec() start");

		Cache cache = manager.getCache(cacheName);

		MapReduceTask t = new MapReduceTask(cache, true, true);

		t.mappedWith(mapper).reducedWith(reducer);

		if (combiner != null) {
			t.combinedWith(combiner);
		}

		Map result = null;

		if (exclusive) {
			Cache statusCache = manager.getCache(CACHE_STATUS);
			statusCache.getAdvancedCache().lock(MR_LOCK_KEY);
		}

		result = t.execute();

		logger.debugv("exec() end : result={0}", result);

		Map result2 = new HashMap();
		Set s = result.entrySet();
		for (Object entry : s) {
			Map.Entry e = (Map.Entry) entry;
			if (e.getValue() != null) {
				result2.put(e.getKey(), e.getValue());
			}
		}

		return result2;
	}
	

	/**
	 * DistExcecutionを実行する
	 * @param cacheName
	 * @param callable
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Lock(LockType.READ)
	public <T> List<T> execDist(String cacheName, Callable<T> callable) {

		logger.debug("exec() start");

		Cache cache = manager.getCache(cacheName);
		
		DistributedExecutorService des = new DefaultExecutorService(cache);
		DistributedTaskBuilder<T> taskBuilder = des.createDistributedTaskBuilder(callable);
		taskBuilder.executionPolicy(DistributedTaskExecutionPolicy.ALL);
		DistributedTask<T> distributedTask = taskBuilder.build();
		List<Future<T>> futures = des.submitEverywhere(distributedTask);
		
		List<T> result = new ArrayList();
		for(Future<T> future : futures) {
			try {
				result.add(future.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		logger.debugv("exec() end : result={0}", result);
		
		return result;
	}
	
	/**
	 * JBossシャットダウン時に発生するエラーを無視してJNDIバインディングを解除する。
	 * @param ctx
	 * @param jndiName
	 * @throws NamingException
	 */
	private void safeUnbind(Context ctx, String jndiName) throws NamingException {
		
		try {
			ctx.unbind(jndiName);
		} catch (IllegalArgumentException ex) {
			if (ex.getMessage() != null && ex.getMessage().indexOf("JBAS011857") > -1) {
				// this case is shutdown phase.
			} else {
				throw ex;
			}
		} catch (NamingException e) {
			if (e.getMessage().contains("JBAS011836")) {
				// EAP停止時のエラーなので、無視する。
			} else {
				throw new RuntimeException(e);
			}
		}
	}
}
