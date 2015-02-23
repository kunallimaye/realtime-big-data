package com.redhat.example.mapreduce;

import java.util.List;

import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;
import org.jboss.logging.Logger;

import com.redhat.example.data.TokenizedData;

public class RegionCountMapper implements Mapper<String, List<TokenizedData>, String, Integer> {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -5943370243108735560L;

	private static final Logger logger = Logger.getLogger(RegionCountMapper.class);
	
	public void map(String key, List<TokenizedData> value, Collector<String, Integer> c) {
		logger.tracev("map() start {0} {1}", key, value);
		
		if(value == null) {
			logger.tracev("value is null. {0} is expire or removed." , key);
			return;
		}

		for(TokenizedData token : value) {
			if( token.getAllFeatures().contains("地域") ) {
				c.emit(token.getSurfaceForm(), 1);
			}
		}
		
		logger.trace("map() end");
	}
}