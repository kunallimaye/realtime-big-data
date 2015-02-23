package com.redhat.example;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.atilika.kuromoji.Token;
import org.codehaus.jackson.map.ObjectMapper;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.jboss.logging.Logger;

import com.redhat.example.data.InformationEntry;
import com.redhat.example.data.InformationHeader;
import com.redhat.example.data.TokenizedData;
import com.redhat.example.framework.jdg.DoNothingReducer;
import com.redhat.example.framework.jdg.JdgLibServiceBean;
import com.redhat.example.framework.kuromoji.KuromojiServcieBean;
import com.redhat.example.mapreduce.KeywordScoringMapper;
import com.redhat.example.mapreduce.RegionCountMapper;
import com.redhat.example.mapreduce.RegionCountReducer;
import com.redhat.example.mapreduce.WordCountMapper;
import com.redhat.example.mapreduce.WordCountReducer;
import com.redhat.example.mapreduce.script.ScriptMapper;
import com.redhat.example.mapreduce.script.ScriptReducer;
import com.redhat.example.util.StopWatch;

@Stateless
@WebService
public class RealTimeBigData {

	@EJB
	private JdgLibServiceBean service;

	@EJB
	private KuromojiServcieBean kuromoji;
	
	private static final Logger logger = Logger.getLogger(RealTimeBigData.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@WebResult(name="execTime")
	public String entry(@WebParam(name="information") InformationEntry information) {
		StopWatch sw = new StopWatch("entry");
		
		ObjectMapper mapper = new ObjectMapper();

		// 入力データをそのままDataGridに保存。
		//  このデータは、解析ロジックの修正時にMapReduceフレームワークに再投入するために保存する。
		String json;
		try {
			json = mapper.writeValueAsString(information);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		sw.snap("toJson");
		
		service.get("main").put(information.getHeader().getKey(), json, 10, TimeUnit.SECONDS);
		sw.snap("putMainCache");
		
		logger.debugv("information={0}", json);
		
		List<Token> tokenList = kuromoji.tokenize(information.getContent());
		sw.snap("tokenize");

		List<TokenizedData> dataList = new ArrayList<TokenizedData>();
		
		for(Token token : tokenList) {
			TokenizedData data = new TokenizedData();
			
			data.setSource(information.getHeader().getSource1());
			data.setRegion(information.getHeader().getRegion1());
			data.setKey(information.getHeader().getKey());
			data.setSurfaceForm(token.getSurfaceForm());
			data.setAllFeatures(token.getAllFeatures());
			
			dataList.add(data);
		}
		logger.debugv("tokenized={0}", dataList);
		
		// MapReduce用キャッシュに投入
		//  データのフォーマットは未定
		DefaultCacheManager manager = service.getManager();
		Cache mapReduceCache = manager.getCache("map-reduce");

//		mapReduceCache.put(information.getHeader().getKey(), dataList);
		mapReduceCache.put(information.getHeader().getKey(), dataList, 5, TimeUnit.SECONDS);

		sw.snap("put");
		
		String time = sw.toString();
		logger.debugv("time ={0}", time);
		
		return time;
	}

	/**
	 * (参考) ワードカウント
	 */
	@SuppressWarnings({ "unchecked" })
	@WebResult(name="execTime")
	public String execWordCount() {
		StopWatch sw = new StopWatch("execWordCount");
		logger.debug("execWordCount() start");
		
		WordCountMapper mapper = new WordCountMapper();
		WordCountReducer reducer = new WordCountReducer();
		
		Map<String, Integer> wordCountMap = service.execMapReduce("map-reduce", mapper, reducer, null, true);
		sw.snap("mapReduce");
		
		String time = sw.toString();
		
		logger.infov("execWordCount() end : ret={0}, time={1}", wordCountMap, time);
	
		return wordCountMap + " : " + time;
	}
	
	/**
	 * (参考) 地域カウント
	 */
	@SuppressWarnings({ "unchecked" })
	@WebResult(name="execTime")
	public String execRegionCount() {
		
		StopWatch sw = new StopWatch("execRegionCount");
		logger.debug("execRegionCount() start");
		
		RegionCountMapper mapper = new RegionCountMapper();
		RegionCountReducer reducer = new RegionCountReducer();
		
		Map<String, Integer> countMap = service.execMapReduce("map-reduce", mapper, reducer, null, true);
		sw.snap("mapReduce");

		String time = sw.toString();
		
		List<Map.Entry<String,Integer>> entries = new ArrayList<Map.Entry<String,Integer>>(countMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String,Integer>>() {
			
			@Override
			public int compare(Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
				return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
			}
		});
	
		logger.infov("execRegionCount() end : ret={0}, time={1}", entries, time);
		
		return entries + " : " + time;
	}
	
	/**
	 * (参考) キーワードスコアリングカウント
	 */
	@SuppressWarnings({ "unchecked" })
	@WebResult(name="execTime")
	public String execKeywordScoring() {
		StopWatch sw = new StopWatch("execKeywordScoring");
		logger.debug("execKeywordScoring() start");
		
		KeywordScoringMapper mapper = new KeywordScoringMapper();
		DoNothingReducer reducer = new DoNothingReducer();
		
		Map<String, Integer> countMap = service.execMapReduce("map-reduce", mapper, reducer, null, true);
		sw.snap("mapReduce");
		
		String time = sw.toString();
		
		logger.infov("execKeywordScoring() end : ret={0}, time={1}", countMap, time);
		
		return countMap + " : " + time;
	}


	/**
	 * (参考) スクリプトによるMapReduce(性能悪い）
	 */
	@SuppressWarnings({ "unchecked" })
	@WebResult(name="execTime")
	public String execScript(@WebParam(name="mapperScript") String mapperScript, @WebParam(name="reducerScript") String reducerScript) {
		StopWatch sw = new StopWatch("execScript");
		logger.debug("execScript() start");
		
		ScriptMapper mapper = new ScriptMapper(mapperScript);
		ScriptReducer reducer = new ScriptReducer(reducerScript);
		
		Map<Object, Object> countMap = service.execMapReduce("map-reduce", mapper, reducer, null, true);
		sw.snap("mapReduce");

		String time = sw.toString();
		
		logger.infov("execScript() end : ret={0}, time={1}", countMap, time);
		
		return countMap + " : " + time;
	}
	
	/**
	 * (参考) ローカルファイルからのデータ読み込み(TWITTER用)
	 */
	@WebResult(name="execTime")
	public String entryFromLocalFile(@WebParam(name="fileName") String fileName,@WebParam(name="wait")  long wait) {

		long rowCount = 0;
		
		Reader r = null;
		BufferedReader br = null;
		try {
			r = new FileReader(fileName);
			br = new BufferedReader(r);
			
			String line = null;
			while( (line = br.readLine()) != null) {
				rowCount++;
				
				String[] tokens = line.split("\t");
				InformationEntry e = new InformationEntry();
				
				e.setHeader(new InformationHeader());
				e.getHeader().setBusinessDate(tokens[0]);
				e.getHeader().setSource1("TWITTER");
				e.setContent(tokens[2]);
				
				entry(e);
				
				// 指定された時間Waitしてから次の読み込み。
				//  ロード時間を調整することで定常負荷の試験で利用する目的
				if( wait > 0) {
					Thread.sleep(wait);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("NG:" + rowCount, e);
		} finally {
			safeClose(br);
			safeClose(r);
		}
		
		return "OK:" + rowCount;
	}

	private void safeClose(Closeable c) {
		if(c != null) {
			try {
				c.close();
			} catch (IOException e) {
			}
		}
	}
}
