package com.redhat.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.redhat.example.data.WordCount;
import com.redhat.example.framework.jdg.JdgLibServiceBean;
import com.redhat.example.mapreduce.WordCountMapper;
import com.redhat.example.mapreduce.WordCountReducer;
import com.redhat.example.util.StopWatch;

@Stateless
public class WordCountTimerBean {
	
	private static final Logger logger = Logger.getLogger(WordCountTimerBean.class);
	
	@EJB
	private JdgLibServiceBean service;
	
	private static AtomicBoolean running = new AtomicBoolean(false);
	
	@SuppressWarnings("unchecked")
	@Schedule(second = "*/1", minute = "*", hour = "*", persistent = false)
	public void execWordCount() {
		if (service.get("map-reduce").getCacheManager().isCoordinator()) {
			if(running.compareAndSet(false, true)) {
				try {
					StopWatch sw = new StopWatch("execWordCount");
					logger.debug("execWordCount() start");
					
					WordCountMapper mapper = new WordCountMapper();
					WordCountReducer reducer = new WordCountReducer();
					
					Map<String, Integer> wordCountMap = service.execMapReduce("map-reduce", mapper, reducer, null, true);
					sw.snap("mapReduce");
					
					List<WordCount> sorted = new ArrayList<>();

					if(logger.isDebugEnabled()) {
						for(Entry<String, Integer> entry : wordCountMap.entrySet()) {
							WordCount w = new WordCount();
							w.setWord(entry.getKey());
							w.setCount(entry.getValue());
							sorted.add(w);
						}
					} else {
						for(Entry<String, Integer> entry : wordCountMap.entrySet()) {
							WordCount w = new WordCount();
							w.setWord(entry.getKey());
							w.setCount(entry.getValue());
							sorted.add(w);
							if(sorted.size() > 10) {
								break;
							}
						}
					}
					
					Collections.sort(sorted, new SortComp());

					String time = sw.toString();
					
					logger.infov("execWordCount() end : ret={0}, time={1}", sorted, time);
					

				} finally {
					running.compareAndSet(true, false);
				}
			}
		}
	}
	
	private static class SortComp implements Comparator<WordCount> {
		@Override
		public int compare(WordCount o1, WordCount o2) {
			return o2.getCount() - o1.getCount();
		}	
	}
	
	
}
