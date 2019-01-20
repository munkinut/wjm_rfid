package uk.co.indigo.epc;

import junit.framework.TestCase;

public class SSCC96CodecTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'uk.co.indigo.epc.SSCC96Codec.encodeSSCC96(String, int, int)'
	 */
	public void testEncodeSSCC96StringIntInt() {
		String sscc = "106141411928374657";
		String header = "00110001";
		String filter = "011";
		String partition = "000";
		String cpiPadded = "0000111001001100100100010001011101110011";
		String srePadded = "000100010000111001";
		String zeroes = "000000000000000000000000";
		String expected = header + filter + partition + cpiPadded + srePadded + zeroes;
		int L = 12;
		int F = 3;
		String ssccBin = SSCC96Codec.encodeSSCC96(sscc,L,F);
		assertEquals(ssccBin, expected);
	}

	/*
	 * Test method for 'uk.co.indigo.epc.SSCC96Codec.encodeSSCC96(TagSSCC96)'
	 */
	public void testEncodeSSCC96TagSSCC96() {
		int hv = 0x31;
		int fv = 3;
		int pv = 0;
		String cpi = "061414119283";
		String sr = "7465";
		String ed = "1";
		String cd = "7";
		String sc = ed + cpi + sr + cd;
		String header = "00110001";
		String filter = "011";
		String partition = "000";
		String cpiPadded = "0000111001001100100100010001011101110011";
		String srePadded = "000100010000111001";
		String zeroes = "000000000000000000000000";
		String expected = header + filter + partition + cpiPadded + srePadded + zeroes;
		TagSSCC96 tag = new TagSSCC96(hv, fv, pv, cpi, sr, ed, cd, sc);
		String ssccBin = SSCC96Codec.encodeSSCC96(tag);
		assertEquals(ssccBin, expected);
	}

	/*
	 * Test method for 'uk.co.indigo.epc.SSCC96Codec.decodeSSCC96(String)'
	 */
	public void testDecodeSSCC96() {
		String ssccBin = "001100010110000000111001001100100100010001011101110011000100010000111001000000000000000000000000";
		String expected = "106141411928374657";
		TagSSCC96 tag = SSCC96Codec.decodeSSCC96(ssccBin);
		assertEquals(expected, tag.getSscc());
	}

}
