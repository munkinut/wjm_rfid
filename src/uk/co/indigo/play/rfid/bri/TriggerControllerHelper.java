package uk.co.indigo.play.rfid.bri;

public class TriggerControllerHelper {
	
	private TriggerController2 tc; 
	
	public TriggerControllerHelper() {
		try {
			tc = TriggerController2.getInstance();
			addTrigger("stop",9,9,100);
		}
		catch (BRIWrapperException bwe) {
			// TODO
		}
	}
	
	public void addTrigger(String name, int mask, int switches, long delay) {
		try {
			Trigger t = new Trigger(name, mask, switches, delay);
			tc.addTriggerListener(t, new TriggerListenerInterface() {
            	public void notify(TriggerEvent te) throws BRIWrapperException {
            		// TODO
            		System.out.println("BING! " + te.toString());
            		Trigger trig = te.getTrigger();
            		if (trig.getName().equals("stop")) tc.setListening(false);
            	}
			});
		}
		catch (BRIWrapperException bwe) {
			// TODO
		}
	}
	
	public void listen() {
		try {
			tc.listen();
		}
		catch (BRIWrapperException bwe) {
			// TODO
		}
	}

}
