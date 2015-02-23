package com.redhat.example.framework.rule;

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

/**
 * 最新のルールを反映する
 * @author mkobayas
 *
 */
public class RuleReflectDistCallable implements Callable<String>, Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(RuleReflectDistCallable.class);

	transient private RuleSessionServiceBean rule;
	
	@Override
	public String call() throws Exception {
		logger.info("Reflect New Rule.");
		
		getRule().refreshKBase();
		
		return "OK";
	}

	private RuleSessionServiceBean getRule() {
		if(rule == null) {
			Context ctx = null;
			try {
				ctx = new InitialContext();
				rule = RuleSessionServiceBean.lookup();
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally{
				try {
					ctx.close();
				} catch (NamingException e) {}
			}
		}
		
		return rule;
	}

}
