package com.redhat.example.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.redhat.example.InformationEntry;
import com.redhat.example.InformationHeader;
import com.redhat.example.RealTimeBigData;
import com.redhat.example.RealTimeBigDataService;

public class TestLoader {

	private static final Logger logger = Logger.getLogger(TestLoader.class);

	@Option(name="-h", usage="help")
	public static boolean help;
	
	@Option(name="-ipPort", usage="Ip:Port")
	public static String ipPort = "localhost:8080";

	@Option(name="-f", usage="contents data file")
	public static String fileName = "eap63manual.txt";
//	public static String fileName = "jpText.txt";
	
	
	@Option(name="-t", usage="Thread Num")
	public static int threadNum = 2;
	
	@Option(name="-time", usage="Running time(sec)")
	public static long runningTime = 60;
	

	@Option(name="-d", usage="Delay(millsec)")
	public static long delay = 0;
	
	private static long end;
	private static URL url;
	private static RealTimeBigDataService service;
	
	private static List<String> contents = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception {

		// parse argument
		TestLoader app = new TestLoader();
        CmdLineParser parser = new CmdLineParser(app);
        try {
            parser.parseArgument(args);    
        } catch (CmdLineException e) {
            System.err.println(e);
            parser.printUsage(System.err);
            System.exit(1);
        }
		
        if(help) {
            parser.printUsage(System.err);
            System.exit(1);
        }
        
        try {
            url = new URL("http://" + ipPort + "/realtime-big-data-business/RealTimeBigData?wsdl");
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
		

        InputStream is = TestLoader.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String content = null;
        while( (content = br.readLine()) != null) {
        	if(!content.isEmpty()) {
        		contents.add(content);
        	}
        }
        br.close();
        logger.infov("contents have {0} lines.", contents.size() );
        
		service = new RealTimeBigDataService(url);
		
		ExecutorService thPool = Executors.newFixedThreadPool(threadNum);
		
		end = System.currentTimeMillis() + runningTime*1000;
		for(int i=0; i<threadNum; i++) {
			Sender sender = new Sender();
			thPool.execute(sender);
		}
		
		thPool.shutdown();
		thPool.awaitTermination(runningTime, TimeUnit.SECONDS);
		
	}
	
	private static class Sender implements Runnable {

		@Override
		public void run() {
			int contentSize = contents.size();
			RealTimeBigData ws = service.getRealTimeBigDataPort();
			
			while (end > System.currentTimeMillis()) {
				long sleep = (long)(Math.random()*delay);
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					break;
				}
				
				int index = (int)(Math.random()*contentSize);
				String content = contents.get(index);
				
				InformationEntry entry = new InformationEntry();
				InformationHeader header = new InformationHeader();
				header.setSource1("Test");
				header.setRegion1("jp");
				entry.setHeader(header);
				entry.setContent(content);
				String ret = ws.entry(entry);
	
				logger.debugv("send {}" , content);
			}
		}
		
	}
}
