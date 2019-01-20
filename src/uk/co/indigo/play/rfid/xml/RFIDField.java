package uk.co.indigo.play.rfid.xml;

public class RFIDField {
	
	public static final int FORMAT_STR = 1;
	public static final int FORMAT_HEX = 2;
	
	private String name;
	private int format;
	private String value;

	public RFIDField(String name, int format, String value) throws RFIDFieldException{
		super();
		this.name = name;
		this.value = value;
		this.format = format;
		validateFormat();
	}
	
	private void validateFormat() throws RFIDFieldException {
		if (format < FORMAT_STR || format > FORMAT_HEX) 
			throw new RFIDFieldException("Format must be valid.");
		if (format == FORMAT_HEX && !isHex(value))
			throw new RFIDFieldException("Hex value must contain valid hex chars.");
	}
	
	private boolean isHex(String value) {
		return (value.matches("[A-Fa-f0-9]+"));
	}

	public int getFormat() {
		return format;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
