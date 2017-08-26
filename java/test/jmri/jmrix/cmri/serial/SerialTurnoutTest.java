package jmri.jmrix.cmri.serial;

import jmri.implementation.AbstractTurnoutTestBase;
import org.junit.Assert;
import org.junit.Before;

/**
 * Tests for the jmri.jmrix.cmri.serial.SerialTurnout class
 *
 * @author	Bob Jacobsen
 */
public class SerialTurnoutTest extends AbstractTurnoutTestBase {

    private jmri.jmrix.cmri.CMRISystemConnectionMemo memo = null;
    private SerialTrafficControlScaffold tcis = null;
    private SerialNode n = null;

    @Override
    @Before
    public void setUp() {
        apps.tests.Log4JFixture.setUp();
        jmri.util.JUnitUtil.resetInstanceManager();
        
        // prepare an interface
        tcis = new SerialTrafficControlScaffold();
        memo = new jmri.jmrix.cmri.CMRISystemConnectionMemo();
        memo.setTrafficController(tcis);
        n = new SerialNode(0, SerialNode.SMINI,tcis);
        t = new SerialTurnout("CT4", "t4",memo);
        Assert.assertNotNull("node exists", n);
        Assert.assertNotNull("turnout exists", t);
    }

    @Override
    public int numListeners() {
        return tcis.numListeners();
    }

    @Override
    public void checkThrownMsgSent() {

//                tcis.sendSerialMessage(tcis.nextWrite(), null); // force outbound message; normally done by poll loop
//		Assert.assertTrue("message sent", tcis.outbound.size()>0);
//		Assert.assertEquals("content", "41 54 08", tcis.outbound.elementAt(tcis.outbound.size()-1).toString());  // THROWN message
    }

    @Override
    public void checkClosedMsgSent() {
//                tcis.sendSerialMessage(tcis.nextWrite(), null); // force outbound message; normally done by poll loop
//		Assert.assertTrue("message sent", tcis.outbound.size()>0);
//		Assert.assertEquals("content", "41 54 00", tcis.outbound.elementAt(tcis.outbound.size()-1).toString());  // CLOSED message
    }

}
