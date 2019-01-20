/*
 * Created on 05-May-2005
 *
 */
package uk.co.indigo.epc;

import java.util.Arrays;
// import java.util.BitSet;

// import uk.co.indigo.play.bitset.BitSetConverter;

/**
 * @author milbuw
 *
 */
public class EPCParser {
	
	
	public static int[] VALID_BYTES_IN = {8,12};

	private EPCParser() {
		super();
	}
	
	/**
	 * parse - The main entry point to this class.
	 * 
	 * @param epc The byte array to parse. 96 bits = 12 bytes, 64 bits = 8 bytes
	 * @return Tag A Tag object describing the content of the EPC
	 */
	public static Tag parse(byte[] epc) {
		
		byte headerLength = 0;
		byte tagLength = 0;
		byte first2Bits = 0;
		byte first5Bits = 0;
		String encodingScheme;
		
		// byte array must be 64 or 96 bit
		validateBytes(epc.length);
		
		// if the first 2 high order bits of the first byte are 00
		// the header is 8 bits in length, otherwise it is 2 bits
		first2Bits = BitUtils.evalHOBits(epc[0],(byte)2);
		if (first2Bits == 0x00) {
			headerLength = 8;
			encodingScheme = parse8BitEncoding(epc[0]);
			// if the first 5 bits of an 8 bit header are 00001
			// the tag length is 64 otherwise it is 96
			first5Bits = BitUtils.evalHOBits(epc[0],(byte)5);
			if (first5Bits == 0x01) tagLength = 64;
			else tagLength = 96;
		}
		else {
			// the first 2 bits are non-zero, so the header is 2 bits
			// and the tag length is 64 bits
			headerLength = 2;
			tagLength = 64;
			encodingScheme = parse2BitEncoding(first2Bits);
		}
		
		Tag tag = new Tag();
		tag.setHeaderLength(headerLength);
		tag.setTagLength(tagLength);
		tag.setEncodingScheme(encodingScheme);
		
		// for each encoding scheme parse it according 
		// which scheme it conforms to
		tag = parsePerEncoding(tag, epc);
		
		return tag;
	}
	
	private static void validateBytes(int numBytes) {
		Arrays.sort(VALID_BYTES_IN);
		if (Arrays.binarySearch(VALID_BYTES_IN, numBytes) < 0) 
			throw new IllegalArgumentException("The number of bytes passed in is invalid.");
	}

	private static String parse2BitEncoding(byte header) {
		String encodingScheme;
		switch (header) {
			case 0x01:
				encodingScheme = EncodingSchemes.RESERVED_64;
				break;
			case 0x02:
				encodingScheme = EncodingSchemes.SGTIN_64;
				break;
			case 0x03:
				encodingScheme = EncodingSchemes.RESERVED_64;
				break;
			default:
				throw new IllegalArgumentException("A 2-bit encoding scheme can only be 01,10 or 11.");
		}
		return encodingScheme;
	}
	
	private static String parse8BitEncoding(byte header) {
		String encodingScheme;
		if (header == 0x00) encodingScheme = EncodingSchemes.RESERVED;
		else if (header >= 0x01 && header <= 0x07) encodingScheme = EncodingSchemes.RESERVED;
		else if (header == 0x08) encodingScheme = EncodingSchemes.SSCC_64;
		else if (header == 0x09) encodingScheme = EncodingSchemes.GLN_64;
		else if (header == 0x0A) encodingScheme = EncodingSchemes.GRAI_64;
		else if (header == 0x0B) encodingScheme = EncodingSchemes.GIAI_64;
		else if (header >= 0x0C && header <= 0x0F) encodingScheme = EncodingSchemes.RESERVED_64;
		else if (header >= 0x10 && header <= 0x2F) encodingScheme = EncodingSchemes.RESERVED;
		else if (header == 0x30) encodingScheme = EncodingSchemes.SGTIN_96;
		else if (header == 0x31) encodingScheme = EncodingSchemes.SSCC_96;
		else if (header == 0x32) encodingScheme = EncodingSchemes.GLN_96;
		else if (header == 0x33) encodingScheme = EncodingSchemes.GRAI_96;
		else if (header == 0x34) encodingScheme = EncodingSchemes.GIAI_96;
		else if (header == 0x35) encodingScheme = EncodingSchemes.GID_96;
		else if (header >= 0x36 && header <= 0x3F) encodingScheme = EncodingSchemes.RESERVED_96;
		else throw new IllegalArgumentException("An 8-bit encoding scheme must conform to EPC spec.");
		return encodingScheme;
	}
	
	private static Tag parsePerEncoding(Tag tag, byte[] epc) {
		String encodingScheme = tag.getEncodingScheme();
		if (encodingScheme.equals(EncodingSchemes.GID_96)) {
			tag = parseGID96(tag, epc);
		}
		else if (encodingScheme.equals(EncodingSchemes.SGTIN_64)) {
			tag = parseSGTIN64(tag, epc);
		}
		else if (encodingScheme.equals(EncodingSchemes.SGTIN_96)) {
			// tag = parseSGTIN96(tag, epc);
		}
		else if (encodingScheme.equals(EncodingSchemes.SSCC_64)) {
			tag = parseSSCC64(tag, epc);
		}
		else if (encodingScheme.equals(EncodingSchemes.SSCC_96)) {
			tag = parseSSCC96(tag, epc);
		}
		return tag;
	}
	
	private static Tag parseGID96(Tag tag, byte[] epc) {
		TagGID96 tg96 = new TagGID96(tag.getHeaderLength(),
				                     tag.getTagLength(),
									 tag.getEncodingScheme());
		// start at byte[1] and eval 28 bits for the general manager number
		byte[] gmbytes = {epc[1],epc[2],epc[3],epc[4]};
		int i;
		i = BitUtils.parseFirst28BitsToInt(gmbytes);
		tg96.setGeneralManagerNumber(i);
		// start at byte[4] and eval last 24 bits for the object class
		byte[] ocbytes = {epc[4],epc[5],epc[6],epc[7]};
		i = BitUtils.parseMiddle24BitsToInt(ocbytes);
		tg96.setObjectClass(i);
		// start at byte[7] and eval the last 36 bits for the serial number
		byte[] snbytes = {epc[7],epc[8],epc[9],epc[10],epc[11]};
		long l;
		l = BitUtils.parseLast36BitsToLong(snbytes);
		tg96.setSerialNumber(l);
		return tg96;
	}
	
	// FIXME - ABORT ABORT
	private static Tag parseSSCC64(Tag tag, byte[] epc) {
		return tag;
	}

	// FIXME - ABORT ABORT!!
	private static Tag parseSSCC96(Tag tag, byte[] epc) {
		return tag;
	}                                                                          
	
	private static Tag parseSGTIN64(Tag tag, byte[] epc) {
		TagSGTIN64 tsg64 = new TagSGTIN64(tag.getHeaderLength(),
				                          tag.getTagLength(),
										  tag.getEncodingScheme());
		byte filterByte = epc[0];
		byte filterValue = BitUtils.parseSGTIN64FilterBitsToByte(filterByte);
		tsg64.setFilterValue(filterValue);
		byte[] cpiBytes = {epc[0], epc[1], epc[2]};
		short companyPrefixIndex = BitUtils.parseSGTIN64CPIBitsToShort(cpiBytes);
		tsg64.setCompanyPrefixIndex(companyPrefixIndex);
		byte[] irBytes = {epc[2], epc[3], epc[4]};
		int itemReference = BitUtils.parseSGTIN64IRBitsToInt(irBytes);
		tsg64.setItemReference(itemReference);
		byte[] snBytes = {epc[4], epc[5], epc[6], epc[7]};
		int serialNumber = BitUtils.parseSGTIN64SNBitsToInt(snBytes);
		tsg64.setSerialNumber(serialNumber);
		return tsg64;
	}

/*	
	private static Tag parseSGTIN96(Tag tag, byte[] epc) {
		TagSGTIN96 tsg96 = new TagSGTIN96(tag.getHeaderLength(),
				                          tag.getTagLength(),
										  tag.getEncodingScheme());
		byte filterByte = epc[1];
		byte filterValue = BitUtils.parseSGTIN96FilterBitsToByte(filterByte);
		tsg96.setFilterValue(filterValue);
		byte partitionByte = epc[1];
		byte partitionValue = BitUtils.parseSGTIN96PartitionBitsToByte(partitionByte);
		tsg96.setPartitionValue(partitionValue);
		PartitionSGTIN96 p = new PartitionSGTIN96(partitionValue);
		byte cpiBits = p.getCompanyPrefixBits();
		byte cpiDigits = p.getCompanyPrefixDigits();
		byte[] cpiBytes = {epc[1], epc[2], epc[3], epc[4], epc[5], epc[6]};
		long companyPrefixIndex = BitUtils.parseSGTIN96CPIBitsToLong(cpiBytes, cpiBits);
		if (companyPrefixIndex > Math.pow(10,cpiDigits))
			throw new IllegalArgumentException(
				"The input does not contain a legal SGTIN-96 encoding.");
		String companyPrefixIndexStr = BitUtils.parseSGTIN96CPIBitsToString(companyPrefixIndex, cpiDigits);
		tsg96.setCompanyPrefixIndexString(companyPrefixIndexStr);
		
		BitSet epcBits = BitSetConverter.fromByteArray(epc);
		int itemRefFrom = (EncodingSchemes.SGTIN_96_HEADER_LEN + 
						   EncodingSchemes.SGTIN_96_FILTER_LEN + 
						   EncodingSchemes.SGTIN_96_PARTITION_LEN + 
		                   cpiBits);
		int itemRefTo = itemRefFrom + p.getItemReferenceBits();
		BitSet irBits = BitSetConverter.carve(epcBits, itemRefFrom, itemRefTo);
		tsg96.setItemReference(BitSetConverter.toInt(irBits));
		
		int serialNumberFrom = (EncodingSchemes.SGTIN_96_HEADER_LEN +
								EncodingSchemes.SGTIN_96_FILTER_LEN +
								EncodingSchemes.SGTIN_96_PARTITION_LEN +
		        				cpiBits +
		        				p.getItemReferenceBits());
		int serialNumberTo = 96;
		BitSet serialNumberBits = BitSetConverter.carve(epcBits, serialNumberFrom, serialNumberTo);
		tsg96.setSerialNumber(BitSetConverter.toLong(serialNumberBits));
		return tsg96;
	}
*/
}
