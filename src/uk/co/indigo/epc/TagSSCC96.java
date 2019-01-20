/*
 * Created on 10-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * Represents an SSCC-96 tag.
 * 
 * FIXME: sc = ed + cpi + sr + cd.
 *
 * @author milbuw
 * 
 */
public class TagSSCC96 extends Tag {

	private int headerValue;
	private int filterValue;
	private int partitionValue;
	private String companyPrefixIndex;
	private String serialReference;
	private String extensionDigit;
	private String checkDigit;
	private String sscc;
	
	/**
	 * @param hv An int representation of the header value.
	 * @param fv An int representation of the filter value.
	 * @param pv An int representation of the partition value.
	 * @param cpi A String representation of the Company Prefix Index (decimal).
	 * @param sr A String representation of the Serial Reference (decimal).
	 * @param ed A String representation of the Extension Digit (decimal).
	 * @param cd A String representation of the Check Digit (decimal).
	 * @param sc A String representation of the decimal SSCC code.<BR>
	 * Needs to be refactored to calculate this as sc = ed + cpi + sr + cd.
	 */
	public TagSSCC96(int hv,
					 int fv,
					 int pv,
					 String cpi,
					 String sr,
					 String ed,
					 String cd,
					 String sc) {
		super();
		super.setHeaderLength((byte)8);
		super.setTagLength((byte)96);
		super.setEncodingScheme(EncodingSchemes.SSCC_96);
		headerValue = hv;
		filterValue = fv;
		partitionValue = pv;
		companyPrefixIndex = cpi;
		serialReference = sr;
		extensionDigit = ed;
		checkDigit = cd;
		sscc = sc;
	}

	/**
	 * @return The header value.
	 */
	public int getHeaderValue() {
		return headerValue;
	}

	/**
	 * @return The check digit.
	 */
	public String getCheckDigit() {
		return checkDigit;
	}

	/**
	 * @return The company prefix index.
	 */
	public String getCompanyPrefixIndex() {
		return companyPrefixIndex;
	}

	/**
	 * @return The extension digit.
	 */
	public String getExtensionDigit() {
		return extensionDigit;
	}

	/**
	 * @return The filter value.
	 */
	public int getFilterValue() {
		return filterValue;
	}

	/**
	 * @return The partition value.
	 */
	public int getPartitionValue() {
		return partitionValue;
	}

	/**
	 * @return The serial reference.
	 */
	public String getSerialReference() {
		return serialReference;
	}

	/**
	 * @return The SSCC decimal code.
	 */
	public String getSscc() {
		return sscc;
	}

}
