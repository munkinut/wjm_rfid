/*
 * Created on 09-May-2005
 *
 */
package uk.co.indigo.epc;

import junit.framework.TestCase;

/**
 * @author milbuw
 *
 */
public class EPCParserTest extends TestCase {
	
	private byte b00;
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
	private byte b11;
	private byte[] bytes;
	private byte[] wrong;
	private Tag t;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(EPCParserTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		b00 = (byte)0x00;
		b01 = (byte)0xFF;
		b02 = (byte)0xFF;
		b03 = (byte)0xFF;
		b04 = (byte)0xFF;
		b05 = (byte)0xFF;
		b06 = (byte)0xFF;
		b07 = (byte)0xFF;
		b08 = (byte)0xFF;
		b09 = (byte)0xFF;
		b10 = (byte)0xFF;
		b11 = (byte)0xFF;
		bytes = new byte[12];
		bytes[0] = b00;
		bytes[1] = b01;
		bytes[2] = b02;
		bytes[3] = b03;
		bytes[4] = b04;
		bytes[5] = b05;
		bytes[6] = b06;
		bytes[7] = b07;
		bytes[8] = b08;
		bytes[9] = b09;
		bytes[10] = b10;
		bytes[11] = b11;
		wrong = new byte[9];
		wrong[0] = b00;
		wrong[1] = b01;
		wrong[2] = b02;
		wrong[3] = b03;
		wrong[4] = b04;
		wrong[5] = b05;
		wrong[6] = b06;
		wrong[7] = b07;
		wrong[8] = b08;
		t = null;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for EPCParserTest.
	 * @param arg0
	 */
	public EPCParserTest(String arg0) {
		super(arg0);
	}

	public final void testWrongEPCLength() {
		try {
			t = EPCParser.parse(wrong);
		}
		catch (IllegalArgumentException iae) {
		}
		assertNull(t);
	}
	
	public final void test2BitHeader_00() {
		bytes[0] = (byte)0x00;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED);
	}
	
	public final void test2BitHeader_01() {
		bytes[0] = (byte)0x40;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),2);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED_64);
	}
	
	public final void test2BitHeader_02() {
		bytes[0] = (byte)0x80;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),2);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.SGTIN_64);
	}

	public final void test2BitHeader_03() {
		bytes[0] = (byte)0xC0;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),2);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED_64);
	}
	
	public final void test8BitHeader_01() {
		bytes[0] = (byte)0x01;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED);
	}
	
	public final void test8BitHeader_02() {
		bytes[0] = (byte)0x02;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED);
	}

	public final void test8BitHeader_03() {
		bytes[0] = (byte)0x03;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED);
	}

	public final void test8BitHeader_08() {
		bytes[0] = (byte)0x08;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.SSCC_64);
	}

	public final void test8BitHeader_09() {
		bytes[0] = (byte)0x09;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GLN_64);
	}

	public final void test8BitHeader_0A() {
		bytes[0] = (byte)0x0A;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GRAI_64);
	}

	public final void test8BitHeader_0B() {
		bytes[0] = (byte)0x0B;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GIAI_64);
	}

	public final void test8BitHeader_0C() {
		bytes[0] = (byte)0x0C;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED_64);
	}

	public final void test8BitHeader_0F() {
		bytes[0] = (byte)0x0F;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED_64);
	}

	public final void test8BitHeader_10() {
		bytes[0] = (byte)0x10;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED);
	}

	public final void test8BitHeader_2F() {
		bytes[0] = (byte)0x2F;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED);
	}

	public final void test8BitHeader_30() {
		// bytes[1] has to contain valid filter and partition bits
		// filter - 111, partition - 000, last 2 bits 00
		bytes[0] = (byte)0x30;
		bytes[1] = (byte)0xE0;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.SGTIN_96);
	}

	public final void test8BitHeader_31() {
		bytes[0] = (byte)0x31;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.SSCC_96);
	}

	public final void test8BitHeader_32() {
		bytes[0] = (byte)0x32;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GLN_96);
	}

	public final void test8BitHeader_33() {
		bytes[0] = (byte)0x33;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GRAI_96);
	}

	public final void test8BitHeader_34() {
		bytes[0] = (byte)0x34;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GIAI_96);
	}

	public final void test8BitHeader_35() {
		bytes[0] = (byte)0x35;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GID_96);
	}

	public final void test8BitHeader_36() {
		bytes[0] = (byte)0x36;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED_96);
	}

	public final void test8BitHeader_3F() {
		bytes[0] = (byte)0x3F;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.RESERVED_96);
	}
	
	public final void testTagGID_96() {
		bytes[0] = (byte)0x35;
		bytes[1] = (byte)0xAA;
		bytes[2] = (byte)0xAA;
		bytes[3] = (byte)0xAA;
		bytes[4] = (byte)0xAA;
		bytes[5] = (byte)0xAA;
		bytes[6] = (byte)0xAA;
		bytes[7] = (byte)0xAA;
		bytes[8] = (byte)0xAA;
		bytes[9] = (byte)0xAA;
		bytes[10] = (byte)0xAA;
		bytes[11] = (byte)0xAA;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),8);
		assertEquals(t.getTagLength(),96);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.GID_96);
		TagGID96 tg96 = (TagGID96)t;
		assertEquals(tg96.getGeneralManagerNumber(),0xAAAAAAA);
		assertEquals(tg96.getObjectClass(),0xAAAAAA);
		assertEquals(tg96.getSerialNumber(),0xAAAAAAAAAL);
	}

	public final void testTagSGTIN_64() {
		bytes[0] = (byte)0xAA;
		bytes[1] = (byte)0xAA;
		bytes[2] = (byte)0xAA;
		bytes[3] = (byte)0xAA;
		bytes[4] = (byte)0xAA;
		bytes[5] = (byte)0xAA;
		bytes[6] = (byte)0xAA;
		bytes[7] = (byte)0xAA;
		t = EPCParser.parse(bytes);
		assertEquals(t.getHeaderLength(),2);
		assertEquals(t.getTagLength(),64);
		assertEquals(t.getEncodingScheme(),EncodingSchemes.SGTIN_64);
		TagSGTIN64 tg96 = (TagSGTIN64)t;
		assertEquals(tg96.getFilterValue(),0x05);
		assertEquals(tg96.getCompanyPrefixIndex(),0x1555);
		assertEquals(tg96.getItemReference(), 0x55555);
		assertEquals(tg96.getSerialNumber(),0xAAAAAA);
	}

}
