package uk.co.indigo.epc;

public class EncodingSchemes {

	// Encoding scheme names
	// Reserved
	public static final String RESERVED = "[Reserved Scheme]";
	public static final String RESERVED_64 = "[Reserved 64-bit Scheme]";
	public static final String RESERVED_96 = "[Reserved 96-bit Scheme]";
	// 64-bit schemes
	public static final String SGTIN_64 = "SGTIN-64";
	public static final String SSCC_64 = "SSCC-64";
	public static final String GLN_64 = "GLN-64";
	public static final String GRAI_64 = "GRAI-64";
	public static final String GIAI_64 = "GIAI-64";
	// 96-bit schemes
	public static final String SGTIN_96 = "SGTIN-96";
	public static final String SSCC_96 = "SSCC-96";
	public static final String GLN_96 = "GLN-96";
	public static final String GRAI_96 = "GRAI-96";
	public static final String GIAI_96 = "GIAI-96";
	public static final String GID_96 = "GID-96";
	
	public static final int SGTIN_96_HEADER_LEN = 8;
	public static final int SGTIN_96_PARTITION_LEN = 3;
	public static final int SGTIN_96_FILTER_LEN = 3;
	
	public static final int SSCC_96_HEADER_LEN = 8;
	public static final int SSCC_96_PARTITION_LEN = 3;
	public static final int SSCC_96_FILTER_LEN = 3;
	
	public static final int SSCC_96_HEADER = 0x31;
	public static final int SGTIN_96_HEADER = 0x30;
	
	public static final int SSCC_96_FILTER_MIN = 0x00;
	public static final int SSCC_96_FILTER_MAX = 0x07;

	public static final int SGTIN_96_FILTER_MIN = 0x00;
	public static final int SGTIN_96_FILTER_MAX = 0x07;

	public static final int SSCC_96_PARTITION_MIN = 0x00;
	public static final int SSCC_96_PARTITION_MAX = 0x06;

	public static final int SGTIN_96_PARTITION_MIN = 0x00;
	public static final int SGTIN_96_PARTITION_MAX = 0x06;

	private EncodingSchemes() {}
	
	
}
