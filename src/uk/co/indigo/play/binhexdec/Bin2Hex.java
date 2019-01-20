/*
 * Created on 08-Jun-2005
 *
 */
package uk.co.indigo.play.binhexdec;

import java.util.Hashtable;

/**
 * Converts binary strings to hex.
 *
 * @author milbuw
 */
public class Bin2Hex {
    
    private static Hashtable bin2hexTable = new Hashtable();
    
    static {
        bin2hexTable.put("0000","0");
        bin2hexTable.put("0001","1");
        bin2hexTable.put("0010","2");
        bin2hexTable.put("0011","3");
        bin2hexTable.put("0100","4");
        bin2hexTable.put("0101","5");
        bin2hexTable.put("0110","6");
        bin2hexTable.put("0111","7");
        bin2hexTable.put("1000","8");
        bin2hexTable.put("1001","9");
        bin2hexTable.put("1010","A");
        bin2hexTable.put("1011","B");
        bin2hexTable.put("1100","C");
        bin2hexTable.put("1101","D");
        bin2hexTable.put("1110","E");
        bin2hexTable.put("1111","F");
    }

    /**
     * Converts a single nibble.
     * 
     * @param fourBinaryBits A string containing four binary bits E.g. "1011".
     * @return A string containing the hex equivalent E.g. "B".
     * @throws IllegalArgumentException Thrown if string is not valid binary.
     */
    public static String bin2HexDigit(String fourBinaryBits) throws IllegalArgumentException {
        if ((!isValidBinary(fourBinaryBits)) || 
                (!bin2hexTable.containsKey(fourBinaryBits)))
            throw new IllegalArgumentException(
                    "Argument must be 4 bit binary.");
        return (String)bin2hexTable.get(fourBinaryBits);
    }
    
    /**
     * Converts an arbitrary number of bits.
     * 
     * @param xBinaryBits A string containing an arbitrary number of binary bits.
     * @return A string containing the hex equivalent.
     */
    public static String bin2HexDigits(String xBinaryBits) {
        String paddedBits = padToNibbles(xBinaryBits);
        StringBuffer hexDigits = new StringBuffer();
        for (int i = 0; i < paddedBits.length(); i += 4) {
            String chunk = paddedBits.substring(i,i+4);
            String hexDigit = bin2HexDigit(chunk);
            hexDigits.append(hexDigit);
        }
        return hexDigits.toString();
    }
    
    /**
     * Pads an arbitrary number of bits to a multiple of four.
     * 
     * @param xBinaryBits An abitrary number of bits. 
     * @return A string containing the bits, zero left padded to a multiple of four.
     * @throws IllegalArgumentException Thrown if string is not four valid digits.
     */
    public static String padToNibbles(String xBinaryBits) throws IllegalArgumentException {
        if (!isValidBinary(xBinaryBits)) 
            throw new IllegalArgumentException(
                    "Argument must contain only binary digits.");
        int leadBits = xBinaryBits.length()%4;
        int bitsToPad = 0;
        if (leadBits > 0) bitsToPad = 4 - leadBits;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bitsToPad; i++) {
            sb.append('0');
        }
        sb.append(xBinaryBits);
        return sb.toString();
    }
    
    /**
     * Checks if a string contains valid binary. i.e. 1's and 0's.
     * 
     * @param xBinaryBits The string to check.
     * @return True if valid, else false.
     */
    public static boolean isValidBinary(String xBinaryBits) {
        if ((xBinaryBits == null) || (xBinaryBits.length() < 1))
            return false;
        boolean success = true;
        for (int i = 0; i < xBinaryBits.length(); i++) {
            if (xBinaryBits.charAt(i) != '1' &&
                xBinaryBits.charAt(i) != '0') {
                success = false;
                break;
            }
        }
        return success;
    }

}
