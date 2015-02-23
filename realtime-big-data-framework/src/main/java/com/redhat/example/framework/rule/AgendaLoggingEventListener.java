package com.redhat.example.framework.rule;

import org.jboss.logging.Logger;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;

/**
 * 
 * @author mkobayas
 *
 */
class AgendaLoggingEventListener implements AgendaEventListener {

	private static final Logger logger = Logger.getLogger(AgendaLoggingEventListener.class);

	@Override
	public void matchCreated(MatchCreatedEvent event) {
		logger.debug(event);
	}

	@Override
	public void matchCancelled(MatchCancelledEvent event) {
		logger.debug(event);		
	}

	@Override
	public void beforeMatchFired(BeforeMatchFiredEvent event) {
		logger.debug(event);		
	}

	@Override
	public void afterMatchFired(AfterMatchFiredEvent event) {
		logger.debug(event);		
	}

	@Override
	public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
		logger.debug(event);		
	}

	@Override
	public void agendaGroupPushed(AgendaGroupPushedEvent event) {
		logger.debug(event);
	}

	@Override
	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		logger.debug(event);
	}

	@Override
	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		logger.debug(event);
	}

	@Override
	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		logger.debug(event);
	}

	@Override
	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {
		logger.debug(event);
	}
}