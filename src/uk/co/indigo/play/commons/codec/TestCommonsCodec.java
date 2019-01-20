package uk.co.indigo.play.commons.codec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class TestCommonsCodec {

	public static void main(String[] args) {
		
		String s = "Hello";
		byte[] chars = s.getBytes();
		char[] hexChars = Hex.encodeHex(chars);
		for (int i = 0; i < hexChars.length; i++) {
			System.out.print(hexChars[i]);
		}
		System.out.println();
		
		try {
			String new_s = new String(Hex.decodeHex(hexChars));
			System.out.println(new_s);
		}
		catch (DecoderException de) {
			System.err.println(de.getMessage());
		}
	}

}
