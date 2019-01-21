/*
 * Created on 16-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class PartitionSGTIN96 {

	private int companyPrefixBits;
	private int companyPrefixDigits;
	private int itemReferenceBits;
	private int itemReferenceDigits;
	private int partitionNumber;
	
	public static final int PARTITION_NUMBER_IDX = 0;
	public static final int COMPANY_PREFIX_DIGITS_IDX = 1;
	
	public PartitionSGTIN96(int index, int value) {
		
		switch (index) {
			case PARTITION_NUMBER_IDX:
				initFromPartition(value);
				break;
			case COMPANY_PREFIX_DIGITS_IDX:
				initFromCPD(value);
				break;
			default:
				// should never reach here
				throw new IllegalArgumentException("Index must be between 1 and 1 inclusive.");
		}
		
	}

	private void initFromPartition(int partitionNumber) {
		if (partitionNumber < 0 || partitionNumber > 6) 
			throw new IllegalArgumentException("Partition number must be between 0 and 6 inclusive.");

		this.partitionNumber = partitionNumber;
		
		switch (partitionNumber) {
			case 0:
				companyPrefixBits = 40;
				companyPrefixDigits = 12;
				itemReferenceBits = 4;
				itemReferenceDigits = 1;
				break;
			case 1:
				companyPrefixBits = 37;
				companyPrefixDigits = 11;
				itemReferenceBits = 7;
				itemReferenceDigits = 2;
				break;
			case 2:
				companyPrefixBits = 34;
				companyPrefixDigits = 10;
				itemReferenceBits = 10;
				itemReferenceDigits = 3;
				break;
			case 3:
				companyPrefixBits = 30;
				companyPrefixDigits = 9;
				itemReferenceBits = 14;
				itemReferenceDigits = 4;
				break;
			case 4:
				companyPrefixBits = 27;
				companyPrefixDigits = 8;
				itemReferenceBits = 17;
				itemReferenceDigits = 5;
				break;
			case 5:
				companyPrefixBits = 24;
				companyPrefixDigits = 7;
				itemReferenceBits = 20;
				itemReferenceDigits = 6;
				break;
			case 6:
				companyPrefixBits = 20;
				companyPrefixDigits = 6;
				itemReferenceBits = 24;
				itemReferenceDigits = 7;
				break;
			default:
				// should never reach here
				break;
		}
	}
	
	private void initFromCPD(int companyPrefixDigits) {

		this.companyPrefixDigits = companyPrefixDigits;
		
		switch (companyPrefixDigits) {
			case 12:
				companyPrefixBits = 40;
				partitionNumber = 0;
				itemReferenceBits = 4;
				itemReferenceDigits = 1;
				break;
			case 11:
				companyPrefixBits = 37;
				partitionNumber = 1;
				itemReferenceBits = 7;
				itemReferenceDigits = 2;
				break;
			case 10:
				companyPrefixBits = 34;
				partitionNumber = 2;
				itemReferenceBits = 10;
				itemReferenceDigits = 3;
				break;
			case 9:
				companyPrefixBits = 30;
				partitionNumber = 3;
				itemReferenceBits = 14;
				itemReferenceDigits = 4;
				break;
			case 8:
				companyPrefixBits = 27;
				partitionNumber = 4;
				itemReferenceBits = 17;
				itemReferenceDigits = 5;
				break;
			case 7:
				companyPrefixBits = 24;
				partitionNumber = 5;
				itemReferenceBits = 20;
				itemReferenceDigits = 6;
				break;
			case 6:
				companyPrefixBits = 20;
				partitionNumber = 6;
				itemReferenceBits = 24;
				itemReferenceDigits = 7;
				break;
			default:
				// should never reach here
				throw new IllegalArgumentException("Invalid company prefix length.");
		}
		
	}
	
	/**
	 * @return Returns the companyPrefixBits.
	 */

	public int getCompanyPrefixBits() {
		return companyPrefixBits;
	}

	/**
	 * @return Returns the companyPrefixDigits.
	 */
	public int getCompanyPrefixDigits() {
		return companyPrefixDigits;
	}

	/**
	 * @return Returns the serialReferenceBits.
	 */
	public int getItemReferenceBits() {
		return itemReferenceBits;
	}

	/**
	 * @return Returns the serialReferenceDigits.
	 */
	public int getItemReferenceDigits() {
		return itemReferenceDigits;
	}

	/**
	 * @return Returns the partitionNumber.
	 */
	public int getPartitionNumber() {
		return partitionNumber;
	}

}
