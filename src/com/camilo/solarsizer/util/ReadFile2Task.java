package com.camilo.solarsizer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.camilo.solarsizer.model.IntervalData;

public class ReadFile2Task extends Task<ObservableList<IntervalData>> {

	private File file;
	private ObservableList<IntervalData> intervalList;
	public static final String READ_FILE_2_TITLE = "Reading ARCx File...";

	public ReadFile2Task() {
		intervalList = FXCollections.observableArrayList();
	}

	@Override
	protected ObservableList<IntervalData> call() throws Exception {

		ObservableList<IntervalData> intervalList = FXCollections.observableArrayList();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			// //////////reading the first line//////////
			bufferedReader.readLine();

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				// ///////////set up the date//////////////
				IntervalData interval = createIntervalData(line);

				intervalList.add(interval);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(intervalList);

		return intervalList;
	}

	/**
	 * This method is called when we finished reading a line, after the line is 
	 * read, it passes to create the interval object
	 * @param line
	 * @return
	 */
	private IntervalData createIntervalData(String line) {
		String delims = "[	]+";
		String[] dataTokens = line.split(delims);
		String dateTokens = dataTokens[4];
		String year = dateTokens.substring(0, 4);
		String month = dateTokens.substring(4, 6);
		short y = Short.parseShort(year);
		short m = 0;
		if (Character.getNumericValue(month.charAt(0)) == 0) {
			m = (short) Character.getNumericValue(month.charAt(1));
		} else {
			m = Short.parseShort(month);
		}

		String day = dateTokens.substring(6, 8);
		short d = 0;
		if (Character.getNumericValue(day.charAt(0)) == 0) {
			d = (short) Character.getNumericValue(day.charAt(1));
		} else {
			d = Short.parseShort(day);
		}

		String hour = dateTokens.substring(8, 10);
		int h = 0;
		if (Character.getNumericValue(hour.charAt(0)) == 0) {
			h = Character.getNumericValue(hour.charAt(1));
		} else {
			h = Integer.parseInt(hour);
		}

		String minute = dateTokens.substring(10, 12);
		short min = Short.parseShort(minute);

		float con = Float.parseFloat(dataTokens[2]);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		@SuppressWarnings("deprecation")
		Date date = new Date(y - 1900, m - 1, d, h, min);
		cal.setTime(date);

		IntervalData interval = new IntervalData(cal.getTimeInMillis());
		
		switch(dataTokens[5]){
		case ("KWH"):
			if (dataTokens.length == 9) {
				interval.setGenkWh(con);
				interval.setGenkW(con*4);
			} else {
				interval.setKWh(con);
				interval.setkW(con*4);
			}
			
		break;
		case("KW"):
			if (dataTokens.length == 9) {
				interval.setGenkWh(con);
				interval.setGenkW(con/4);
			} else {
				interval.setKWh(con);
				interval.setkW(con/4);
			}
		break;
		}

		
		interval.setDate(cal.getTime());
		interval.setDateValue(cal.getTime().getTime());
		interval.setMeterNumber(Assistant.parsePropertiesString(dataTokens[0]));

		return interval;
	
	}

	/**
	 * Method called from the progress dialog controller
	 * at the initialize method
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	public ObservableList<IntervalData> getIntervalList() {
		return intervalList;
	}

	public void setIntervalList(ObservableList<IntervalData> intervalList) {
		this.intervalList = intervalList;
	}
	
	

}
