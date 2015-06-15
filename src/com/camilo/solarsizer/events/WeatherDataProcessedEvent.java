package com.camilo.solarsizer.events;

import java.util.EventObject;

import javafx.collections.ObservableList;

import com.camilo.solarsizer.model.WeatherData;

/**
 * This class stores temporarily the weather data list and 
 * pass it to the main controller it is called from the ReadWeatherDataTask class
 *  
 * @author Jose Camilo Uzquiano
 *
 */
public class WeatherDataProcessedEvent extends EventObject {
	
	private ObservableList<WeatherData> weatherDataList;
	private static final long serialVersionUID = 336256056563598949L;
	
	public WeatherDataProcessedEvent(Object source, ObservableList<WeatherData> weatherDataList) {
		super(source);
		this.weatherDataList = weatherDataList;
	}

	public ObservableList<WeatherData> getWeatherDataList() {
		return weatherDataList;
	}
}
