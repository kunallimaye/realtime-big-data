package com.redhat.example.mapreduce;

import java.util.Iterator;

import org.infinispan.distexec.mapreduce.Reducer;
import org.jboss.logging.Logger;

public class RegionCountReducer implements Reducer<String, Integer> {	
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 1901016598354633256L;

	private static final Logger logger = Logger.getLogger(RegionCountReducer.class);
	
	
	public Integer reduce(String key, Iterator<Integer> iter) {
		logger.tracev("reduce() start {0}", key);
		
		int sum = 0;
		while (iter.hasNext()) {
			Integer i = iter.next();
			sum += i;
		}

		logger.infov("reduce() end. key={0}, sum={1}", key, sum);
		return sum;
	}
}
