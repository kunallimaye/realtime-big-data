package com.redhat.example.framework.jdg;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.infinispan.AdvancedCache;
import org.infinispan.manager.DefaultCacheManager;
import org.jboss.logging.Logger;

public class JdgUtil {
	private static final Logger logger = Logger.getLogger(JdgUtil.class);
	
	@SuppressWarnings("unchecked")
	public static AdvancedCache<Object, Object> lookupCache(String cacheName) {

		int retryCount = 0;
		while(true) {
			Context ctx = null;
			try {
				ctx = new InitialContext();
				return (AdvancedCache<Object, Object>) ctx.lookup("java:/" + cacheName);
			} catch (NameNotFoundException e) {
				retryCount++;
				if(retryCount <= 10) {
					// 起動の最中に呼ばれた場合。
					logger.infov("指定されたキャッシュは取得できません。リトライします。{0}/10", retryCount);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
				} else {
					throw new RuntimeException("指定されたキャッシュは取得できません。", e);
				}
			} catch (Exception e) {
				throw new RuntimeException("指定されたキャッシュは取得できません。", e);
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

	public static DefaultCacheManager lookupCacheManager() {

		int retryCount = 0;
		while(true) {
			Context ctx = null;
			try {
				ctx = new InitialContext();
				return (DefaultCacheManager) ctx.lookup("java:/framework-infinispan");
			} catch (NameNotFoundException e) {
				retryCount++;
				if(retryCount <= 10) {
					// 起動の最中に呼ばれた場合。
					logger.infov("キャッシュマネージャーを取得できません。リトライします。{0}/10", retryCount);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
				} else {
					throw new RuntimeException("キャッシュマネージャーを取得できません。", e);
				}
			} catch (Exception e) {
				throw new RuntimeException("キャッシュマネージャーを取得できません。", e);
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
