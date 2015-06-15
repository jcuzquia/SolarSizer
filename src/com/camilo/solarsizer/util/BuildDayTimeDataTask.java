package com.camilo.solarsizer.util;

import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.Day;
import com.camilo.solarsizer.model.IntervalData;

public class BuildDayTimeDataTask extends Task<ObservableList<DailyData>> {

	private ObservableList<IntervalData> intervalList;
	private ObservableList<DailyData> dayTimeIntervalData;
	private ObservableList<Day> holidaySchedule;

	public BuildDayTimeDataTask(ObservableList<IntervalData> intervalList) {
		this.intervalList = intervalList;
		holidaySchedule = FXCollections.observableArrayList();
	}

	/**
	 * Method that creates the heat map as an matrix, and the holiday schedule
	 * from beginning to end of the meter
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected ObservableList<DailyData> call() throws Exception {

		dayTimeIntervalData = FXCollections.observableArrayList();
		ObservableList<IntervalData> deletionDataList = FXCollections.observableArrayList(intervalList);

		IntervalData firstData = deletionDataList.get(0);
		deletionDataList.remove(0);
		IntervalData secondData = deletionDataList.get(0);
		
		/*
		 * Loop until the deletion list has no items in it
		 * This means that the overall transfer is complete
		 */
		while (deletionDataList.size() > 0) {

			//create the daily container
			ObservableList<IntervalData> dayTimeData = FXCollections.observableArrayList();
			Day dayType = new Day(firstData.getDateValue()); 
			
			//while they are the same day keep adding to this specific day container
			while (firstData.getDate().getDate() == secondData.getDate().getDate()) { 

				firstData.setkW(firstData.getKWh() * 4);
				firstData.setGenkW(firstData.getGenkWh() * 4);

				dayTimeData.add(firstData);
				firstData = deletionDataList.get(0); // we get the first data
				deletionDataList.remove(0);
				secondData = deletionDataList.get(0);
				if(deletionDataList.size() == 1){
					break;
				}
			}
			
			dayTimeData.add(firstData);
			firstData = deletionDataList.get(0);
			deletionDataList.remove(0);
			
			if(deletionDataList.size() == 0 ){
				break;
			}
			secondData = deletionDataList.get(0);
			if (dayTimeData.size() != 96) {
				dayTimeData = fixDayTimeData(dayTimeData);
			}
			dayTimeIntervalData.add(new DailyData(dayTimeData, dayType.getDateValue()));
		}
		
		return dayTimeIntervalData;
	}

	@SuppressWarnings("deprecation")
	/**
	 * This method returns 96 items per list to build the matrix of the heat map. 
	 * It checks only for repeated hours and minutes for each specific day, if the daily list
	 * is not equal to 96 items. This is mostly done for daylight savings
	 * @param dayTimeData
	 * @return
	 */
	private ObservableList<IntervalData> fixDayTimeData(ObservableList<IntervalData> dayTimeData) {
		
		java.util.Collections.sort(dayTimeData);
		
		for (int i = 0; i < dayTimeData.size()-2; i++){
			Date date1 = new Date(dayTimeData.get(i).getDateValue());
			
			int h1 = date1.getHours();
			int m1 = date1.getMinutes();
			for(int j = i+1; j < dayTimeData.size()-1; j++){
				Date date2 = new Date(dayTimeData.get(j).getDateValue());
				int h2 = date2.getHours();
				int m2 = date2.getMinutes();
				
				if(h1 == h2 && m1 == m2){ //check if the minute and hour are equal for each item
					dayTimeData.get(i).setKWh(dayTimeData.get(j).getKWh());
					dayTimeData.get(i).setkW(dayTimeData.get(j).getkW());
					dayTimeData.get(i).setGenkWh(dayTimeData.get(j).getGenkWh());
					dayTimeData.get(i).setGenkW(dayTimeData.get(j).getGenkW());
					dayTimeData.remove(j);
				}
				
			}
		}
		
		return dayTimeData;
	}

	public ObservableList<DailyData> getDayTimeIntervalData() {
		return dayTimeIntervalData;
	}

	public ObservableList<Day> getHolidaySchedule() {
		return holidaySchedule;
	}

	public void setHolidaySchedule(ObservableList<Day> holidaySchedule) {
		this.holidaySchedule = holidaySchedule;
	}

}
