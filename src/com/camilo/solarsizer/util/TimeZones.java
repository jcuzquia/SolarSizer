package com.camilo.solarsizer.util;

/**
 * This enum handles the time zone and the values associated with it
 * @author Jose Camilo Uzquiano
 *
 */
public enum TimeZones {

	Eastern("US/Eastern"), 
	Central("US/Central"), 
	Mountain("US/Mountain"), 
	Pacific("US/Pacific");

	private String timeZoneIndex;

	private TimeZones(String timeZoneIndex) {
		this.timeZoneIndex = timeZoneIndex;
	}
	
	/**
	 * This is a method that gets the TimeZone
	 * @param timeZoneIndex
	 * @return
	 */
	public static TimeZones getTimeZone(String timeZoneIndex){
		for(TimeZones t : TimeZones.values()){
			if(t.timeZoneIndex.equals(timeZoneIndex)) {
				return t;
			}
		}
		throw new IllegalArgumentException("TimeZone not found");
	}
	
	public String getTimeZoneIndex(){
		return timeZoneIndex;
	}

}
