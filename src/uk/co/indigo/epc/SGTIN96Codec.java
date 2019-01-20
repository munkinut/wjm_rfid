package uk.co.indigo.epc;

public class SGTIN96Codec {

	private SGTIN96Codec() {}
	
	public static String encodeSGTIN96(String sscc, int L, int F) {
		PartitionSGTIN96 ps96 = new PartitionSGTIN96(PartitionSGTIN96.COMPANY_PREFIX_DIGITS_IDX,L);
		int P = ps96.getPartitionNumber();
		int D = ps96.getItemReferenceDigits();
		String companyPrefix = sscc.substring(1,L+1);
		long C = Long.parseLong(companyPrefix);
		StringBuffer srBuf = new StringBuffer();
		String extensionDigit = sscc.substring(0,1);
		srBuf.append(extensionDigit);
		String itemReference = sscc.substring(L+1,L+1+(D-1));
		srBuf.append(itemReference);
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
		int N = ps96.getItemReferenceBits();
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
	
	public static String encodeSGTIN96(TagSGTIN96 tag) {
		String cpi = tag.getCompanyPrefixIndex(); 
		int F = tag.getFilterValue();
		int P = tag.getPartitionValue();
		PartitionSGTIN96 ps96 = new PartitionSGTIN96(PartitionSGTIN96.PARTITION_NUMBER_IDX, P);
		long C = Long.parseLong(cpi);
		String itemReference = tag.getItemReference();
		String indicatorDigit = tag.getIndicatorDigit();
		StringBuffer srBuf = new StringBuffer(indicatorDigit);
		srBuf.append(itemReference);
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
		
		String itemRef = Dec2Bin.calc("" + S);
		int N = ps96.getItemReferenceBits();
		zeroes = N - itemRef.length();
		itemRef = prependZeroes(zeroes, itemRef);

		String zeroes24 = "000000000000000000000000";
		
		StringBuffer sgtinStrBuf = new StringBuffer(header);
		sgtinStrBuf.append(filter);
		sgtinStrBuf.append(partition);
		sgtinStrBuf.append(companyPrefix);
		sgtinStrBuf.append(itemRef);
		sgtinStrBuf.append(zeroes24);
		
		return sgtinStrBuf.toString();
	}

	public static TagSGTIN96 decodeSGTIN96(String sgtinBits) {
		if (sgtinBits.length() != 96) throw new IllegalArgumentException("SGTIN96 string must be 96 characters.");
		if (!validate(sgtinBits))  throw new IllegalArgumentException("SGTIN96 string must contain only binary.");

		int headerValue = parseHeaderValue(sgtinBits);
		
		int filterValue = parseFilterValue(sgtinBits);
		
		int partitionValue = parsePartitionValue(sgtinBits);

		PartitionSGTIN96 part = new PartitionSGTIN96(PartitionSGTIN96.PARTITION_NUMBER_IDX, 
												   	 partitionValue);
		int M = part.getCompanyPrefixBits();
		int L = part.getCompanyPrefixDigits();
		
		String companyPrefix = parseCompanyPrefix(sgtinBits, M, L);
		
		String itemReferencePlusExt = parseItemReference(sgtinBits, M, L);
		
		// FIXME - calculate EAN.UCC GTIN-14 - step 6,7,8 of decode proc
		
		// FIXME - calculate serial number - step 9
		
		// FIXME - calculate EAN-128 Application Identifier 21 - step 10
		
		String extensionDigit = itemReferencePlusExt.substring(0,1);
		
		String itemReference = itemReferencePlusExt.substring(1);
		
		String ssccNoCheckDigit = extensionDigit + companyPrefix + itemReference;
		
		String checkDigit = calculateCheckDigit(ssccNoCheckDigit);
		
		String sscc = ssccNoCheckDigit + checkDigit;
		
		TagSGTIN96 tag = new TagSGTIN96(headerValue,
									  filterValue,
									  partitionValue,
									  companyPrefix,
									  itemReference,
									  extensionDigit,
									  checkDigit,
									  sscc);
		
		return tag;
	}
	
	private static boolean validate(String sgtinBits) {
		return (sgtinBits.matches("[01]*"));
	}
	
	private static boolean validHeader(int headerValue) {
		return (headerValue == EncodingSchemes.SGTIN_96_HEADER);
	}
	
	private static boolean validFilter(int filterValue) {
		return (filterValue >= EncodingSchemes.SGTIN_96_FILTER_MIN &&
				filterValue <= EncodingSchemes.SGTIN_96_FILTER_MAX);
	}
	
	private static boolean validPartition(int partitionValue) {
		return (partitionValue >= EncodingSchemes.SGTIN_96_PARTITION_MIN &&
				partitionValue <= EncodingSchemes.SGTIN_96_PARTITION_MAX);
	}
	
	private static boolean validCompanyPrefix(long companyPrefix, int L) {
		return companyPrefix < Math.pow(10,L);
	}
	
	private static boolean validItemReference(long itemReference, int L) {
		return itemReference < Math.pow(10,13-L);
	}
	
	private static int parseHeaderValue(String sgtinBits) {
		String slice = sgtinBits.substring(0,8);
		int headerValue = binStrToInt(slice); 
		if (!validHeader(headerValue)) throw new IllegalArgumentException("SGTIN96 header is invalid.");
		return headerValue;
	}

	private static int parseFilterValue(String sgtinBits) {
		String slice = sgtinBits.substring(8,11);
		int filterValue = binStrToInt(slice); 
		if (!validFilter(filterValue)) throw new IllegalArgumentException("SGTIN96 filter value is invalid.");
		return filterValue; 
	}
	
	private static int parsePartitionValue(String sgtinBits) {
		String slice = sgtinBits.substring(11,14);
		int partitionValue = binStrToInt(slice);
		if (!validPartition(partitionValue)) throw new IllegalArgumentException("SGTIN partition value is invalid.");
		return partitionValue;
	}
	
	private static String parseCompanyPrefix(String sgtinBits, int M, int L) {
		String slice = sgtinBits.substring(14,14+M);
		long sliceDecimal = binStrToLong(slice);
		if (!validCompanyPrefix(sliceDecimal, L)) throw new IllegalArgumentException("SGTIN company prefix is invalid.");
		int zeroes = L - (new String("" + sliceDecimal).length());
		return prependZeroes(zeroes, sliceDecimal);
	}
	
	private static String parseItemReference(String sgtinBits, int M, int L) {
		String slice = sgtinBits.substring(14+M,58);
		long sliceDecimal = binStrToLong(slice);
		if (!validItemReference(sliceDecimal, L)) throw new IllegalArgumentException("SGTIN company prefix is invalid.");
		int zeroes = (13-L) - (new String("" + sliceDecimal).length());
		return prependZeroes(zeroes, sliceDecimal);
	}
	
	private static String prependZeroes(int zeroes, String sliceBinStr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < zeroes; i++) {
			sb.append("0");
		}
		sb.append(sliceBinStr);
		return sb.toString();
	}
	
	private static String prependZeroes(int zeroes, long sliceDecimal) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < zeroes; i++) {
			sb.append("0");
		}
		sb.append(sliceDecimal);
		return sb.toString();
	}
	
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
	
	private static int binStrToInt(String slice) {
		return Bin2Dec.calc(slice).intValue();
	}
	
	private static long binStrToLong(String slice) {
		return Bin2Dec.calc(slice).longValue();
	}
	
}
