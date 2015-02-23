package com.redhat.example.framework.rule;

import org.jboss.logging.Logger;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
/**
 * 
 * @author mkobayas
 *
 */
class RuleRuntimeLoggingEventListener implements RuleRuntimeEventListener {

	private static final Logger logger = Logger.getLogger(RuleRuntimeLoggingEventListener.class);

	@Override
	public void objectInserted(ObjectInsertedEvent event) {
		logger.debug(event);		
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent event) {
		logger.debug(event);		
	}

	@Override
	public void objectDeleted(ObjectDeletedEvent event) {
		logger.debug(event);		
	}

}