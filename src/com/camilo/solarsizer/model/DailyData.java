package com.camilo.solarsizer.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
/**
 * This class gathers compiles all the data that is contained on a daily basis
 *
 * @author Jose Camilo Uzquiano
 */
public class DailyData {

	private double temperature;
	private ObservableList<IntervalData> dailyIntervalList;
	private Date date;
	private long dateValue;
	private int dayOfWeek;
	private DayType dayType;
	private ComboBox<DayType> comboBox;
	private float totalDailykWh;
	
	
	/**
	 * Constructor
	 * @param dailyIntervalList
	 * @param dateValue
	 */
	public DailyData(ObservableList<IntervalData> dailyIntervalList, Long dateValue) {
		this.dailyIntervalList = dailyIntervalList;
		this.dateValue = dateValue;
		date = new Date(dateValue);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		dayType = DayType.SCHOOL_DAY;
		
		if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
			dayType = DayType.WEEKEND;
		} 
		
		
		totalDailykWh = getTotalDailyConsumption(this.dailyIntervalList);
	}
	
	
	/**
	 * Simple method that adds up the total kWh for each day
	 * @param dailyIntervalList
	 * @return totalDailyConsumption
	 */
	private float getTotalDailyConsumption(ObservableList<IntervalData> dailyIntervalList) {
		float totalDailyConsumption = 0;
		for(IntervalData data : dailyIntervalList){
			totalDailyConsumption = totalDailyConsumption + data.getKWh() - data.getGenkWh();
		}
		return totalDailyConsumption;
	}

	public ObservableList<IntervalData> getDailyIntervalList() {
		return dailyIntervalList;
	}
	public void setDailyIntervalList(ObservableList<IntervalData> dailyIntervalList) {
		this.dailyIntervalList = dailyIntervalList;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public long getDateValue() {
		return dateValue;
	}
	public void setDateValue(long dateValue) {
		this.dateValue = dateValue;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public DayType getDayType() {
		return dayType;
	}
	public void setDayType(DayType dayType) {
		this.dayType = dayType;
	}
	public ComboBox<DayType> getComboBox() {
		return comboBox;
	}
	public void setComboBox(ComboBox<DayType> comboBox) {
		this.comboBox = comboBox;
	}
	public float getTotalDailykWh() {
		return totalDailykWh;
	}
}
