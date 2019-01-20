/*
 * Created on 23-Jun-2005
 *
 */
package uk.co.indigo.play.bitset;

import java.util.BitSet;

/**
 * @author milbuw
 *
 */
public class BitSetConverter {

    public static BitSet fromByteArray(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i=0; i<bytes.length*8; i++) {
            if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }
    
    public static byte[] toByteArray(BitSet bits) {
        byte[] returnVal;
        byte[] bytes = new byte[bits.length()/8+1];
        for (int i=0; i<bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length-i/8-1] |= 1<<(i%8);
            }
        }
        returnVal = bytes;
        if (((bytes[0] & 0xff) == 0) && (bytes.length > 1)) {
            if ((bytes[1] & 0xff) > 0x7f) {
                byte[] bytes2 = new byte[bytes.length-1];
                for (int i = 0; i < bytes2.length; i++) {
                    bytes2[i] = bytes[i+1];
                }
                returnVal = bytes2;
            }
        }
        return returnVal;
    }
    
    public static BitSet carve(BitSet bits, int from, int to) {
        if ((from < 0) || (from > (bits.size() + 1)) ||
            ((to < 0) || (to < from) || to > (bits.size() + 1))
           ) {
            throw new IllegalArgumentException("Check indices.");
        }
        BitSet newBits = new BitSet();
        int newBitsCtr = 0;
        for (int i = from; i < to; i++) {
            newBits.set(newBitsCtr,bits.get(i));
            newBitsCtr++;
        }
        return newBits;
    }

    public static int toInt(BitSet bits) {
        int result = 0;
        for (int i = 0; i < bits.length(); i++) {
            result += (bits.get(i)?Math.pow(2,i):0);
        }
        return result;
    }

    public static long toLong(BitSet bits) {
        long result = 0;
        for (int i = 0; i < bits.length(); i++) {
            result += (bits.get(i)?Math.pow(2,i):0);
        }
        return result;
    }
    
    public static BitSet reverse(BitSet bits) {
        BitSet newBitSet = new BitSet();
        for (int i = 0; i < bits.length(); i++) {
            newBitSet.set(bits.length()-1 - i, bits.get(i));
        }
        return newBitSet;
    }
}
