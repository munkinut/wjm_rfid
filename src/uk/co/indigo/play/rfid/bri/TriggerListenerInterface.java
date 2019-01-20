package uk.co.indigo.play.rfid.bri;

public interface TriggerListenerInterface {
	
	public void notify(TriggerEvent triggerEvent) throws BRIWrapperException;

}
