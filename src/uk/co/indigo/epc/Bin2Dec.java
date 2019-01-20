package uk.co.indigo.epc;


import java.math.*;

public class Bin2Dec {
	
	public static BigInteger calc(String binary) {
		char[] binArray = binary.toCharArray();
		BigInteger decimal = BigInteger.valueOf(0);
		int i;
		BigInteger bit=BigInteger.ZERO;
		for(i = 0;i< binary.length();i++){
			if(binArray[i] == '0')bit = BigInteger.ZERO;
			else if(binArray[i] == '1')bit = BigInteger.ONE;
			else {
				return BigInteger.ZERO;
			}
			decimal = decimal.multiply(BigInteger.valueOf(2)).add(bit);
		}
		return decimal;   
	}
}
