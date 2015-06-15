package com.camilo.solarsizer.model;

import java.util.Date;

import javafx.collections.ObservableList;

/**
 * this class handles and stores the daily data for the weather
 * data that has been processed
 * @author Jose Camilo Uzquiano
 *
 */
public class DailyWeatherData {

	private double temperature;
	private ObservableList<WeatherData> dailyWeatherList;
	private Date date; 
	private long dateValue;
	private int dayOfWeek;
	private DayType dayType;
	
	public DailyWeatherData(ObservableList<WeatherData> dailyWeatherList, long dateValue) {
		this.dailyWeatherList = dailyWeatherList;
		this.dateValue = dateValue;
		date = new Date(dateValue);
		
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public ObservableList<WeatherData> getDailyWeatherList() {
		return dailyWeatherList;
	}

	public void setDailyWeatherList(ObservableList<WeatherData> dailyWeatherList) {
		this.dailyWeatherList = dailyWeatherList;
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
	
	
	
}
