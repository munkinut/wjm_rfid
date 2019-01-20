package uk.co.indigo.epc;

/**
 * Encodes and decodes SSCC96 codes.
 * 
 * @author milbuw
 *
 */
public class SSCC96Codec {

	/**
	 * Made private to prevent instantiation (static methods only). 
	 */
	private SSCC96Codec() {}
	
	/**
	 * Encodes SSCC decimal to 96-bit binary string.
	 *  
	 * @param sscc The decimal SSCC E.g. "106141411928374657".
	 * @param L The "L" value specified by EPC spec - Company Prefix Digits.
	 * @param F The "F" value specified by EPC spec - Filter Value.
	 * @return A 96-bit binary string version of the SSCC code.
	 */
	public static String encodeSSCC96(String sscc, int L, int F) {
		PartitionSSCC96 ps96 = new PartitionSSCC96(PartitionSSCC96.COMPANY_PREFIX_DIGITS_IDX,L);
		int P = ps96.getPartitionNumber();
		int D = ps96.getSerialReferenceDigits();
		String companyPrefix = sscc.substring(1,L+1);
		long C = Long.parseLong(companyPrefix);
		StringBuffer srBuf = new StringBuffer();
		String extensionDigit = sscc.substring(0,1);
		srBuf.append(extensionDigit);
		String serialReference = sscc.substring(L+1,L+1+(D-1));
		srBuf.append(serialReference);
		long S = Long.parseLong(srBuf.toString());
		String headerBin = "00110001";

		String filter = Dec2Bin.calc("" + F);
		// add leading zeroes to make filter 3 bits
		int zeroes = 3 - filter.length();
		filter = prependZeroes(zeroes, filter);
		
		String partition = Dec2Bin.calc("" + P);
		// add leading zeroes to make partition 3 bits
		zeroes = 3 - partition.length();
		partition = prependZeroes(zeroes, partition);
		
		String cpi = Dec2Bin.calc("" + C);
		// add leading zeroes to make cpi M bits
		int M = ps96.getCompanyPrefixBits();
		zeroes = M - cpi.length();
		cpi = prependZeroes(zeroes, cpi);
		
		String serialRef = Dec2Bin.calc("" + S);
		// add leading zeroes to make sr N bits
		int N = ps96.getSerialReferenceBits();
		zeroes = N - serialRef.length();
		serialRef = prependZeroes(zeroes, serialRef);
		
		String zeroes24 = "000000000000000000000000";
		
		StringBuffer ssccStrBuf = new StringBuffer(headerBin);
		ssccStrBuf.append(filter);
		ssccStrBuf.append(partition);
		ssccStrBuf.append(cpi);
		ssccStrBuf.append(serialRef);
		ssccStrBuf.append(zeroes24);
		
		return ssccStrBuf.toString();
	}
	
	/**
	 * Encodes SSCC96 Tag object to 96-bit binary string.
	 *  
	 * @param tag A TagSSCC96 object.
	 * @return A 96-bit binary string version of the SSCC96 Tag.
	 */
	public static String encodeSSCC96(TagSSCC96 tag) {
		String cpi = tag.getCompanyPrefixIndex(); 
		int F = tag.getFilterValue();
		int P = tag.getPartitionValue();
		PartitionSSCC96 ps96 = new PartitionSSCC96(PartitionSSCC96.PARTITION_NUMBER_IDX, P);
		long C = Long.parseLong(cpi);
		String serialReference = tag.getSerialReference();
		String extensionDigit = tag.getExtensionDigit();
		StringBuffer srBuf = new StringBuffer(extensionDigit);
		srBuf.append(serialReference);
		String sr = srBuf.toString();
		long S = Long.parseLong(sr);
		
		String header = Dec2Bin.calc("" + tag.getHeaderValue());
		int zeroes = tag.getHeaderLength() - header.length();
		header = prependZeroes(zeroes, header);
		
		String filter = Dec2Bin.calc("" + F);
		zeroes = 3 - filter.length();
		filter = prependZeroes(zeroes, filter);
		
		String partition = Dec2Bin.calc("" + P);
		zeroes = 3 - partition.length();
		partition = prependZeroes(zeroes, partition);
		
		String companyPrefix = Dec2Bin.calc("" + C);
		int M = ps96.getCompanyPrefixBits();
		zeroes = M - companyPrefix.length();
		partition = prependZeroes(zeroes, partition);
		
		String serialRef = Dec2Bin.calc("" + S);
		int N = ps96.getSerialReferenceBits();
		zeroes = N - serialRef.length();
		serialRef = prependZeroes(zeroes, serialRef);

		String zeroes24 = "000000000000000000000000";
		
		StringBuffer ssccStrBuf = new StringBuffer(header);
		ssccStrBuf.append(filter);
		ssccStrBuf.append(partition);
		ssccStrBuf.append(companyPrefix);
		ssccStrBuf.append(serialRef);
		ssccStrBuf.append(zeroes24);
		
		return ssccStrBuf.toString();
	}

	/**
	 * Decodes a 96-bit binary string to a TagSSCC96 object.
	 * 
	 * @param ssccBits The 96-bit binary string.
	 * @return A TagSSCC96 object representing the SSCC code.
	 * @throws IllegalArgumentException Thrown if the input string is not valid.
	 */
	public static TagSSCC96 decodeSSCC96(String ssccBits) throws IllegalArgumentException {
		if (ssccBits.length() != 96) throw new IllegalArgumentException("SSCC96 string must be 96 characters.");
		if (!validate(ssccBits))  throw new IllegalArgumentException("SSCC96 string must contain only binary.");

		int headerValue = parseHeaderValue(ssccBits);
		
		int filterValue = parseFilterValue(ssccBits);
		
		int partitionValue = parsePartitionValue(ssccBits);

		PartitionSSCC96 part = new PartitionSSCC96(PartitionSSCC96.PARTITION_NUMBER_IDX, 
												   partitionValue);
		int M = part.getCompanyPrefixBits();
		int L = part.getCompanyPrefixDigits();
		
		String companyPrefix = parseCompanyPrefix(ssccBits, M, L);
		
		String serialReferencePlusExt = parseSerialReference(ssccBits, M, L);
		
		String extensionDigit = serialReferencePlusExt.substring(0,1);
		
		String serialReference = serialReferencePlusExt.substring(1);
		
		String ssccNoCheckDigit = extensionDigit + companyPrefix + serialReference;
		
		String checkDigit = calculateCheckDigit(ssccNoCheckDigit);
		
		String sscc = ssccNoCheckDigit + checkDigit;
		
		TagSSCC96 tag = new TagSSCC96(headerValue,
									  filterValue,
									  partitionValue,
									  companyPrefix,
									  serialReference,
									  extensionDigit,
									  checkDigit,
									  sscc);
		
		return tag;
	}
	
	/**
	 * Validates binary strings.
	 * 
	 * @param ssccBits The string to check.
	 * @return True if valid, otherwise false.
	 */
	private static boolean validate(String ssccBits) {
		return (ssccBits.matches("[01]*"));
	}
	
	/**
	 * Validates the header value against the list of known encoding schemes.
	 * 
	 * @param headerValue The header value to check.
	 * @return True if valid, otherwise false.
	 */
	private static boolean validHeader(int headerValue) {
		return (headerValue == EncodingSchemes.SSCC_96_HEADER);
	}
	
	/**
	 * Validates the filter value against the list of known encoding schemes.
	 * 
	 * @param filterValue The filter value to check.
	 * @return True if valid, otherwise false.
	 */
	private static boolean validFilter(int filterValue) {
		return (filterValue >= EncodingSchemes.SSCC_96_FILTER_MIN &&
				filterValue <= EncodingSchemes.SSCC_96_FILTER_MAX);
	}
	
	/**
	 * Validates the partition value against the list of known encoding schemes.
	 * 
	 * @param partitionValue The partition value to check.
	 * @return True if valid, otherwise false.
	 */
	private static boolean validPartition(int partitionValue) {
		return (partitionValue >= EncodingSchemes.SSCC_96_PARTITION_MIN &&
				partitionValue <= EncodingSchemes.SSCC_96_PARTITION_MAX);
	}
	
	/**
	 * Validates the company prefix.
	 * 
	 * @param companyPrefix The company prefix to check.
	 * @param L The supposed number of digits in the company prefix.
	 * @return True if valid, otherwise false.
	 */
	private static boolean validCompanyPrefix(long companyPrefix, int L) {
		return companyPrefix < Math.pow(10,L);
	}
	
	/**
	 * Validates the serial reference.
	 * 
	 * @param serialReference The company prefix to check.
	 * @param L The supposed number of digits in the company prefix.
	 * (Serial reference digits are 17 - L).
	 * @return True if valid, otherwise false.
	 */
	private static boolean validSerialReference(long serialReference, int L) {
		return serialReference < Math.pow(10,17-L);
	}
	
	/**
	 * Parses the header value from the binary sscc string.
	 * 
	 * @param ssccBits The binary string.
	 * @return An int representing the header value.
	 */
	private static int parseHeaderValue(String ssccBits) {
		String slice = ssccBits.substring(0,8);
		int headerValue = binStrToInt(slice); 
		if (!validHeader(headerValue)) throw new IllegalArgumentException("SSCC96 header is invalid.");
		return headerValue;
	}

	/**
	 * Parses the filter value from the binary sscc string.
	 * 
	 * @param ssccBits The binary string.
	 * @return An int representing the filter value;
	 */
	private static int parseFilterValue(String ssccBits) {
		String slice = ssccBits.substring(8,11);
		int filterValue = binStrToInt(slice); 
		if (!validFilter(filterValue)) throw new IllegalArgumentException("SSCC96 filter value is invalid.");
		return filterValue; 
	}
	
	/**
	 * Parses the partition value from the binary sscc string.
	 * 
	 * @param ssccBits The binary string.
	 * @return An int representing the partition value.
	 */
	private static int parsePartitionValue(String ssccBits) {
		String slice = ssccBits.substring(11,14);
		int partitionValue = binStrToInt(slice);
		if (!validPartition(partitionValue)) throw new IllegalArgumentException("SSCC partition value is invalid.");
		return partitionValue;
	}
	
	/**
	 * Parses the company prefix index from the sscc binary string.
	 * 
	 * @param ssccBits The binary string.
	 * @param M The M value defined by EPC spec.  The number of CPI digits.
	 * @param L The L value defined by EPC spec.  The number of CPI bits. 
	 * @return A string containing the CPI padded to the correct length with leading zeroes.
	 */
	private static String parseCompanyPrefix(String ssccBits, int M, int L) {
		String slice = ssccBits.substring(14,14+M);
		long sliceDecimal = binStrToLong(slice);
		if (!validCompanyPrefix(sliceDecimal, L)) throw new IllegalArgumentException("SSCC company prefix is invalid.");
		int zeroes = L - (new String("" + sliceDecimal).length());
		return prependZeroes(zeroes, sliceDecimal);
	}
	
	/**
	 * Parses the serial reference from the sscc binary string.
	 * 
	 * @param ssccBits The binary string.
	 * @param M The M value defined by EPC spec.  The number of CPI digits.
	 * (This is so you know where to slice from to get the serial reference).
	 * @param L The L value defined by EPC spec.  The number of CPI bits.
	 * @return A string containing the serial reference, padded to the correct length by leading zeroes.
	 */
	private static String parseSerialReference(String ssccBits, int M, int L) {
		String slice = ssccBits.substring(14+M,72);
		long sliceDecimal = binStrToLong(slice);
		if (!validSerialReference(sliceDecimal, L)) throw new IllegalArgumentException("SSCC company prefix is invalid.");
		int zeroes = (17-L) - (new String("" + sliceDecimal).length());
		return prependZeroes(zeroes, sliceDecimal);
	}
	
	/**
	 * Prepends the specified number of zeroes to the beginning of a string.
	 * @param zeroes The number of zeroes to prepend.
	 * @param sliceBinStr The string to prepend to.
	 * @return A string padded with leading zeroes.
	 */
	private static String prependZeroes(int zeroes, String sliceBinStr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < zeroes; i++) {
			sb.append("0");
		}
		sb.append(sliceBinStr);
		return sb.toString();
	}
	
	/**
	 * Prepends leading zeroes to a long.
	 * @param zeroes The number of zeroes to prepend.
	 * @param sliceDecimal The long value to prepend to.
	 * @return A string containing the long, padded to the correct length with leading zeroes.
	 */
	private static String prependZeroes(int zeroes, long sliceDecimal) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < zeroes; i++) {
			sb.append("0");
		}
		sb.append(sliceDecimal);
		return sb.toString();
	}
	
	/**
	 * Calculates the check digit according to the rules specified in the EPC spec.
	 * @param ssccNoCheckDigit The sscc with no check digit.
	 * @return The check digit.
	 */
	private static String calculateCheckDigit(String ssccNoCheckDigit) {
		int accA = 0;
		int accB = 0;
		for (int i = 0; i <= 16; i+=2) {
			accA += Integer.parseInt("" + ssccNoCheckDigit.charAt(i));
		}
		for (int i = 1; i <= 15; i+=2) {
			accB += Integer.parseInt("" + ssccNoCheckDigit.charAt(i));
		}
		int checkDigit = ((3 * accA) - accB)%10;
		return new Integer(checkDigit).toString();
	}
	
	/**
	 * Converts a binary string to an int.  Proxy to utility class Bin2Dec.
	 * @param slice The binary string.
	 * @return The int representation of the binary string.
	 */
	private static int binStrToInt(String slice) {
		return Bin2Dec.calc(slice).intValue();
	}
	
	/**
	 * Converts a binary string to a long.  Proxy to utility class Bin2Dec.
	 * @param slice The binary string.
	 * @return The long representation of the binary string.
	 */
	private static long binStrToLong(String slice) {
		return Bin2Dec.calc(slice).longValue();
	}
	
}
