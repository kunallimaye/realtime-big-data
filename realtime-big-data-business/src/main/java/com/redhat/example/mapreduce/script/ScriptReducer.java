package com.redhat.example.mapreduce.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.util.Iterator;

import org.infinispan.distexec.mapreduce.Reducer;
import org.jboss.logging.Logger;

public class ScriptReducer implements Reducer<Object, Object> {	
	
	/** The serialVersionUID */
	private static final long serialVersionUID = 1901016598354633256L;

	private static final Logger logger = Logger.getLogger(ScriptReducer.class);

	private String scriptText;

	private transient Script script;
	
	public ScriptReducer(String scriptText) {
		this.scriptText = scriptText;
	}
	
	public Object reduce(Object key, Iterator<Object> iter) {
		logger.tracev("reduce() start {0}", key);

		Script script = getScript();
		
		Binding binding = new Binding();

		binding.setVariable("key", key);
		binding.setVariable("iter", iter);
		  
		script.setBinding(binding);
		
		Object result = script.run();
		
		logger.tracev("reduce() end : result={0}", result);

		return result;
	}

	private Script getScript() {
		if(script == null) {
			GroovyShell shell = new GroovyShell();
			script = shell.parse(scriptText);
		}
		return script;
	}
}
