package org.vu.aso.next.nxt;

public interface INxtSensor {
	public String getValue();
	public boolean toBeMonitored();
	public void on();
	public void off();
}
