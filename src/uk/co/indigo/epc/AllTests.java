/*
 * Created on 09-May-2005
 * HEAD
 */
package uk.co.indigo.epc;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author milbuw
 *
 */
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for uk.co.indigo.epc");
		//$JUnit-BEGIN$
		suite.addTestSuite(EPCParserTest.class);
		suite.addTestSuite(BitUtilsTest.class);
		//$JUnit-END$
		return suite;
	}
}