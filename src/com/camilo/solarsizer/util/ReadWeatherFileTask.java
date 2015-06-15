package com.camilo.solarsizer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import org.controlsfx.dialog.Dialogs;

import com.camilo.solarsizer.model.WeatherData;

/**
 * This class is the task that reads green button data as a default
 * 
 * @author Jose Camilo Uzquiano
 *
 */
@SuppressWarnings("deprecation")
public class ReadWeatherFileTask extends Task<ObservableList<WeatherData>> {

	private File file;
	public static final String READ_WEATHER_FILE_1_TITLE = "Reading NOAA Text File";
	private boolean withStationName = false;

	public ReadWeatherFileTask() {
	}

	/**
	 * This protected method is thrown automatically and this is where it reads
	 * the textfile and process the data
	 */
	@Override
	protected ObservableList<WeatherData> call() throws Exception {

		ObservableList<WeatherData> weatherList = FXCollections.observableArrayList();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			// //////////reading the first line//////////
			bufferedReader.readLine();
			//we check if the data has station name
			String itemsLine = bufferedReader.readLine();
			String itemsDelims = "[ ]+";
			String[] itemsTokens = itemsLine.split(itemsDelims);
			if(itemsTokens[0].equals("Name")){
				withStationName = true;
			}
			
			
			String line = null;
			// while there is still line in the textfile keep looping
			while ((line = bufferedReader.readLine()) != null) {
				//first check if the line is empty else ignore it
				if (line.trim().length() > 0){
					WeatherData data = createWeatherData(line);

					weatherList.add(data);
				}
				
			}
		} catch (FileNotFoundException e) {
			// throws a dialog to make sure that the file is still there
			Dialogs.create().title("File not found").masthead("We couldn't find the file you want to read")
					.message("Please make sure that the file is still there").showError();

		} catch (IOException e) {
			// throws a dialog to make sure that the file is still there
			Dialogs.create().title("Unable to read file").masthead("We weren't able to process the file")
					.message("Please make sure that the file has the appropriate format").showError();
			// TODO: add more information on how the format of the text file
			// should look
		}
		Collections.sort(weatherList);

		return weatherList;
	}

	/**
	 * Method called to create the weather data to be added to the list
	 * 
	 * @param line
	 */
	private WeatherData createWeatherData(String line) throws IOException {
		String delims = "[,]+";

		String[] dataTokens = line.split(delims);
		WeatherData data = null;
		int startIndex = 2;
		if(withStationName){
			startIndex++;
		}
		
		try {
			short y = Short.parseShort(dataTokens[startIndex].substring(0, 4));
			short m = Short.parseShort(dataTokens[startIndex].substring(4, 6));
			short d = Short.parseShort(dataTokens[startIndex].substring(6, 8));

			short h = Short.parseShort(dataTokens[startIndex+1].substring(0, 2));
			short min = Short.parseShort(dataTokens[startIndex+1].substring(2, 4));

			float temp = Float.parseFloat(dataTokens[startIndex+5]);

			Date date = new Date(y - 1900, m - 1, d, h, min);

			data = new WeatherData(date);
			data.setTemperature(temp);
			data.setDateValue(date.getTime());
			data.setDate(date);
		} catch (Exception e) {
			// throws a dialog to make sure that the file is still there
			Dialogs.create().title("Unable To Read File").masthead("The file couldn't be read. Make sure that the file has the right format")
					.message("Please make sure that the file is still there").showError();
			return null;
		} 

		return data;
	}

	/**
	 * Method called from the progress dialog controller at the initialize
	 * method
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
