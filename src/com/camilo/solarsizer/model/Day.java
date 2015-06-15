package com.camilo.solarsizer.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.camilo.solarsizer.util.Assistant;

import javafx.beans.property.StringProperty;

/**
 * List of dates
 * @author Camilo Uzquiano
 *
 */
public class Day {
	
	private Date date;
	private long dateValue;
	private StringProperty dayType;
	private StringProperty dateStringProp;
	
	public Day(long dateValue){
		
		this.dateValue = dateValue;
		date = new Date(dateValue);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if(day == Calendar.SATURDAY || day == Calendar.SUNDAY){
			dayType = Assistant.parsePropertiesString("Weekend");
		} else {
			dayType = Assistant.parsePropertiesString("SchoolDay");
		}
		dateStringProp = Assistant.parsePropertiesString(date.toString());
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getDateValue() {
		return dateValue;
	}

	public void setDateValue(long dateValue) {
		this.dateValue = dateValue;
	}

	public StringProperty getDayType() {
		return dayType;
	}

	public void setDayType(StringProperty dayType) {
		this.dayType = dayType;
	}

	public StringProperty getDateStringProp() {
		return dateStringProp;
	}

	public void setDateStringProp(StringProperty dateStringProp) {
		this.dateStringProp = dateStringProp;
	}
	
	
}
