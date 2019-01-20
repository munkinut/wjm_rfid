/*
 * Created on 10-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class TagSSCC64 extends Tag {

	private byte filterValue;
	private short companyPrefixIndex;
	private long serialReference;
	private int leadingZeroes;
	
	public TagSSCC64() {
		super();
		filterValue = 0;
		companyPrefixIndex = 0;
		serialReference = 0;
		leadingZeroes = 0;
	}

	/**
	 * @param headerLength
	 * @param tagLength
	 */
	public TagSSCC64(byte headerLength, byte tagLength) {
		super(headerLength, tagLength);
		filterValue = 0;
		companyPrefixIndex = 0;
		serialReference = 0;
		leadingZeroes = 0;
	}

	/**
	 * @param headerLength
	 * @param tagLength
	 * @param encodingScheme
	 */
	public TagSSCC64(byte headerLength, byte tagLength, String encodingScheme) {
		super(headerLength, tagLength, encodingScheme);
		filterValue = 0;
		companyPrefixIndex = 0;
		serialReference = 0;
		leadingZeroes = 0;
	}

	/**
	 * @return Returns the filterValue.
	 */
	public byte getFilterValue() {
		return filterValue;
	}
	
	/**
	 * @param filterValue The filterValue to set.
	 */
	public void setFilterValue(byte filterValue) {
		this.filterValue = filterValue;
	}
	
	/**
	 * @return Returns the companyPrefixIndex.
	 */
	public short getCompanyPrefixIndex() {
		return companyPrefixIndex;
	}
	
	/**
	 * @param companyPrefixIndex The companyPrefixIndex to set.
	 */
	public void setCompanyPrefixIndex(short companyPrefixIndex) {
		this.companyPrefixIndex = companyPrefixIndex;
	}
	
	/**
	 * @return Returns the serialNumber.
	 */
	public long getSerialReference() {
		return serialReference;
	}
	
	/**
	 * @param serialReference The  serial reference to set.
	 */
	public void setSerialReference(long serialReference) {
		this.serialReference = serialReference;
	}

	public int getLeadingZeroes() {
		return leadingZeroes;
	}

	public void setLeadingZeroes(int leadingZeroes) {
		this.leadingZeroes = leadingZeroes;
	}
}
