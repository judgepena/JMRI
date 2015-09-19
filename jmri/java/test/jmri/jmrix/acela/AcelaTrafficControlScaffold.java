package jmri.jmrix.acela;

import org.apache.log4j.Logger;
import java.util.Vector;

/**
 * Stands in for the AcelaTrafficController class
 * @author			Bob Coleman Copyright 2008
 */

public class AcelaTrafficControlScaffold extends AcelaTrafficController {
	public AcelaTrafficControlScaffold() {
		if (log.isDebugEnabled()) log.debug("setting instance: "+this);
		self = this;
	}

	// override some AcelaTrafficController methods for test purposes

    @Override
	public boolean status() {
		return true;
	}

	/**
	 * record messages sent, provide access for making sure they are OK
	 */
	public Vector<AcelaMessage> outbound = new Vector<AcelaMessage>();  // public OK here, so long as this is a test class
    @Override
	public void sendAcelaMessage(AcelaMessage m, AcelaListener reply) {
		if (log.isDebugEnabled()) log.debug("sendAcelaMessage ["+m+"]");
		// save a copy
		outbound.addElement(m);
		// we don't return an echo so that the processing before the echo can be
		// separately tested
	}

	// test control member functions

	/**
	 * forward a message to the listeners, e.g. test receipt
	 */
	protected void sendTestMessage(AcelaMessage m, AcelaListener l) {
		// forward a test message to NceListeners
		if (log.isDebugEnabled()) log.debug("sendTestMessage  ["+m+"]");
		notifyMessage(m, l);
		return;
	}

	protected void sendTestMessage(AcelaReply m) {
		// forward a test message to NceListeners
		if (log.isDebugEnabled()) log.debug("sendTestMessage  ["+m+"]");
		//notifyMessage(m, l);
		return;
	}

	/*
	* Check number of listeners, used for testing dispose()
	*/

	public int numListeners() {
		return cmdListeners.size();
	}

	static Logger log = Logger.getLogger(AcelaTrafficControlScaffold.class.getName());

}