/*
 * Created on 23-Jun-2005
 *
 */
package uk.co.indigo.play.bitset;

import java.util.BitSet;
import uk.co.indigo.play.binhexdec.Bin2Hex;

import junit.framework.TestCase;

/**
 * @author milbuw
 *
 */
public class BitSetConverterTest extends TestCase {
    
    private byte[] epc;
    private byte[] epc2;
    private byte b0;
    private byte b1;
    private byte b2;
    private byte b3;
    private byte b4;
    private byte b5;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(BitSetConverterTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        b0 = (byte)0xfe;
        b1 = (byte)0xff;
        b2 = (byte)0xac;
        b3 = (byte)0x7f;
        b4 = (byte)0xaa;
        b5 = (byte)0x0e;
        byte[] temp = {b0, b1, b2};
        byte[] temp2 = {b3, b4, b5};
        epc = temp;
        epc2 = temp2;
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        epc = null;
        epc2 = null;
    }

    public final void testFromByteArray() {
        BitSet bits = BitSetConverter.fromByteArray(epc);
        StringBuffer sb = new StringBuffer();
        for (int i = bits.length() - 1; i >= 0; i--) {
            sb.append(bits.get(i)?'1':'0');
        }
        String bitsStr = sb.toString();
        String hexBits = Bin2Hex.bin2HexDigits(bitsStr);
        System.out.println(hexBits);
        assertEquals("FEFFAC", hexBits.toUpperCase());
    }

    public final void testToByteArray() {
        BitSet bits = BitSetConverter.fromByteArray(epc);
        byte[] bytes = BitSetConverter.toByteArray(bits);
        assertEquals(bytes[0],b0);
        assertEquals(bytes[1],b1);
        assertEquals(bytes[2],b2);
    }
    
    public final void testTBAFirstByteLT128() {
        BitSet bits = BitSetConverter.fromByteArray(epc2);
        byte[] bytes = BitSetConverter.toByteArray(bits);
        assertEquals(bytes[0], b3);
        assertEquals(bytes[1], b4);
        assertEquals(bytes[2], b5);
    }

}
