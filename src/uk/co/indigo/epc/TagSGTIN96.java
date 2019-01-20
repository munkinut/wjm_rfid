/*
 * Created on 13-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class TagSGTIN96 extends Tag {

	private int headerValue;
	private int filterValue;
	private int partitionValue;
	private String companyPrefixIndex;
	private String itemReference;
	private String indicatorDigit;
	private String checkDigit;
	private String serialNumber;
	
	public TagSGTIN96(int headerValue,
			  		  int filterValue, 
					  int partitionValue, 
					  String companyPrefixIndex, 
					  String itemReference, 
					  String indicatorDigit, 
					  String checkDigit, 
					  String serialNumber) {
		super();
		super.setHeaderLength((byte)8);
		super.setTagLength((byte)96);
		super.setEncodingScheme(EncodingSchemes.SGTIN_96);
		this.checkDigit = checkDigit;
		this.companyPrefixIndex = companyPrefixIndex;
		this.indicatorDigit = indicatorDigit;
		this.filterValue = filterValue;
		this.headerValue = headerValue;
		this.itemReference = itemReference;
		this.partitionValue = partitionValue;
		this.serialNumber = serialNumber;
	}

	public String getCheckDigit() {
		return checkDigit;
	}

	public String getCompanyPrefixIndex() {
		return companyPrefixIndex;
	}

	public String getIndicatorDigit() {
		return indicatorDigit;
	}

	public int getFilterValue() {
		return filterValue;
	}

	public int getHeaderValue() {
		return headerValue;
	}

	public String getItemReference() {
		return itemReference;
	}

	public int getPartitionValue() {
		return partitionValue;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

}
