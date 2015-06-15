package com.camilo.solarsizer.events;

import java.util.EventObject;

import com.camilo.solarsizer.util.TimeZones;

/**
 * This class stores momentarily the selected value from the combo box of the
 * time zone dialog. This object is then passed to the
 * weatherProgressDialogController
 * 
 * @author Jose Camilo Uzquiano
 *
 */
public class TimeZoneSelectionEvent extends EventObject {

	private static final long serialVersionUID = -6877906472340755746L;
	private TimeZones timeZone;
	private String typeTemperatureImport;

	public TimeZoneSelectionEvent(Object source, TimeZones timeZone, String typeTemperatureImport) {
		super(source);
		this.timeZone = timeZone;
		this.typeTemperatureImport = typeTemperatureImport;

	}

	public TimeZones getTimeZone() {
		return timeZone;
	}


	public String getTypeTemperatureImport() {
		return typeTemperatureImport;
	}
}
