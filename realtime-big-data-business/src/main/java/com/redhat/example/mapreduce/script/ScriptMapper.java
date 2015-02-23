package com.redhat.example.mapreduce.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;
import org.jboss.logging.Logger;

public class ScriptMapper implements Mapper<Object, Object, Object, Object> {
	
	/** The serialVersionUID */
	private static final long serialVersionUID = -5943370243108735560L;

	private static final Logger logger = Logger.getLogger(ScriptMapper.class);
	
	private String scriptText;
	
	private transient Script script;
		
	public ScriptMapper(String scriptText) {
		this.scriptText = scriptText;
	}

	public void map(Object key, Object value, Collector<Object, Object> collector) {
		logger.tracev("map() start {0} {1}", key, value);
		
		if(value == null) {
			logger.tracev("value is null. {0} is expire or removed.", key);
			return;
		}

		Script script = getScript() ;
		
		Binding binding = new Binding();

		binding.setVariable("key", key);
		binding.setVariable("value", value);
		binding.setVariable("collector", collector);
		  
		script.setBinding(binding);
		
		script.run();
		
		logger.trace("map() end");
	}
	
	private Script getScript() {
		if(script == null) {
			GroovyShell shell = new GroovyShell();
			script = shell.parse(scriptText);
		}		
		return script;
	}
}