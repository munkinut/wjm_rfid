/*
 * Created on 05-May-2005
 *
 */
package uk.co.indigo.epc;

/**
 * @author milbuw
 *
 */
public class Tag {
	
	private byte headerLength;
	private byte tagLength;
	private String encodingScheme;

	/**
	 * Tag
	 * 
	 * Object representation of an EPC.
	 */
	public Tag() {
		super();
		headerLength = 0;
		tagLength = 0;
		encodingScheme = "";
	}

	/**
	 * Tag
	 * 
	 * Object representation of an EPC.
	 * 
	 * @param headerLength The number of bits in the header
	 * @param tagLength The number of bits in the tag
	 */
	public Tag(byte headerLength, byte tagLength) {
		super();
		this.headerLength = headerLength;
		this.tagLength = tagLength;
		this.encodingScheme = "";
	}

	/**
	 * Tag
	 * 
	 * Object representation of an EPC.
	 * 
	 * @param headerLength The number of bits in the header
	 * @param tagLength The number of bits in the tag
	 * @param encodingScheme The encoding scheme indentifier
	 */
	public Tag(byte headerLength, byte tagLength, String encodingScheme) {
		super();
		this.headerLength = headerLength;
		this.tagLength = tagLength;
		this.encodingScheme = encodingScheme;
	}

	/**
	 * @return Returns the headerLength.
	 */
	public byte getHeaderLength() {
		return headerLength;
	}
	
	/**
	 * @param headerLength The headerLength to set.
	 */
	public void setHeaderLength(byte headerLength) {
		this.headerLength = headerLength;
	}
	
	/**
	 * @return Returns the tagLength.
	 */
	public byte getTagLength() {
		return tagLength;
	}
	
	/**
	 * @param tagLength The tagLength to set.
	 */
	public void setTagLength(byte tagLength) {
		this.tagLength = tagLength;
	}
	
	/**
	 * @return Returns the encodingScheme.
	 */
	public String getEncodingScheme() {
		return encodingScheme;
	}
	
	/**
	 * @param encodingScheme The encodingScheme to set.
	 */
	public void setEncodingScheme(String encodingScheme) {
		this.encodingScheme = encodingScheme;
	}
}
