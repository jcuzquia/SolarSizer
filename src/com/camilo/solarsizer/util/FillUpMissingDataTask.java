package com.camilo.solarsizer.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.camilo.solarsizer.model.IntervalData;

public class FillUpMissingDataTask extends Task<ObservableList<IntervalData>> {

	private ObservableList<IntervalData> intervalList;
	public static final String TITLE = "Filling up missing data with zero";
	
	public FillUpMissingDataTask(ObservableList<IntervalData> intervalList) {
	this.intervalList = intervalList;
	}

	@Override
	protected ObservableList<IntervalData> call() throws Exception {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		for (int i = 0; i < intervalList.size() - 1; i++) {
			IntervalData data1 = intervalList.get(i);
			IntervalData data2 = intervalList.get(i + 1);
			if (data2.getDateValue() - data1.getDateValue() > Constants.MINUTE * 15) { //if it is greater, fill with empty interval

				long dateVal = data1.getDateValue() + Constants.MINUTE * 15;

				cal.setTime(new Date(dateVal));
				IntervalData fillData = new IntervalData(cal.getTimeInMillis());//new interval with no energy
				fillData.setKWh(0);
				intervalList.add(i + 1, fillData);
				data1 = intervalList.get(i);

				data2 = intervalList.get(i + 1);

			} 
			//if the interval data is 5 minutes
			else if (data2.getDateValue() - data1.getDateValue() < Constants.MINUTE * 15){ //if they are
				intervalList.get(i).setKWh(data1.getKWh() + data2.getKWh());
				
				//TODO: set all the values still
				intervalList.remove(i+1);
				i--;
			}
			// System.out.println("COUNTER: " +i + " SIZE" +
			// intervalDataList.size());
		}

		return intervalList;
	}


}
