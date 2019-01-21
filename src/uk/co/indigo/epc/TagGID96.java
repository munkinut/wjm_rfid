/*
 * Created on 10-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class TagGID96 extends Tag {

	private int generalManagerNumber;
	private int objectClass;
	private long serialNumber;

	/**
	 * 
	 */
	public TagGID96() {
		super();
		generalManagerNumber = 0;
		objectClass = 0;
		serialNumber = 0;
	}

	/**
	 * @param headerLength
	 * @param tagLength
	 */
	public TagGID96(byte headerLength, byte tagLength) {
		super(headerLength, tagLength);
		generalManagerNumber = 0;
		objectClass = 0;
		serialNumber = 0;
	}

	/**
	 * @param headerLength
	 * @param tagLength
	 */
	public TagGID96(byte headerLength, byte tagLength, String encodingScheme) {
		super(headerLength, tagLength, encodingScheme);
		generalManagerNumber = 0;
		objectClass = 0;
		serialNumber = 0;
	}

	/**
	 * @return Returns the generalManagerNumber.
	 */
	public int getGeneralManagerNumber() {
		return generalManagerNumber;
	}

	/**
	 * @param generalManagerNumber The generalManagerNumber to set.
	 */
	public void setGeneralManagerNumber(int generalManagerNumber) {
		this.generalManagerNumber = generalManagerNumber;
	}

	/**
	 * @return Returns the objectClass.
	 */
	public int getObjectClass() {
		return objectClass;
	}

	/**
	 * @param objectClass The objectClass to set.
	 */
	public void setObjectClass(int objectClass) {
		this.objectClass = objectClass;
	}

	/**
	 * @return Returns the serialNumber.
	 */
	public long getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber The serialNumber to set.
	 */
	public void setSerialNumber(long serialNumber) {
		this.serialNumber = serialNumber;
	}

}
