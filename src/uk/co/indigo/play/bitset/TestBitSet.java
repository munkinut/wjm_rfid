/*
 * Created on 22-Jun-2005
 *
 */
package uk.co.indigo.play.bitset;

import java.util.BitSet;

/**
 * @author milbuw
 *
 */
public class TestBitSet {
    
    public static void main(String[] args) {
        
        byte[] epc = {(byte)0xae, (byte)0xFF, (byte)0xFF, (byte)0xAA, (byte)0xFF,
                (byte)0xae, (byte)0xFF, (byte)0xFF, (byte)0xAA, (byte)0xFF,
                (byte)0xAA, (byte)0xFF};
        
        for (int i = 0; i < epc.length; i++) {
            System.out.print((epc[i] & 0xff) + " ");
        }
        
        System.out.println();
        
        BitSet bits = BitSetConverter.fromByteArray(epc);
        for (int i = 0; i < bits.size(); i++) {
            System.out.print(bits.get(i)?'1':'0');
        }
        
        System.out.println();
        
        byte[] bytes = BitSetConverter.toByteArray(bits);
        for (int i = 0; i < bytes.length; i++) {
            System.out.print((bytes[i] &0xff) + " ");
        }

        System.out.println();
        
        int from = 0;
        int to = 8;
        
        BitSet carvedBits = BitSetConverter.carve(bits, from, to);
        for (int i = 0; i < carvedBits.size(); i++) {
            System.out.print(carvedBits.get(i)?'1':'0');
        }
        
        System.out.println();
        
        byte[] carvedBytes = BitSetConverter.toByteArray(carvedBits);
        for (int i = 0; i < carvedBytes.length; i++) {
            System.out.print((carvedBytes[i] & 0xff) + " ");
        }
        
        System.out.println();
        
        BitSet reversedBits = BitSetConverter.reverse(carvedBits);
        for (int i = 0; i < reversedBits.size(); i++) {
            System.out.print(reversedBits.get(i)?'1':'0');
        }
        
        System.out.println();
        
        byte[] reversedBytes = BitSetConverter.toByteArray(reversedBits);
        for (int i = 0; i < reversedBytes.length; i++) {
            System.out.print((reversedBytes[i] & 0xff) + " ");
        }
        
        System.out.println();
        
        // sets the scene for handling the big endian bits of an epc
        // using our little-endian BitSet.  should now be able to carve
        // out arbitrary chunks and evaluate them.
    }
        
}
