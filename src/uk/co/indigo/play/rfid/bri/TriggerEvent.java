package uk.co.indigo.play.rfid.bri;

public class TriggerEvent {
	
	private Trigger trigger;

	public TriggerEvent(Trigger trigger) {
		super();
		this.trigger = trigger;
	}

	public Trigger getTrigger() {
		return trigger;
	}
	
	public String toString() {
		return trigger.getName() + " " + trigger.getMask() + " " + trigger.getSwitches() +
			" " + trigger.getDelay();
	}

}
