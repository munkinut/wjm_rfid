package uk.co.indigo.play.rfid.bri;

public class Trigger {
	
	private volatile int hashCode = 0;
	
	private String name;
	private int mask;
	private int switches;
	private long delay;

	public Trigger(String name, int mask, int switches, long delay) throws BRIWrapperException {
		super();
		validate(name, mask, switches, delay);
		this.name = name;
		this.mask = mask;
		this.switches = switches;
		this.delay = delay;
	}
	
	private void validate(String name, int mask, int switches, long delay) throws BRIWrapperException {
        if (name == null || name.equals(""))
            throw new BRIWrapperException("Trigger name cannot be null or empty.");
        if (delay < 0)
            throw new BRIWrapperException("The filter delay must be >= 0.");
        if ((switches < 0) || (switches > 15))
            throw new BRIWrapperException("Switch value must be between 0 and 15.");
        if ((mask < 0) || (mask > 15))
            throw new BRIWrapperException("Mask value must be between 0 and 15.");
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSwitches() {
		return switches;
	}

	public void setSwitches(int switches) {
		this.switches = switches;
	}
	
	public boolean equals(Object o) {
		return o instanceof Trigger &&
			((Trigger)o).getName().equals(name);
	}
	
	public int hashCode() {
		if (hashCode == 0) {
			int result = 17;
			result = 37 * result + switches;
			hashCode = result;
		}
		return hashCode;
	}

}
