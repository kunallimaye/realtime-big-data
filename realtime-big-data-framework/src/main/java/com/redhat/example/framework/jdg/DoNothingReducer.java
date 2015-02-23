package com.redhat.example.framework.jdg;

import java.util.Iterator;

import org.infinispan.distexec.mapreduce.Reducer;

public class DoNothingReducer implements Reducer<Object, Object> {

	private static final long serialVersionUID = 1L;

	@Override
	public Integer reduce(Object reducedKey, Iterator<Object> iter) {
		return null;
	}

}
