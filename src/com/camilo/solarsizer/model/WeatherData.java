package com.camilo.solarsizer.model;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.jfree.data.time.Minute;

/**
 * This class stores the hourly weather data, at the moment it only has 
 * the outside air temperature stored. The data has to be from a specific format
 * given by NOAA
 * @author Jose Camilo Uzquiano
 *
 */
public class WeatherData implements Comparable<WeatherData> {

	private float temperature;
	
	private short month, day, year, hour, min;
	
	private long dateValue;
	
	private Date date;
	
	private Minute minute;
	
	private int timeZone;
	
	/**
	 * contructor of the class where you use a local date
	 * @param date
	 */
	@SuppressWarnings("deprecation")
	public WeatherData(Date date) {
		this.date = date;
		year = (short) (date.getYear()+1900);
		month = (short) (date.getMonth()+1);
		day = (short) (date.getDate());
		hour = (short) (date.getHours());
		min = (short) (date.getMinutes());
		dateValue = date.getTime();
		minute = new Minute(date);
	}

	public float getTemperature() {
		return temperature;
	}


	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}


	public short getMonth() {
		return month;
	}


	public void setMonth(short month) {
		this.month = month;
	}


	public short getDay() {
		return day;
	}


	public void setDay(short day) {
		this.day = day;
	}


	public short getYear() {
		return year;
	}


	public void setYear(short year) {
		this.year = year;
	}


	public long getDateValue() {
		return dateValue;
	}


	public void setDateValue(long dateValue) {
		this.dateValue = dateValue;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Minute getMinute() {
		return minute;
	}


	public void setMinute(Minute minute) {
		this.minute = minute;
	}


	@Override
	public int compareTo(WeatherData data) {
		long comparedTime = data.getDateValue();
		if (this.dateValue > comparedTime) {
			return 1;
		} else if (this.dateValue == comparedTime) {
			return 0;
		} else {
			return -1;
		}
	}

	public int getTimeZone() {
		return timeZone;
	}
	
	public short getHour() {
		return hour;
	}

	public void setHour(short hour) {
		this.hour = hour;
	}

	public short getMin() {
		return min;
	}

	public void setMin(short min) {
		this.min = min;
	}
	
	public void setDateVariables(long dateValue){
		
	}

	/**
	 * method that is used to change the time zone for the data
	 * @param timeZone
	 */
	@SuppressWarnings("deprecation")
	public void setTimeZone(String timeZone) {
		TimeZone fromTimeZone = TimeZone.getTimeZone("GMT");
		TimeZone toTimeZone = TimeZone.getTimeZone(timeZone);
		
		//this is where na calendar is created that will handle the TimeZone
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateValue); 
		cal.setTimeZone(fromTimeZone);
		cal.add(Calendar.MILLISECOND, toTimeZone.getRawOffset());
		
		if(fromTimeZone.inDaylightTime(cal.getTime())){
			cal.add(Calendar.MILLISECOND, toTimeZone.getDSTSavings());
		}
		
		//new dateValue after change in timeZone
		dateValue = cal.getTimeInMillis();
		date = cal.getTime();
		
		//new variables that are made after the change in TimeZone
		minute = new Minute(cal.getTime(), TimeZone.getTimeZone(timeZone));
		year = (short)(date.getYear()+1900);
		month = (short)(date.getMonth()+1);
		day = (short)date.getDate();
		hour = (short)date.getHours();
		min = (short) date.getMinutes();
		
		
	}
	
	/**
	 * This method returns a new format of the dates
	 * not sure if it is being used yet
	 * @return
	 */
	public int getIntegerValue(){
		int value = 0;
		String y = Integer.toString(minute.getDay().getYear()); // no problems with this
		int month = minute.getDay().getMonth();
		String m = null;
		if(month < 10){
			m = ""+0+month;
		} else {
			m = ""+month;
		}
		String d = null;
		int day = minute.getDay().getDayOfMonth();
		if(day < 10){
			d = ""+0+day;
		} else {
			d = ""+day;
		}
		String h = null;
		int hour = minute.getHourValue();
		if(hour < 10){
			h = ""+0+hour;
		}else{
			h=""+hour;
		}
		String stringValue = y+m+d+h;
		value = Integer.parseInt(stringValue);
		return value;
	}
}
