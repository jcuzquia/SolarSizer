package com.camilo.solarsizer.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.camilo.solarsizer.model.DailyWeatherData;
import com.camilo.solarsizer.model.Day;
import com.camilo.solarsizer.model.WeatherData;
/**
 * this class handles the construction of the matrix of the weatherData
 * @author Jose Camilo Uzquiano
 *
 */

public class BuildWeatherMatrixTask extends Task<ObservableList<DailyWeatherData>> {

	private ObservableList<WeatherData> weatherDataList;
	private ObservableList<DailyWeatherData> dailyWeatherDataList;
	
	public BuildWeatherMatrixTask(ObservableList<WeatherData> weatherDataList) {
		this.weatherDataList = weatherDataList;
	}	
	@Override
	/**
	 * this method handles and returns a processed weather data
	 * this is called from the WeatherProgressDialogController
	 */
	protected ObservableList<DailyWeatherData> call() throws Exception {
		
		dailyWeatherDataList = FXCollections.observableArrayList();
		ObservableList<WeatherData> deletionDataList = FXCollections.observableArrayList(weatherDataList);
		
		
		while (deletionDataList.size() > 0){
			
			ObservableList<WeatherData> dayTimeData = FXCollections.observableArrayList();//create the dailyContainer
			WeatherData data = deletionDataList.get(0);
			Day dayType = new Day(data.getDateValue());
			
			while (data.getDay() == deletionDataList // if the day match
					.get(0).getDay()){		//then add to the dayList
				
				dayTimeData.add(data);
				data = deletionDataList.get(0); //we get the first data
				deletionDataList.remove(0);	
				
				if(deletionDataList.size() == 0){
					break;
				}
			}
			if (dayTimeData.size() > 96) {
				dayTimeData = trimToSizeForArray(dayTimeData);
			}
			dailyWeatherDataList.add(new DailyWeatherData(dayTimeData, dayType.getDateValue()));
		}

		return dailyWeatherDataList;
	}
	
	private ObservableList<WeatherData> trimToSizeForArray(ObservableList<WeatherData> dayTimeData) {
		while (dayTimeData.size() > 96) {
			dayTimeData.remove(dayTimeData.size()-1);
		}

		return dayTimeData;
	}
	public ObservableList<WeatherData> getWeatherDataList() {
		return weatherDataList;
	}
	public void setWeatherDataList(ObservableList<WeatherData> weatherDataList) {
		this.weatherDataList = weatherDataList;
	}
	public ObservableList<DailyWeatherData> getDailyWeatherDataList() {
		return dailyWeatherDataList;
	}
	public void setDailyWeatherDataList(ObservableList<DailyWeatherData> dailyWeatherDataList) {
		this.dailyWeatherDataList = dailyWeatherDataList;
	}
	
	

}
