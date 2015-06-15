package com.camilo.solarsizer.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

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
public class ReadTMY3DataTask extends Task<ObservableList<WeatherData>> {

	private URL url;
	public static final String READ_WEATHER_FILE_1_TITLE = "Reading TMY3 Data Task";

	public ReadTMY3DataTask(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This protected method is thrown automatically and this is where it reads
	 * the textfile and process the data
	 */
	@Override
	protected ObservableList<WeatherData> call() throws Exception {

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
			// //////////reading the first line//////////
			bufferedReader.readLine();
			//we check if the data has station name
			String itemsLine = bufferedReader.readLine();
			String itemsDelims = "[,]+";
			String[] itemsTokens = itemsLine.split(itemsDelims);
			
			String line = null;
			// while there is still line in the textfile keep looping
			while ((line = bufferedReader.readLine()) != null) {
				// ///////////set up the date//////////////
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			// throws a dialog to make sure that the file is still there
			Dialogs.create().title("No data Found").masthead("We couldn't find the data you want to read")
					.message("Please contact someone to correct the URL").showError();

		} catch (IOException e) {
			// throws a dialog to make sure that the file is still there
			Dialogs.create().title("Unable to read file").masthead("We weren't able to process the file")
					.message("please make sure that your internet connection is working").showError();
			// TODO: add more information on how the format of the text file
			// should look
		}

		return null;
	}

}
