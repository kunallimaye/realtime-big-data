package com.redhat.example.framework.management;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.jboss.logging.Logger;

import com.redhat.example.framework.jdg.JdgLibServiceBean;
import com.redhat.example.framework.jdg.impexp.ExpCacheDistCallable;
import com.redhat.example.framework.jdg.impexp.ImpCacheDistCallable;
import com.redhat.example.framework.rule.RuleReflectDistCallable;
import com.redhat.example.util.StopWatch;

@WebService
@Stateless
public class SystemManagement {

	private static final Logger logger = Logger.getLogger(SystemManagement.class);
	
	@EJB
	JdgLibServiceBean jdg;
	
	@WebMethod
	@WebResult(name="result")
	public String dumpCache(@WebParam(name="cacheName") String cacheName, @WebParam(name="fileName") String fileName) {
		StopWatch sw = new StopWatch("export");
		logger.info("dumpCache() start");
		
		ExpCacheDistCallable callable = new ExpCacheDistCallable(cacheName, fileName);
		
		List<String> resultList = jdg.execDist(cacheName, callable);

		for(String result : resultList) {
			logger.infov("result={0}", result);
		}
		
		logger.infov("dumpCache() end");
		
		return resultList.toString() + " : " + sw.toString();
	}

	@WebMethod
	@WebResult(name="result")
	public String loadCache(@WebParam(name="cacheName") String cacheName, @WebParam(name="fileName") String fileName) {
		StopWatch sw = new StopWatch("import");
		logger.info("loadCache() start");
		
		ImpCacheDistCallable callable = new ImpCacheDistCallable(cacheName, fileName);
		
		List<String> resultList = jdg.execDist(cacheName, callable);

		for(String result : resultList) {
			logger.infov("result={0}", result);
		}
		
		logger.infov("loadCache() end");
		
		return resultList.toString() + " : " + sw.toString();
	}
	
	@WebMethod
	@WebResult(name="result")
	public String clearCache(@WebParam(name="cacheName") String cacheName) {
		StopWatch sw = new StopWatch("clear");
		
		jdg.get(cacheName).clear();
		
		return sw.toString();
	}

	@WebMethod
	@WebResult(name="result")
	public String reflectRule() {
		StopWatch sw = new StopWatch("clear");

		RuleReflectDistCallable callable = new RuleReflectDistCallable();
		jdg.execDist(JdgLibServiceBean.CACHE_STATUS, callable);
		
		return sw.toString();
	}
}
