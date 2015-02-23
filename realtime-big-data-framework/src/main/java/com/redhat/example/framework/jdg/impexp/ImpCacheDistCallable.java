package com.redhat.example.framework.jdg.impexp;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.Callable;

import org.infinispan.AdvancedCache;
import org.infinispan.remoting.transport.Address;
import org.jboss.logging.Logger;

import com.redhat.example.framework.jdg.JdgUtil;

/**
 * ファイルからキャッシュ内容をロードします。
 * @author mkobayas
 *
 */
public class ImpCacheDistCallable implements Callable<String>, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ImpCacheDistCallable.class);

	private String cacheName;
	private String fileId;
	
	public ImpCacheDistCallable(String cacheName, String fileId) {
		this.cacheName = cacheName;
		this.fileId = fileId;
	}

	@Override
	public String call() throws Exception {
		logger.infov("Cache={0}: fileId={1}", cacheName, fileId);
		
		long inputCount = 0;
		AdvancedCache<Object, Object> cache = JdgUtil.lookupCache(cacheName);
		Address localAddress = cache.getCacheManager().getAddress();
		
		String localFileId = System.getProperty("jboss.node.name");
		if( localFileId == null || localFileId.isEmpty()) {
			localFileId = localAddress.toString();
		}
		
		String inputFileName = fileId + "-" + cacheName + "-" + localFileId + ".dat";
		String inputDirName = System.getProperty("jboss.server.log.dir");
		if( inputDirName == null || inputDirName.isEmpty()) {
			inputDirName = System.getProperty("java.io.tmpdir");
		}
		
		File inputFile = new File(inputDirName, inputFileName);
		if(inputFile.exists()) {
			logger.debugv("inputFile={0}", inputFile);
		} else {
			logger.debugv("inputFile={0} is not exist", inputFile);
			return "";
		}
		
		ObjectInputStream ois = null;
		try {
			InputStream fis = new FileInputStream(inputFile);
			InputStream bis = new BufferedInputStream(fis, 1024*8);
			ois = new ObjectInputStream(bis);
			
			while(true) {
				
				Object key = ois.readObject();
				Object value = ois.readObject();
				
				cache.put(key, value);
				inputCount++;
			}
			
		} catch(EOFException e) {
			// DO nothing. This is Usual case.
		} finally {
			if(ois != null) {
				ois.close();
			}
		}

		logger.infov("inputFile={0}, inputCount={1}", inputFile, inputCount);
		return inputFileName;
    }

}
