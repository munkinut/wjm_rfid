/*
 * Created on 07-Jun-2005
 *
 */
package uk.co.indigo.play.unsigned;

/**
 * @author milbuw
 *
 */
public class UnsignedByte implements UnsignedByteOperations {
	
	private byte wrappedByte;
	private static final byte L_ROTATION_MASK = (byte)0x80;
	private static final byte R_ROTATION_MASK = (byte)0x01;
	private static final byte R_ZERO = (byte)0x00;
	private static final byte R_ONE = (byte)0x01;
	private static final byte L_ZERO = (byte)0x00;
	private static final byte L_ONE = (byte)0x80;

	public UnsignedByte(byte wrappedByte) {
		this.wrappedByte = wrappedByte;
	}

	public UnsignedByte and(UnsignedByte uByte) {
		byte b = (byte)(wrappedByte & uByte.getWrappedByte());
		return new UnsignedByte(b);
	}

	public UnsignedByte lrotate(int places) {
		byte toRotate = wrappedByte;
		for (int i = 0; i < places; i++) {
			toRotate = lrotate(toRotate);
		}
		return new UnsignedByte(toRotate);
	}
	
	private byte lrotate(byte toRotate) {
		byte lastRotatedBit;
		byte rotated = toRotate;
		byte lastRotatedBitMask = (byte)(wrappedByte & L_ROTATION_MASK);
		switch (lastRotatedBitMask) {
			case ((byte)0x80) : 
				lastRotatedBit = R_ONE;
				break;
			case ((byte)0x00) :
				lastRotatedBit = R_ZERO;
				break;
			default :
				// this should never happen
				lastRotatedBit = R_ZERO;
		}
		rotated <<= 1;
		rotated |= lastRotatedBit;
		return rotated;
	}

	private byte rrotate(byte toRotate) {
		byte lastRotatedBit;
		byte rotated = toRotate;
		byte lastRotatedBitMask = (byte)(wrappedByte & R_ROTATION_MASK);
		switch (lastRotatedBitMask) {
			case ((byte)0x01) : 
				lastRotatedBit = L_ONE;
				break;
			case ((byte)0x00) :
				lastRotatedBit = L_ZERO;
				break;
			default :
				// this should never happen
				lastRotatedBit = L_ZERO;
		}
		rotated = (byte)((toRotate & 0xFF) >> 1);
		rotated |= lastRotatedBit;
		return rotated;
	}

	public UnsignedByte lshift(int places) {
		byte toShift = wrappedByte;
		byte shifted = (byte)(toShift << places);
		return new UnsignedByte(shifted);
	}

	public UnsignedByte complement() {
		byte b = (byte)(~wrappedByte);
		return new UnsignedByte(b);
	}

	public UnsignedByte or(UnsignedByte uByte) {
		byte b = (byte)(wrappedByte | uByte.getWrappedByte());
		return new UnsignedByte(b);
	}

	public UnsignedByte rrotate(int places) {
		byte toRotate = wrappedByte;
		for (int i = 0; i < places; i++) {
			toRotate = rrotate(toRotate);
		}
		return new UnsignedByte(toRotate);
	}

	public UnsignedByte rshift(int places) {
		byte toShift = wrappedByte;
		byte shifted = (byte)((toShift & 0xFF) >> places);
		return new UnsignedByte(shifted);
	}

	public UnsignedByte xor(UnsignedByte uByte) {
		byte b = (byte)(wrappedByte ^ uByte.getWrappedByte());
		return new UnsignedByte(b);
	}
	
	/**
	 * @return Returns the wrappedByte.
	 */
	public byte getWrappedByte() {
		return wrappedByte;
	}
	
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof UnsignedByte)) return false;
		UnsignedByte ub = (UnsignedByte)arg0;
		if (!(ub.getWrappedByte() == this.getWrappedByte())) return false;
		return true;
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
