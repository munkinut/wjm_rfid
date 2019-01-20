package uk.co.indigo.epc;

// program to calculate the binary value from a decimal number

import java.math.*;

public class Dec2Bin {
	
	public static String calc(String decimalStr) {
		
		BigInteger decimal = BigInteger.ZERO;
		try {
			decimal = new BigInteger(decimalStr); 
		} catch (NumberFormatException nfe) {
			return "0";   
		}
		String binaryStr = "";
		int i=1;
		BigInteger powerOfTwo=BigInteger.ONE;
		while(powerOfTwo.compareTo(decimal)<=0){
			powerOfTwo = powerOfTwo.multiply(BigInteger.valueOf(2));
			i++;
		}
		i--;
		powerOfTwo=powerOfTwo.divide(BigInteger.valueOf(2));
		while (i> 0){
			if (powerOfTwo.compareTo(decimal)<=0){
				binaryStr = binaryStr+"1";
				decimal = decimal.subtract(powerOfTwo);
			}
			else 
				binaryStr = binaryStr+"0";
			powerOfTwo=powerOfTwo.divide(BigInteger.valueOf(2));
			i--;
		}
		return binaryStr;
	}
	
}



