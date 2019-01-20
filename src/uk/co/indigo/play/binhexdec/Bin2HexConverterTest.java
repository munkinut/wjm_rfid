/*
 * Created on 08-Jun-2005
 *
 */
package uk.co.indigo.play.binhexdec;

import junit.framework.TestCase;

/**
 * @author milbuw
 *
 */
public class Bin2HexConverterTest extends TestCase {
    
    private String[] ok;
    private String[] nok;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(Bin2HexConverterTest.class);
    }

	protected void setUp() throws Exception {
		super.setUp();
		ok = new String[6];
		nok = new String[6];
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		ok = null;
		nok = null;
	}

    public final void testBin2HexDigit() {
        ok[0] = "0000";
        ok[1] = "0001";
        ok[2] = "0010";
        ok[3] = "0110";
        ok[4] = "1111";
        ok[5] = "0101";
        nok[0] = "001";
        nok[1] = null;
        nok[2] = "";
        nok[3] = "\n";
        nok[4] = "000:";
        nok[5] = "sdfg";
        assertEquals(Bin2Hex.bin2HexDigit(ok[0]),"0");
        assertEquals(Bin2Hex.bin2HexDigit(ok[1]),"1");
        assertEquals(Bin2Hex.bin2HexDigit(ok[2]),"2");
        assertEquals(Bin2Hex.bin2HexDigit(ok[3]),"6");
        assertEquals(Bin2Hex.bin2HexDigit(ok[4]),"F");
        assertEquals(Bin2Hex.bin2HexDigit(ok[5]),"5");
        for (int i = 0; i < nok.length; i++) {
            try {
                Bin2Hex.bin2HexDigit(nok[i]);
            }
            catch (IllegalArgumentException iae) {
                assertTrue(iae.getMessage().equals("Argument must be 4 bit binary."));
            }
        }
    }

    public final void testBin2HexDigits() {
        ok[0] = "11111111";
        ok[1] = "00000000";
        ok[2] = "11110000";
        ok[3] = "10101010";
        ok[4] = "101010101";
        ok[5] = "11010101010101";
        nok[0] = "x001";
        nok[1] = null;
        nok[2] = "";
        nok[3] = "\n";
        nok[4] = "000:";
        nok[5] = "sdfg";
        assertEquals(Bin2Hex.bin2HexDigits(ok[0]),"FF");
        assertEquals(Bin2Hex.bin2HexDigits(ok[1]),"00");
        assertEquals(Bin2Hex.bin2HexDigits(ok[2]),"F0");
        assertEquals(Bin2Hex.bin2HexDigits(ok[3]),"AA");
        assertEquals(Bin2Hex.bin2HexDigits(ok[4]),"155");
        assertEquals(Bin2Hex.bin2HexDigits(ok[5]),"3555");
        for (int i = 0; i < nok.length; i++) {
            try {
                Bin2Hex.bin2HexDigits(nok[i]);
            }
            catch (IllegalArgumentException iae) {
                assertTrue(iae.getMessage().equals(
                        "Argument must contain only binary digits."));
            }
        }
    }

    public final void testPadTo4BitChunks() {
        ok[0] = "1111";
        ok[1] = "0000";
        ok[2] = "100";
        ok[3] = "11111";
        ok[4] = "00000";
        ok[5] = "10101";
        assertEquals(Bin2Hex.padToNibbles(ok[0]),"1111");
        assertEquals(Bin2Hex.padToNibbles(ok[1]),"0000");
        assertEquals(Bin2Hex.padToNibbles(ok[2]),"0100");
        assertEquals(Bin2Hex.padToNibbles(ok[3]),"00011111");
        assertEquals(Bin2Hex.padToNibbles(ok[4]),"00000000");
        assertEquals(Bin2Hex.padToNibbles(ok[5]),"00010101");
    }

    public final void testIsValidBinary() {
        ok[0] = "0000";
        ok[1] = "0";
        ok[2] = "1";
        ok[3] = "1111";
        ok[4] = "1010";
        ok[5] = "0101";
        nok[0] = "A001";
        nok[1] = null;
        nok[2] = "";
        nok[3] = "\n";
        nok[4] = "000:";
        nok[5] = "sdfg";
        for (int i = 0; i < ok.length; i++) {
            assertTrue(Bin2Hex.isValidBinary(ok[i]));
        }
        for (int i = 0; i < nok.length; i++) {
            assertFalse(Bin2Hex.isValidBinary(nok[i]));
        }
    }

}
