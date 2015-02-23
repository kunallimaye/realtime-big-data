package com.redhat.example.util;

import java.util.ArrayList;
import java.util.List;

public class StopWatch {

	String mainLable;
	long start;
	
	List<Long> timeList = new ArrayList<Long>();
	List<String> labelList = new ArrayList<String>();
	
	long beforeTime = 0;
	
	public StopWatch(String mainLable) {
		this.mainLable = mainLable;
		start = beforeTime = System.nanoTime();
	}
	
	public StopWatch reset() {
		beforeTime = System.nanoTime();
		return this;
	}
	
	public void snap(String label) {
		timeList.add(System.nanoTime() - beforeTime);
		labelList.add(label);
		beforeTime = System.nanoTime();
	}
	
	public String toString() {
		long end = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		sb.append(mainLable).append("=").append(String.format("%1$,3d", end-start));
		
		if(timeList.size() == 0) {
			sb.append(" []");
			return sb.toString();
		} else {
			sb.append(" [");
		}
		
		for(int i=0; i<timeList.size(); i++) {
			long time = timeList.get(i);
			String timeS = String.format("%1$,3d", time);

			sb.append(labelList.get(i)).append("=").append(timeS);
			
			if(i+1 == timeList.size()) {
				sb.append("]");
			} else {
				sb.append(", ");
			}
		}
		
		return sb.toString();
	}
	
	public long getTotalTimeNano() {
		return System.nanoTime() - start;
	}
	
	public static void main(String[] args) {
		StopWatch sw = new StopWatch("テスト").reset();

		sw.snap("aa");
		sw.snap("bb");
		sw.reset();
		sw.snap("CC");
		System.out.println(sw);
	}
}
