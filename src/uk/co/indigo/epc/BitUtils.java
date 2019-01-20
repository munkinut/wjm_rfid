/*
 * Created on 09-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class BitUtils {

	public BitUtils() {
		super();
	}

	public static byte evalHOBits(byte toTest, byte bits) {
		byte bitmask = (byte)0x80; // 10000000
		byte acc = 0;
		// Calculate the base of the first bit
		byte divisor = (byte)Math.pow(2, bits-1);
		for (int i = 0; i < bits; i++) {
			if ((byte)(toTest & bitmask) == bitmask)
				acc += divisor;
			divisor /= 2;
			 /* The bitmask must be ANDed with 0xFF before the 
			 * shift because of the lack of unsigned byte types.
			 * Doing this promotes the byte and masks the high
			 * order bits.  Demoting back to byte in the cast 
			 * gives the correct return type. 
			 */  
			bitmask = (byte)((bitmask & 0xFF) >> 1);
		}
		return acc;
	}

	/**
	 * Converts first 28 bits of 4 unsigned bytes to an int
	 * @param b an array of 4 unsigned bytes
	 * @return an int
	 */
	public static final int parseFirst28BitsToInt(byte[] b) {
		if (b.length != 4) throw new IllegalArgumentException(
			"Must provide 32 bits (big endian) in a 4 byte array.");
	    long l = 0;
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    l >>= 4;
	    return (int)l;
	}

	/**
	 * Converts middle 24 bits of 4 unsigned bytes to an int
	 * @param b an array of 4 unsigned bytes
	 * @return an int
	 */
	public static final int parseMiddle24BitsToInt(byte[] b) {
		if (b.length != 4) throw new IllegalArgumentException(
			"Must provide 32 bits (big endian) in a 4 byte array.");
	    long l = 0;
	    // zero first 4 bits of first byte
	    b[0] = (byte)(b[0] & 0x0F);
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    l >>= 4;
	    return (int)l;
	}

	/**
	 * Converts last 36 bits of 5 unsigned bytes to a long
	 * @param b an array of 5 unsigned bytes
	 * @return a long
	 */
	public static final long parseLast36BitsToLong(byte[] b) {
		if (b.length != 5) throw new IllegalArgumentException(
			"Must provide 40 bits (big endian) in a 5 byte array.");
	    long l = 0;
	    // zero first 4 bits of first byte
	    b[0] = (byte)(b[0] & 0x0F);
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    l <<= 8;
	    l |= b[4] & 0xFF;
	    return l;
	}
	
	public static final long parseSSCC64SerialRefBitsToLong(byte[] b) {
		if (b.length != 5) throw new IllegalArgumentException(
			"Must provide 40 bits (big endian) in a 5 byte array.");
	    long l = 0;
	    // zero first 1 bit of first byte
	    b[0] = (byte)(b[0] & 0x7F);
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    l <<= 8;
	    l |= b[4] & 0xFF;
	    return l;
	}
	
	public static final byte parseSGTIN64FilterBitsToByte(byte filterByte) {
		byte b = 0;
		// zero first 2 bits of byte
		filterByte = (byte)(filterByte & 0x3F);
		// feed into resultant byte
		b |= filterByte & 0xFF;
		// shift last 3 bits off, leaving the 3 filter bits
		b >>= 3;
		return b;
	}

	public static final short parseSGTIN64CPIBitsToShort(byte[] cpiBytes) {
		if (cpiBytes.length != 3) throw new IllegalArgumentException(
				"Must provide 24 bits (big endian) in a 3 byte array.");
		int s = 0;
		// zero first 5 bits of first byte
		cpiBytes[0] = (byte)(cpiBytes[0] & 0x07);
		// feed into resultant short and shift to make room for next byte
		s |= cpiBytes[0] & 0xFF;
		s <<= 8;
		// feed in next byte and shift to make room for last byte
		s |= cpiBytes[1] & 0xFF;
		s <<= 8;
		// feed in last byte and shift off last 5 bits
		s |= cpiBytes[2] & 0xFF;
		s >>= 5;
		return (short)s;
	}

	public static final short parseSSCC64CPIBitsToShort(byte[] cpiBytes) {
		if (cpiBytes.length != 3) throw new IllegalArgumentException(
				"Must provide 24 bits (big endian) in a 3 byte array.");
		int s = 0;
		// zero first 3 bits of first byte (filter value)
		cpiBytes[0] = (byte)(cpiBytes[0] & 0x1F);
		// feed into resultant short and shift to make room for next byte
		s |= cpiBytes[0] & 0xFF;
		s <<= 8;
		// feed in next byte and shift to make room for last byte
		s |= cpiBytes[1] & 0xFF;
		s <<= 8;
		// feed in last byte and shift off last 7 bits
		s |= cpiBytes[2] & 0xFF;
		s >>= 7;
		return (short)s;
	}

	public static final int parseSGTIN64IRBitsToInt(byte[] irBytes) {
		if (irBytes.length != 3) throw new IllegalArgumentException(
				"Must provide 24 bits (big endian) in a 3 byte array.");
		int s = 0;
		// zero first 3 bits of first byte
		irBytes[0] = (byte)(irBytes[0] & 0x1F);
		// feed into resultant short and shift to make room for next byte
		s |= irBytes[0] & 0xFF;
		s <<= 8;
		// feed in next byte and shift to make room for last byte
		s |= irBytes[1] & 0xFF;
		s <<= 8;
		// feed in last byte and shift off last 1 bit
		s |= irBytes[2] & 0xFF;
		s >>= 1;
		return s;
	}

	public static final int parseSGTIN64SNBitsToInt(byte[] snBytes) {
		if (snBytes.length != 4) throw new IllegalArgumentException(
				"Must provide 32 bits (big endian) in a 4 byte array.");
		int s = 0;
		// zero first 7 bits of first byte
		snBytes[0] = (byte)(snBytes[0] & 0x01);
		// feed into resultant short and shift to make room for next byte
		s |= snBytes[0] & 0xFF;
		s <<= 8;
		// feed in next byte and shift to make room for next byte
		s |= snBytes[1] & 0xFF;
		s <<= 8;
		// feed in next byte and shift to make room for last byte
		s |= snBytes[2] & 0xFF;
		s <<= 8;
		// feed in last byte
		s |= snBytes[3] & 0xFF;
		return s;
	}

	public static final byte parseSGTIN96FilterBitsToByte(byte filterByte) {
		return BitUtils.evalHOBits(filterByte,(byte)3);
	}

	public static final byte parseSSCC96FilterBitsToByte(byte filterByte) {
		return BitUtils.evalHOBits(filterByte,(byte)3);
	}

	public static final byte parseSSCC64FilterBitsToByte(byte filterByte) {
		return BitUtils.evalHOBits(filterByte,(byte)3);
	}

	public static final byte parseSGTIN96PartitionBitsToByte(byte partitionByte) {
		byte result = 0;
		// zero first 3 bits and last 2 bits 
		partitionByte = (byte)(partitionByte & 0x1C);
		result |= partitionByte & 0xFF;
		result >>= 2;
		return result;
	}
	
	public static final byte parseSSCC96PartitionBitsToByte(byte partitionByte) {
		byte result = 0;
		// zero first 3 bits and last 2 bits 
		partitionByte = (byte)(partitionByte & 0x1C);
		result |= partitionByte & 0xFF;
		result >>= 2;
		return result;
	}
	
	public static final long parseSGTIN96CPIBitsToLong(byte[] cpiBytes, byte cpiBits) {
		if (cpiBytes.length != 6) throw new IllegalArgumentException(
				"Must provide 48 bits in a 6 byte array");
		long result = 0;
		// zero first 6 bits of first byte and shift into result
		cpiBytes[0] = (byte)(cpiBytes[0] & 0x03);
		result |= cpiBytes[0] & 0xFF;
		result <<= 8;
		switch (cpiBits) {
			case 40:
				// already have the first 2 bits so take the next 38
				byte[] toParse38 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4],cpiBytes[5]};
				result = parseMSBToLong(result, toParse38, 38);
				break;
			case 37:
				// already have the first 2 bits so take the next 35
				byte[] toParse35 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4],cpiBytes[5]};
				result = parseMSBToLong(result, toParse35, 35);
				break;
			case 34:
				// already have the first 2 bits so take the next 32
				byte[] toParse32 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4]};
				result = parseMSBToLong(result, toParse32, 32);
				break;
			case 30:
				// already have the first 2 bits so take the next 28
				byte[] toParse28 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4]};
				result = parseMSBToLong(result, toParse28, 28);
				break;
			case 27:
				// already have the first 2 bits so take the next 25
				byte[] toParse25 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4]};
				result = parseMSBToLong(result, toParse25, 25);
				break;
			case 24:
				// already have the first 2 bits so take the next 22
				byte[] toParse22 = {cpiBytes[1],cpiBytes[2],cpiBytes[3]};
				result = parseMSBToLong(result, toParse22, 22);
				break;
			case 20:
				// already have the first 2 bits so take the next 18
				byte[] toParse18 = {cpiBytes[1],cpiBytes[2],cpiBytes[3]};
				result = parseMSBToLong(result, toParse18, 18);
				break;
			default:
				throw new IllegalArgumentException(
					"The specified number of bits to parse is incorrect."
				);
		}
		return result;
	}
	
	public static final String parseSGTIN96CPIBitsToString(long companyPrefixIndex, byte cpiDigits) {
		if (Long.toString(companyPrefixIndex).length() > cpiDigits)
			throw new IllegalArgumentException(
				"The required digits are larger than the prefix index length.");
		StringBuffer sb = new StringBuffer();
		sb.append(companyPrefixIndex);
		int cpiLength = sb.length();
		int requiredDigits = cpiDigits - cpiLength;
		for (int i = 0; i < requiredDigits; i++) {
			sb.insert(0,"0");
		}
		return sb.toString();
	}

	// FIXME - change bit splitting for SSCC
	public static final long parseSSCC96CPIBitsToLong(byte[] cpiBytes, byte cpiBits) {
		if (cpiBytes.length != 6) throw new IllegalArgumentException(
				"Must provide 48 bits in a 6 byte array");
		long result = 0;
		// zero first 6 bits of first byte and shift into result
		cpiBytes[0] = (byte)(cpiBytes[0] & 0x03);
		result |= cpiBytes[0] & 0xFF;
		result <<= 8;
		switch (cpiBits) {
			case 40:
				// already have the first 2 bits so take the next 38
				byte[] toParse38 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4],cpiBytes[5]};
				result = parseMSBToLong(result, toParse38, 38);
				break;
			case 37:
				// already have the first 2 bits so take the next 35
				byte[] toParse35 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4],cpiBytes[5]};
				result = parseMSBToLong(result, toParse35, 35);
				break;
			case 34:
				// already have the first 2 bits so take the next 32
				byte[] toParse32 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4]};
				result = parseMSBToLong(result, toParse32, 32);
				break;
			case 30:
				// already have the first 2 bits so take the next 28
				byte[] toParse28 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4]};
				result = parseMSBToLong(result, toParse28, 28);
				break;
			case 27:
				// already have the first 2 bits so take the next 25
				byte[] toParse25 = {cpiBytes[1],cpiBytes[2],cpiBytes[3],cpiBytes[4]};
				result = parseMSBToLong(result, toParse25, 25);
				break;
			case 24:
				// already have the first 2 bits so take the next 22
				byte[] toParse22 = {cpiBytes[1],cpiBytes[2],cpiBytes[3]};
				result = parseMSBToLong(result, toParse22, 22);
				break;
			case 20:
				// already have the first 2 bits so take the next 18
				byte[] toParse18 = {cpiBytes[1],cpiBytes[2],cpiBytes[3]};
				result = parseMSBToLong(result, toParse18, 18);
				break;
			default:
				throw new IllegalArgumentException(
					"The specified number of bits to parse is incorrect."
				);
		}
		return result;
	}
	
	// FIXME - fix bit splitting if necessary for SSCC
	public static final String parseSSCC96CPIBitsToString(long companyPrefixIndex, byte cpiDigits) {
		if (Long.toString(companyPrefixIndex).length() > cpiDigits)
			throw new IllegalArgumentException(
				"The required digits are larger than the prefix index length.");
		StringBuffer sb = new StringBuffer();
		sb.append(companyPrefixIndex);
		int cpiLength = sb.length();
		int requiredDigits = cpiDigits - cpiLength;
		for (int i = 0; i < requiredDigits; i++) {
			sb.insert(0,"0");
		}
		return sb.toString();
	}
	
	// FIXME - fix bit splitting if necessary for SSCC
	public static final String parseSSCC96SerialRefToString(long serialRef, byte srDigits) {
		if (Long.toString(serialRef).length() > srDigits)
			throw new IllegalArgumentException(
				"The required digits are larger than the prefix index length.");
		StringBuffer sb = new StringBuffer();
		sb.append(serialRef);
		int cpiLength = sb.length();
		int requiredDigits = srDigits - cpiLength;
		for (int i = 0; i < requiredDigits; i++) {
			sb.insert(0,"0");
		}
		return sb.toString();
	}
	
	public static final long parseMSBToLong(long l, byte[] b, long bits) {
		if (b.length < 1) throw new IllegalArgumentException(
			"Byte array must have something in it.");
		long capacity = b.length * 8;
		if (bits > capacity) throw new IllegalArgumentException(
			"The number of bits to parse cannot exceed the capacity of the byte array.");
		l = rollBytesIntoLong(l,b);
		// (capacity - bits) is the number we need to roll back to
		// get only the most significant bits required
		l >>= (capacity - bits);
		return l;
	}
	
	public static final long rollBytesIntoLong(long l, byte[] b) {
		if (b.length < 1) throw new IllegalArgumentException(
				"Byte array to roll must have values in it."
		);
		// iterate over the byte array
		for (int i = 0; i < b.length; i++) {
			// OR with LSB of long - effectively rolls byte into long
			l |= b[i] & 0xFF;
			// make room in the long for the next byte if there is one
			// i.e. if we're looking at anything other than the last byte,
			// shift the long left to make space.  the last byte just 
			// gets OR'd with no shifting required.
			if (i != (b.length-1)) l <<= 8;
		}
		return l;
	}

}
