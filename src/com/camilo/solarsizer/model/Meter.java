package com.camilo.solarsizer.model;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.camilo.solarsizer.util.Assistant;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * Model for a Meter reading
 * @author Camilo Uzquiano
 *
 */
public class Meter {

	private StringProperty location;
	private StringProperty meterNumber;
	private ObservableList<IntervalData> intervalDataList;
	private boolean activated;
	private Date startDate, endDate;
	private Calendar calendar;
	private Color color;
	
	public Meter(ObservableList<IntervalData> intervalDataList) {
		this.intervalDataList = intervalDataList;
		
		if(this.intervalDataList.get(0).getMeterNumber() == null){
			meterNumber = Assistant.parsePropertiesString("no number");
		} else {
			meterNumber = this.intervalDataList.get(0).getMeterNumber();
		}
		
		activated = true;
		startDate = this.intervalDataList.get(0).getDate();
		endDate = intervalDataList.get(this.intervalDataList.size()-1).getDate();
		calendar = new GregorianCalendar();
		calendar.setTime(startDate);
		color = Color.GREEN;
		
		
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
		
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public StringProperty getLocation() {
		return location;
	}
	public void setLocation(StringProperty location) {
		this.location = location;
	}
	public ObservableList<IntervalData> getIntervalDataList() {
		return intervalDataList;
	}
	public void setIntervalDataList(ObservableList<IntervalData> intervalDataList) {
		this.intervalDataList = intervalDataList;
	}
	public StringProperty getMeterNumber() {
		return meterNumber;
	}
	public void setMeterNumber(StringProperty meterNumber) {
		this.meterNumber = meterNumber;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	public String getColorForFX(){
		return Assistant.toRGBCodeAWT(color);
	}
}
