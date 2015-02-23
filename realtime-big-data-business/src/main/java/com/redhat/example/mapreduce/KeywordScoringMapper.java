package com.redhat.example.mapreduce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;
import org.jboss.logging.Logger;
import org.kie.api.runtime.KieSession;

import com.redhat.example.data.TokenizedData;
import com.redhat.example.framework.rule.RuleSessionServiceBean;

public class KeywordScoringMapper implements Mapper<String, List<TokenizedData>, String, Integer> {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -5943370243108735560L;

	private static final Logger logger = Logger.getLogger(KeywordScoringMapper.class);
	
	
	transient private RuleSessionServiceBean rule;
	
	
	
	public void map(String key, List<TokenizedData> value, Collector<String, Integer> c) {
		logger.tracev("map() start {0} {1}", key, value);
		
		if(value == null) {
			logger.tracev("value is null. {0} is expire or removed." , key);
			return;
		}

		RuleSessionServiceBean rule = getRule();
		
		Map<String, Integer> score = new HashMap<String, Integer>();
		

		KieSession ksession = null;
		try {
			ksession = rule.borrow();
			ksession.setGlobal("logger", logger);
			ksession.setGlobal("score", score);
			
			for(TokenizedData token : value) {
				ksession.insert(token);
			}
			
			ksession.fireAllRules();
		
		} finally {
			if(ksession != null) {
				rule.release(ksession);
			}
		}
		
		logger.trace("map() end");
	}

	private RuleSessionServiceBean getRule() {
		if(rule == null) {
			rule = RuleSessionServiceBean.lookup();
		}
		
		return rule;
	}
}