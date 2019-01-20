/*
 * Created on 16-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class PartitionSSCC96 {

	private int companyPrefixBits;
	private int companyPrefixDigits;
	private int serialReferenceBits;
	private int serialReferenceDigits;
	private int partitionNumber;
	
	public static final int PARTITION_NUMBER_IDX = 0;
	public static final int COMPANY_PREFIX_DIGITS_IDX = 1;
	
	public PartitionSSCC96(int index, int value) {
		
		switch (index) {
			case PARTITION_NUMBER_IDX:
				initFromPartition(value);
				break;
			case COMPANY_PREFIX_DIGITS_IDX:
				initFromCPD(value);
				break;
			default:
				// should never reach here
				throw new IllegalArgumentException("Index must be between 0 and 1 inclusive.");
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
				serialReferenceBits = 18;
				serialReferenceDigits = 5;
				break;
			case 1:
				companyPrefixBits = 37;
				companyPrefixDigits = 11;
				serialReferenceBits = 21;
				serialReferenceDigits = 6;
				break;
			case 2:
				companyPrefixBits = 34;
				companyPrefixDigits = 10;
				serialReferenceBits = 24;
				serialReferenceDigits = 7;
				break;
			case 3:
				companyPrefixBits = 30;
				companyPrefixDigits = 9;
				serialReferenceBits = 28;
				serialReferenceDigits = 8;
				break;
			case 4:
				companyPrefixBits = 27;
				companyPrefixDigits = 8;
				serialReferenceBits = 31;
				serialReferenceDigits = 9;
				break;
			case 5:
				companyPrefixBits = 24;
				companyPrefixDigits = 7;
				serialReferenceBits = 34;
				serialReferenceDigits = 10;
				break;
			case 6:
				companyPrefixBits = 20;
				companyPrefixDigits = 6;
				serialReferenceBits = 38;
				serialReferenceDigits = 11;
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
				serialReferenceBits = 18;
				serialReferenceDigits = 5;
				break;
			case 11:
				companyPrefixBits = 37;
				partitionNumber = 1;
				serialReferenceBits = 21;
				serialReferenceDigits = 6;
				break;
			case 10:
				companyPrefixBits = 34;
				partitionNumber = 2;
				serialReferenceBits = 24;
				serialReferenceDigits = 7;
				break;
			case 9:
				companyPrefixBits = 30;
				partitionNumber = 3;
				serialReferenceBits = 28;
				serialReferenceDigits = 8;
				break;
			case 8:
				companyPrefixBits = 27;
				partitionNumber = 4;
				serialReferenceBits = 31;
				serialReferenceDigits = 9;
				break;
			case 7:
				companyPrefixBits = 24;
				partitionNumber = 5;
				serialReferenceBits = 34;
				serialReferenceDigits = 10;
				break;
			case 6:
				companyPrefixBits = 20;
				partitionNumber = 6;
				serialReferenceBits = 38;
				serialReferenceDigits = 11;
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
	public int getSerialReferenceBits() {
		return serialReferenceBits;
	}

	/**
	 * @return Returns the serialReferenceDigits.
	 */
	public int getSerialReferenceDigits() {
		return serialReferenceDigits;
	}

	/**
	 * @return Returns the partitionNumber.
	 */
	public int getPartitionNumber() {
		return partitionNumber;
	}

}
