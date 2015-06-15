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

public class ReadFile3Task extends Task<ObservableList<IntervalData>> {

	private File file;
	public static final String READ_FILE_3_TITLE = "Reading Green Button File new format...";

	public ReadFile3Task() {
	}

	@Override
	protected ObservableList<IntervalData> call() throws Exception {

		ObservableList<IntervalData> intervalList = FXCollections.observableArrayList();
		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(
				file))) {
			
			// //////////reading the first line//////////
			bufferedReader.readLine();
			String delims = "	";
			String dateDelims = "[/]+";
			String timeDelims = "[:]+";

			String line = null;

			
			while ((line = bufferedReader.readLine()) != null) {
				
				// ///////////set up the date//////////////
				String[] dataTokens = line.split(delims);
				String[] dateTimeTokens = dataTokens[10].split(" ");
				
				String[] dateTokens = dateTimeTokens[0].split(dateDelims);
				
				short m = Short.parseShort(dateTokens[0]);
				short d = Short.parseShort(dateTokens[1]);
				short y = Short.parseShort(dateTokens[2]);

				// ///////////set up the time///////////////
				String[] timeTokens = dateTimeTokens[1].split(timeDelims);
				short h1 = Short.parseShort(timeTokens[0]);
				short min1 = Short.parseShort(timeTokens[1]);

				float con = Float.parseFloat(dataTokens[12]);
				Calendar cal = Calendar
						.getInstance(TimeZone.getTimeZone("GMT"));
				@SuppressWarnings("deprecation")
				Date date = new Date(y - 1900, m - 1, d, h1, min1);
				cal.setTime(date);
				
				IntervalData interval = new IntervalData(cal.getTimeInMillis());
				interval.setMeterNumber(Assistant.parsePropertiesString(dataTokens[1]));

				if (con<0){
					interval.setGenkWh(con*(-1));
					interval.setGenkW(con*(-1)*4);
				} else {
					interval.setKWh(con);
					interval.setkW(con*4);
				}
				
				interval.setDate(cal.getTime());
				interval.setDateValue(cal.getTime().getTime());

				intervalList.add(interval);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Can't find file" + file.toString());
			
		} catch (IOException e) {
			System.out.println("Unable to read file" + file.toString());
		}
		Collections.sort(intervalList);
		return intervalList;
	}


	/**
	 * Method called from the progress dialog controller
	 * at the initialize method
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	

}
