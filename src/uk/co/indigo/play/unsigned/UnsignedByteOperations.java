/*
 * Created on 07-Jun-2005
 *
 */
package uk.co.indigo.play.unsigned;

/**
 * @author milbuw
 *
 */
public interface UnsignedByteOperations {
	
	public UnsignedByte and(UnsignedByte uByte);

	public UnsignedByte or(UnsignedByte uByte);
	
	public UnsignedByte xor(UnsignedByte uByte);
	
	public UnsignedByte complement();
	
	public UnsignedByte lshift(int places);
	
	public UnsignedByte rshift(int places);
	
	public UnsignedByte lrotate(int places);
	
	public UnsignedByte rrotate(int places);
	
}
