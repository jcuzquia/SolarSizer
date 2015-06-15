package com.camilo.solarsizer.util;

import java.util.Date;

import com.camilo.solarsizer.model.WeatherData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * This class handles the matching of the weather data so it is 15 minutes
 * interval it will allocate closest time to each quarter hour that is closer to
 * it. minutes should be
 * 
 * 
 * @author Jose Camilo Uzquiano
 *
 */
public class MatchTimeWeatherDataTask extends Task<ObservableList<WeatherData>> {

	private ObservableList<WeatherData> weatherDataList;
	public static final String TITLE = "Matching Time Interval";

	public MatchTimeWeatherDataTask(ObservableList<WeatherData> weatherDataList) {
		this.weatherDataList = weatherDataList;
	}

	/**
	 * This method is called when the task is executed, it handles the main task
	 * of this program
	 */
	protected ObservableList<WeatherData> call() throws Exception {

		// first initialize the two containers
		ObservableList<WeatherData> deleteList = FXCollections.observableArrayList(weatherDataList);
		ObservableList<WeatherData> toAddList = FXCollections.observableArrayList();
		ObservableList<WeatherData> intervalWeatherList = FXCollections.observableArrayList();
		// initialize the first data
		WeatherData weatherDataInitTimedData = getRightTimeInterval(deleteList.get(0));
		if (weatherDataInitTimedData.getDateValue() < deleteList.get(0).getDateValue()) {
			intervalWeatherList.add(weatherDataInitTimedData);
		} else {
			toAddList.add(deleteList.get(0));
		}

		deleteList.remove(0);
		while (!deleteList.isEmpty()) {
			WeatherData observationData = deleteList.get(0);
			// System.out.println("=======================================");
			// System.out.println("This is the observation date: " +
			// observationData.getDate().toString());
			WeatherData timedData = getRightTimeInterval(observationData);
			// System.out.println("This is the timed data added:" +
			// timedData.getDate().toString());
			// System.out.println("This is the Size" + deleteList.size() +
			// "this is count: " + count);
			intervalWeatherList.add(timedData);

			deleteList.remove(0);
		}

		// this is where the data is processed
		intervalWeatherList = deleteRepeatedDatesWeatherDataList(intervalWeatherList);
		intervalWeatherList = fillGapsInWeatherList(intervalWeatherList);
//		intervalWeatherList = correctEmptyBigGaps(intervalWeatherList);
		intervalWeatherList = trimToNearestDayList(intervalWeatherList);

		return intervalWeatherList;
	}

	/**
	 * This method correct the empty gaps by filling the temperature of a specific day with the temperature
	 * of the same time from the following day
	 * @param intervalWeatherList
	 * @return intervalWeatherList
	 */
	@SuppressWarnings("unused")
	private ObservableList<WeatherData> correctEmptyBigGaps(ObservableList<WeatherData> intervalWeatherList) {
		int count = 0;
		for (WeatherData data : intervalWeatherList){
			float temp = data.getTemperature();
			if(temp == -100){
				if (count < 96){
					data.setTemperature(intervalWeatherList.get(count+96).getTemperature());
				} else {
					data.setTemperature(intervalWeatherList.get(count-96).getTemperature());
				}
			}
			
			count++;
		}
		
		return intervalWeatherList;
	}

	/**
	 * This method simply trims to the neares day, which will be then used to
	 * create the weather matrix
	 * 
	 * @param intervalWeatherList
	 * @return weatherDataList
	 */
	private ObservableList<WeatherData> trimToNearestDayList(ObservableList<WeatherData> intervalWeatherList) {
		int hour = intervalWeatherList.get(0).getHour();
		int min = intervalWeatherList.get(0).getMin();

		while (hour != 0 || min != 0) { // while the hour or minute are not 0
			intervalWeatherList.remove(0);

			hour = intervalWeatherList.get(0).getHour();
			min = intervalWeatherList.get(0).getMin();

		}

		int hour1 = intervalWeatherList.get(intervalWeatherList.size() - 1).getHour();
		while (hour1 != 23) { // while the hour and minute is not 23:45
			// System.out.println("HOUR: " + hour1 + ">>MIN: " + minute1);
			intervalWeatherList.remove(intervalWeatherList.size() - 1);
			hour1 = intervalWeatherList.get(intervalWeatherList.size() - 1).getHour();

		}

		return intervalWeatherList;
	}

	/**
	 * This method fills up the gaps in the weather file with the closest time
	 * data
	 * 
	 * @param intervalWeatherList
	 * @return
	 */
	private ObservableList<WeatherData> fillGapsInWeatherList(ObservableList<WeatherData> intervalWeatherList) {
		for (int i = 0; i < intervalWeatherList.size() - 2; i++) {
			WeatherData data1 = intervalWeatherList.get(i);
			WeatherData data2 = intervalWeatherList.get(i + 1);
			// System.out.println("This is the data: " +
			// data1.getDate().toString() + "... Temperature = " +
			// data1.getTemperature());
			long residual = (data2.getDateValue() - data1.getDateValue())/Constants.MINUTE;
			if (residual > 15) {
				Date date = new Date(data1.getDateValue() + 15 * Constants.MINUTE);
				WeatherData newWeatherData = new WeatherData(date);
				
				double regressedTemp = getNewLinearTemperature(data1.getDateValue(), data1.getTemperature(),
						data2.getDateValue(), data2.getTemperature(),
						newWeatherData.getDateValue());

				newWeatherData.setTemperature((float) regressedTemp);
				// insert this new data with regressed temperature
				intervalWeatherList.add(i + 1, newWeatherData);
			} 
//			else if(residual == 15) {
//				//not do anything
//			} else if(residual >= 220){
//				Date date = new Date(data1.getDateValue() + 15 * Constants.MINUTE);
//				WeatherData newWeatherData = new WeatherData(date);
//				newWeatherData.setTemperature(-100);
//				intervalWeatherList.add(i + 1, newWeatherData);
//			}

		}
		return intervalWeatherList;
	}

	/**
	 * This method returns a new temperature based on the data available
	 * 
	 * @param dateValue
	 * @param temperature
	 * @param dateValue2
	 * @param temperature2
	 * @return
	 */
	private double getNewLinearTemperature(long dateValue, float temperature, long dateValue2, float temperature2, long emptyDataDateValue) {
		
		long dTime = dateValue2-dateValue;
		
		float dTemp = temperature2-temperature;
		double slope = dTemp/dTime;
		
		//y = mx x b
		double newTemp = slope * (emptyDataDateValue-dateValue) + temperature;
		
		return newTemp;
	}


	/**
	 * This method handles the repetition of the dates of the data
	 * 
	 * @param intervalWeatherList
	 */
	private ObservableList<WeatherData> deleteRepeatedDatesWeatherDataList(
			ObservableList<WeatherData> intervalWeatherList) {
		for (int i = 0; i < intervalWeatherList.size() - 2; i++) {
			WeatherData data1 = intervalWeatherList.get(i);
			WeatherData data2 = intervalWeatherList.get(i + 1);
			if (data1.getDateValue() == data2.getDateValue()) {
				float avgtemp = takeAverageTemperature(intervalWeatherList, i);
				
				intervalWeatherList = setAverageTemperature(i, intervalWeatherList);
				intervalWeatherList.get(i).setTemperature(avgtemp);
				intervalWeatherList.remove(i + 1);
			}
		}
		return intervalWeatherList;
	}

	
	/**
	 * This method set the average temperature between the beginning and the end in case 
	 * the data has been put in the same time stamp
	 * @param i
	 * @param intervalWeatherList
	 * @return
	 */
	private ObservableList<WeatherData> setAverageTemperature(int i, ObservableList<WeatherData> intervalWeatherList) {
		
		int count = 0;
		WeatherData data1 = intervalWeatherList.get(i);
		float temp = data1.getTemperature();
		int counter = 1;
		for(count = i+1; count < intervalWeatherList.size()-2; count++){
			if(data1.getDateValue() == intervalWeatherList.get(count).getDateValue()){
				temp = temp + intervalWeatherList.get(count).getTemperature();
				counter++;
				intervalWeatherList.remove(count);
			} else {
				break;
			}
		}
		
		intervalWeatherList.get(i).setTemperature(temp/counter);
		
		return intervalWeatherList;
	}

	/**
	 * This method returns the average temperature if multiple time stamps are equal.
	 * @param intervalWeatherList
	 * @param i
	 * @return average temperature
	 */
	private float takeAverageTemperature(ObservableList<WeatherData> intervalWeatherList, int i) {

		
		int count = 0;
		WeatherData data1 = intervalWeatherList.get(i);
		float temp = data1.getTemperature();
		int counter = 1;
		for(count = i+1; count < intervalWeatherList.size()-2; count++){
			if(data1.getDateValue() == intervalWeatherList.get(count).getDateValue()){
				temp = temp + intervalWeatherList.get(count).getTemperature();
				counter++;
			} else {
				break;
			}
			
			
		}
		
		return temp/counter;
	}

	/**
	 * This method gets the closest time interval, it is subdivided in several
	 * methods if the data follows the pre-requisites of 0, 15, 30 and 45
	 * minutes then simply return data
	 * 
	 * @param data
	 * @return new weather data
	 */
	private WeatherData getRightTimeInterval(WeatherData data) {
		short min = data.getMin();
		if (min == 0 || min == 15 || min == 30 || min == 45) {
			return data;
		} else {
			WeatherData rightTimeStampData = createRightTimeStampData(data);
			return rightTimeStampData;
		}

	}

	/**
	 * Method that creates the right time stamp for a data that is not
	 * 
	 * @param data
	 * @return
	 */
	private WeatherData createRightTimeStampData(WeatherData data) {

		short min = data.getMin();
		short prevMin = 0;
		short posMin = 0;

		if (min > 0 && min < 15) {
			prevMin = 0;
			posMin = 15;
		} else if (min > 15 && min < 30) {
			prevMin = 15;
			posMin = 30;
		} else if (min > 30 && min < 45) {
			prevMin = 30;
			posMin = 45;
		} else if (min > 45 && min < 60) {
			prevMin = 45;
			posMin = 60;
		}

		Date newDate = getClosestMinute(prevMin, posMin, data.getDate());
		WeatherData newData = new WeatherData(newDate);
		newData.setDateValue(newDate.getTime());
		newData.setTemperature(data.getTemperature());

		return newData;
	}

	/**
	 * method that retrieves the closest minute, this method outputs either 0,
	 * 15, 30, or 45 dates
	 * 
	 * @param min
	 * @return timedDate
	 */
	private Date getClosestMinute(short min1, short min2, Date dataDate) {
		@SuppressWarnings("deprecation")
		int min = dataDate.getMinutes();
		long actualTime = dataDate.getTime();
		int res1 = min - min1;

		int res2 = min2 - min;

		if (res1 < res2) {
			Date timedDate = new Date(actualTime - res1 * Constants.MINUTE);
			return timedDate;
		} else {
			Date timedDate = new Date(actualTime + res2 * Constants.MINUTE);
			return timedDate;
		}
	}
}
