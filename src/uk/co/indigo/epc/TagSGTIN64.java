/*
 * Created on 10-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class TagSGTIN64 extends Tag {

	private byte filterValue;
	private short companyPrefixIndex;
	private int itemReference;
	private int serialNumber;
	
	public TagSGTIN64() {
		super();
		filterValue = 0;
		companyPrefixIndex = 0;
		itemReference = 0;
		serialNumber = 0;
	}

	/**
	 * @param headerLength
	 * @param tagLength
	 */
	public TagSGTIN64(byte headerLength, byte tagLength) {
		super(headerLength, tagLength);
		filterValue = 0;
		companyPrefixIndex = 0;
		itemReference = 0;
		serialNumber = 0;
	}

	/**
	 * @param headerLength
	 * @param tagLength
	 * @param encodingScheme
	 */
	public TagSGTIN64(byte headerLength, byte tagLength, String encodingScheme) {
		super(headerLength, tagLength, encodingScheme);
		filterValue = 0;
		companyPrefixIndex = 0;
		itemReference = 0;
		serialNumber = 0;
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
	 * @return Returns the itemReference.
	 */
	public int getItemReference() {
		return itemReference;
	}
	
	/**
	 * @param itemReference The itemReference to set.
	 */
	public void setItemReference(int itemReference) {
		this.itemReference = itemReference;
	}
	
	/**
	 * @return Returns the serialNumber.
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	
	/**
	 * @param serialNumber The serialNumber to set.
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
}
