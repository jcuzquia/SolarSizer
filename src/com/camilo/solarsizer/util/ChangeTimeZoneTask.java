package com.camilo.solarsizer.util;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.camilo.solarsizer.model.WeatherData;

/**
 * This class handles the change in Timezone for the data
 * @author Jose Camilo Uzquiano
 *
 */
public class ChangeTimeZoneTask extends Task<ObservableList<WeatherData>> {

	private ObservableList<WeatherData> weatherDataList;
	private String timeZone;
	public static final String CHANGE_TIME_ZONE_TASK_TITLE = "Changing the time zone of the data";
	private String temperatureType;
	
	public ChangeTimeZoneTask(ObservableList<WeatherData> weatherDataList, String temperatureType) {
		this.weatherDataList = weatherDataList;
		this.temperatureType = temperatureType;
	}
	
	@Override
	protected ObservableList<WeatherData> call() throws Exception {
		
		
		for (WeatherData data : weatherDataList){
			data.setTimeZone(timeZone);
			
			if(temperatureType.equals("Farenheit")){
				setFarenheitConversion(data);
			}
			
		}
		
		return weatherDataList;
	}

	/**
	 * This method handles the conversion of the temperature to farenheit. 
	 * all NOAA files are set in celcius
	 * @param data
	 */
	private void setFarenheitConversion(WeatherData data) {
		float tempInitial = data.getTemperature();
		
		float toFarenheit = tempInitial*9/5 + 32;
		
		data.setTemperature(toFarenheit);
		
	}

	/**
	 * we set the time zone before starting the task. This is done by the
	 * WeatherProgressDialogCOntroller
	 * @param string
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	

}
