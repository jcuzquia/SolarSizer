package com.camilo.solarsizer.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javafx.beans.property.StringProperty;

import org.jfree.data.time.Minute;

import com.camilo.solarsizer.util.Constants;

/**
 * Model for the interval data which is about 15 minutes reading from a a meter
 * 
 * @author Jose Camilo Uzquiano
 *
 */

public class IntervalData implements Comparable<IntervalData> {

	private float kWh, cost, kW, genkW, genkWh, kVarh, kVar;
	private short julianDay;
	private boolean isStartDay, isEndDay;
	private Date date;
	private long dateValue;
	private StringProperty meterNumber;
	private String daytype;

	/**
	 * This will be the value used to create the interval data
	 * @param dateValue
	 */
	public IntervalData(long dateValue){
		this.dateValue = dateValue;
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(dateValue);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			daytype = "Weekend";
		} else {
			daytype = "WorkDay";
		}
		
		date = new Date(dateValue);
		
		//TODO: set the julian day
		
	}

	private void setJulianDay(int month, int day) {
		int days = 0;
		for (int m = 0; m < month - 1; m++) {
			days = days + Constants.MONTH_LENGTHS[m];
		}
		this.julianDay = (short) (days + day);

	}

	public float getKWh() {
		return kWh;
	}

	public void setKWh(float kWh) {
		this.kWh = kWh;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getkW() {
		return kW;
	}

	public short getJulianDay() {
		return julianDay;
	}

	public boolean isStartDay() {
		return isStartDay;
	}

	public void setStartDay(boolean isStartDay) {
		this.isStartDay = isStartDay;
	}

	public boolean isEndDay() {
		return isEndDay;
	}

	public void setEndDay(boolean isEndDay) {
		this.isEndDay = isEndDay;
	}

	

	

	@Override
	public String toString() {
		return "IntervalData [kW=" + kW + ", dateValue=" + dateValue + "]";
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

	public void setJulianDay(short julianDay) {
		this.julianDay = julianDay;
	}

	public float getkVarh() {
		return kVarh;
	}

	public void setkVarh(float kVarh) {
		this.kVarh = kVarh;
	}

	public float getkVar() {
		return kVar;
	}

	public void setkVar(float kVar) {
		this.kVar = kVar;
	}

	public void setkW(float kW) {
		this.kW = kW;
	}

	@Override
	public int compareTo(IntervalData data) {
		long comparedTime = data.getDateValue();
		if (this.dateValue > comparedTime) {
			return 1;
		} else if (this.dateValue == comparedTime) {
			return 0;
		} else {
			return -1;
		}
	}

	public float getGenkW() {
		return genkW;
	}

	public void setGenkW(float genkW) {
		this.genkW = genkW;
	}

	public float getGenkWh() {
		return genkWh;
	}

	public void setGenkWh(float genkWh) {
		this.genkWh = genkWh;
	}

	public StringProperty getMeterNumber() {
		return meterNumber;
	}

	public void setMeterNumber(StringProperty meterNumber) {
		this.meterNumber = meterNumber;
	}

	public String getDaytype() {
		return daytype;
	}

	public void setDaytype(String daytype) {
		this.daytype = daytype;
	}
}
