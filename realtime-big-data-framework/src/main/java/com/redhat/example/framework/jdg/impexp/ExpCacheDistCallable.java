package com.redhat.example.framework.jdg.impexp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;

import org.infinispan.AdvancedCache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.distribution.DistributionManager;
import org.infinispan.remoting.transport.Address;
import org.jboss.logging.Logger;

import com.redhat.example.framework.jdg.JdgUtil;

public class ExpCacheDistCallable implements Callable<String>, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ExpCacheDistCallable.class);

	private String cacheName;
	private String fileId;
	
	public ExpCacheDistCallable(String cacheName, String fileId) {
		this.cacheName = cacheName;
		this.fileId = fileId;	
	}

	@Override
	public String call() throws Exception {
		logger.infov("Cache={0}: fileId={1}", cacheName, fileId);
		
		long outputCount = 0;
		AdvancedCache<Object, Object> cache = JdgUtil.lookupCache(cacheName);
		DistributionManager dm = cache.getDistributionManager();
		Address localAddress = cache.getCacheManager().getAddress();
		
		// 指定されたキャッシュがDISTモードか、REPLモードかを判定
		boolean distMode;
		CacheMode mode = cache.getCacheConfiguration().clustering().cacheMode();
		if (CacheMode.DIST_ASYNC.equals(mode) || CacheMode.DIST_SYNC.equals(mode)) {
			distMode = true;
		} else {
			distMode = false;
		}
		
		String localFileId = System.getProperty("jboss.node.name");
		if( localFileId == null || localFileId.isEmpty()) {
			localFileId = localAddress.toString();
		}

		// このインスタンスの全データ（プライマリ/バックアップ込）
		Set<Entry<Object, Object>> entrySet = cache.entrySet();
		

		String outPutFileName = fileId + "-" + cacheName + "-" + localFileId + ".dat";
		String outDirName = System.getProperty("jboss.server.log.dir");
		if( outDirName == null || outDirName.isEmpty()) {
			outDirName = System.getProperty("java.io.tmpdir");
		}
		
		File outputFile = new File(outDirName, outPutFileName);
		logger.debugv("outputFile={0}", outputFile);

		ObjectOutputStream oos = null;
		try {
			OutputStream fos = new FileOutputStream(outputFile);
			OutputStream bos = new BufferedOutputStream(fos, 1024*8);
			oos = new ObjectOutputStream(bos);
			
			for(Entry<Object, Object> entry : entrySet) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				
				// DISTモードの時は、自分がプライマリノードの場合のファイル出力
				// REPLモードの時は、全件出力( TODO 要件確認必要 1ノードでのみ出力するようにすることも可能)
				boolean outputFlag = false;
				if(distMode) {
					Address primaryLocation = dm.getPrimaryLocation(key);
					if (primaryLocation != null && primaryLocation.equals(localAddress)) {
						outputFlag = true;
					}
				} else {
					outputFlag = true;
				}
				
				if(outputFlag) {
					oos.writeObject(key);
					oos.writeObject(value);
					outputCount++;
				}
			}
			
			oos.flush();
		} finally {
			if(oos != null) {
				oos.close();
			}
		}

		logger.infov("outputFile={0}, outputCount={1}", outputFile, outputCount);
		return outPutFileName;
    }
	
}
