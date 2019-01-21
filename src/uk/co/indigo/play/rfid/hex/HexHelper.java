package uk.co.indigo.play.rfid.hex;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


public class HexHelper {

	private HexHelper() {}
	
	public static char[] toHex(String ascii) {
		return Hex.encodeHex(ascii.getBytes());
	}
	
	public static String toASCII(char[] hex) throws HexHelperException {
		try {
			return new String(Hex.decodeHex(hex));
		}
		catch (DecoderException de) {
			throw new HexHelperException(de);
		}
	}
	
}
