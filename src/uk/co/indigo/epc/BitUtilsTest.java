/*
 * Created on 10-May-2005
 *
 */
package uk.co.indigo.epc;

import junit.framework.TestCase;

/**
 * @author milbuw
 *
 */
public class BitUtilsTest extends TestCase {
	
	private byte b01;
	private byte b02;
	private byte b03;
	private byte b04;
	private byte b05;
	private byte b06;
	private byte b07;
	private byte b08;
	private byte b09;
	private byte b10;
	private byte[] parseFirst28Bits;
	private byte[] parseMiddle24Bits;
	private byte[] parseLast36Bits;
	private byte parse3FilterBits;
	private byte parseFirst3FilterBits;
	private byte parseFirst3PartBits;
	private byte[] parseCPIBits;
	private byte[] parseIRBits;
	private byte[] parseSNBits;
	private int result;
	private long l_result;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BitUtilsTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		result = 0;
		l_result = 0;
		b01 = (byte)0xFF;
		b02 = (byte)0xFF;
		b03 = (byte)0xFF;
		b04 = (byte)0xFF;
		b05 = (byte)0xAF;
		b06 = (byte)0xFA;
		b07 = (byte)0xAA;
		b08 = (byte)0x07;
		b09 = (byte)0xE0;
		b10 = (byte)0x01;
		parseFirst28Bits = new byte[4];
		parseFirst28Bits[0] = b01;
		parseFirst28Bits[1] = b02;
		parseFirst28Bits[2] = b03;
		parseFirst28Bits[3] = b04;
		parseMiddle24Bits = new byte[4];
		parseMiddle24Bits[0] = b05;
		parseMiddle24Bits[1] = b01;
		parseMiddle24Bits[2] = b02;
		parseMiddle24Bits[3] = b06;
		parseLast36Bits = new byte[5];
		parseLast36Bits[0] = b01;
		parseLast36Bits[1] = b02;
		parseLast36Bits[2] = b03;
		parseLast36Bits[3] = b04;
		parseLast36Bits[4] = b01;
		parse3FilterBits = (byte)0x38;
		parseFirst3FilterBits = (byte)0xE0;
		parseCPIBits = new byte[3];
		parseCPIBits[0] = b08;
		parseCPIBits[1] = b01;
		parseCPIBits[2] = b09;
		parseIRBits = new byte[3];
		parseIRBits[0] = b09;
		parseIRBits[1] = b01;
		parseIRBits[2] = b01;
		parseSNBits = new byte[4];
		parseSNBits[0] = b10;
		parseSNBits[1] = b01;
		parseSNBits[2] = b01;
		parseSNBits[3] = b01;
		parseFirst3PartBits = (byte)0xFF;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testParseMiddle24BitsToInt() {
		result = BitUtils.parseMiddle24BitsToInt(parseMiddle24Bits);
		assertEquals(result, 0xFFFFFF);
		parseMiddle24Bits[0] = b07;
		parseMiddle24Bits[1] = b07;
		parseMiddle24Bits[2] = b07;
		parseMiddle24Bits[3] = b07;
		result = BitUtils.parseMiddle24BitsToInt(parseMiddle24Bits);
		assertEquals(result, 0xAAAAAA);
	}

	public final void testParseFirst28BitsToInt() {
		result = BitUtils.parseFirst28BitsToInt(parseFirst28Bits);
		assertEquals(result, 0xFFFFFFF);
		parseFirst28Bits[0] = b07;
		parseFirst28Bits[1] = b07;
		parseFirst28Bits[2] = b07;
		parseFirst28Bits[3] = b07;
		result = BitUtils.parseFirst28BitsToInt(parseFirst28Bits);
		assertEquals(result, 0xAAAAAAA);
	}

	public final void testParseLast36BitsToLong() {
		l_result = BitUtils.parseLast36BitsToLong(parseLast36Bits);
		assertEquals(l_result, 0xFFFFFFFFFL);
		parseLast36Bits[0] = b07;
		parseLast36Bits[1] = b07;
		parseLast36Bits[2] = b07;
		parseLast36Bits[3] = b07;
		parseLast36Bits[4] = b07;
		l_result = BitUtils.parseLast36BitsToLong(parseLast36Bits);
		assertEquals(l_result, 0xAAAAAAAAAL);
	}

	public final void testParseSGTIN64FilterBitsToByte() {
		byte b_result = BitUtils.parseSGTIN64FilterBitsToByte(parse3FilterBits);
		assertEquals(b_result, 0x07);
	}

	public final void testParseSGTIN64CPIBitsToShort() {
		short s_result = BitUtils.parseSGTIN64CPIBitsToShort(parseCPIBits);
		assertEquals(s_result, 0x3FFF);
	}

	public final void testParseSGTIN64IRBitsToInt() {
		int s_result = BitUtils.parseSGTIN64IRBitsToInt(parseIRBits);
		assertEquals(s_result, 0x7FFF);
	}

	public final void testParseSGTIN64SNBitsToInt() {
		int s_result = BitUtils.parseSGTIN64SNBitsToInt(parseSNBits);
		assertEquals(s_result, 0x01FFFFFF);
	}

	public final void testParseSGTIN96FilterBitsToByte() {
		byte b_result = BitUtils.parseSGTIN96FilterBitsToByte(parseFirst3FilterBits);
		assertEquals(b_result, 0x07);
	}
	
	public final void testParseSGTIN96PartitionBitsToByte() {
		byte b_result = BitUtils.parseSGTIN96PartitionBitsToByte(parseFirst3PartBits);
		assertEquals(b_result, 0x07);
	}
	
	public final void testRollBytesIntoLong() {
		long l_result = BitUtils.rollBytesIntoLong(0,new byte[] {b07,b07,b07});
		assertEquals(l_result, 0xAAAAAA);
	}
	
	public final void testParseMSBToLong() {
		long l_result;
		l_result = BitUtils.parseMSBToLong(0,new byte[] {b07,b07,b07},16);
		assertEquals(l_result, 0xAAAA);
		l_result = BitUtils.parseMSBToLong(0,new byte[] {b07,b07,b07},20);
		assertEquals(l_result, 0xAAAAA);
	}
	
	public final void testParseSGTIN96CPIBitsToString() {
		String s_result = BitUtils.parseSGTIN96CPIBitsToString(0xAAAAAA,(byte)10);
		assertEquals(s_result,"0011184810");
	}
}
