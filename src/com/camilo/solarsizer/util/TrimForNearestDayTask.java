package com.camilo.solarsizer.util;

import java.util.Date;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.camilo.solarsizer.model.IntervalData;

public class TrimForNearestDayTask extends Task<ObservableList<IntervalData>> {

	private ObservableList<IntervalData> intervalList;
	public static final String TITLE = "Trimming to nearest day";

	public TrimForNearestDayTask(ObservableList<IntervalData> intervalList) {
		this.intervalList = intervalList;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected ObservableList<IntervalData> call() throws Exception {
		int hour = new Date(intervalList.get(0).getDateValue()).getHours();
		int min = new Date(intervalList.get(0).getDateValue()).getMinutes();

		
		while (hour != 0 || min != 0) { // while the hour or minute are not 0
			intervalList.remove(0);

			hour = new Date(intervalList.get(0).getDateValue()).getHours();
			min = new Date(intervalList.get(0).getDateValue()).getMinutes();
			
		}

		int hour1 = new Date(intervalList.get(intervalList.size() - 1).getDateValue()).getHours();
		while (hour1 != 23) { // while the hour and minute is not 23:45
			intervalList.remove(intervalList.size() - 1);
			hour1 = new Date(intervalList.get(intervalList.size() - 1).getDateValue()).getHours();

		}
		return intervalList;
	}

}
