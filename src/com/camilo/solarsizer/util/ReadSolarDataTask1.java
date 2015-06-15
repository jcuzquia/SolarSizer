package com.camilo.solarsizer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import org.controlsfx.dialog.Dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;






import com.camilo.solarsizer.model.IntervalData;

/**
 * This class is the task that reads green button data as a default
 * @author Jose Camilo Uzquiano
 *
 */
@SuppressWarnings("deprecation")
public class ReadSolarDataTask1 extends Task<ObservableList<IntervalData>> {

	private File file;
	public static final String READ_FILE_1_TITLE = "Solar Data";

	public ReadSolarDataTask1() {
	}

	/**
	 * This protected method is thrown automatically and this is where it reads
	 * the textfile and process the data
	 */
	@Override
	protected ObservableList<IntervalData> call() throws Exception {

		ObservableList<IntervalData> intervalList = FXCollections.observableArrayList();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			// //////////reading the first line//////////
			String line = bufferedReader.readLine();
			String delims = "[	]+";
			String[] lineTokens = line.split(delims);
			int powerIndex = retrieveAveragePowerIndex(lineTokens);
			String dateDelims = "[/ :]+";


			// while there is still line in the textfile keep looping
			while ((line = bufferedReader.readLine()) != null) {
				// ///////////set up the date//////////////
				String[] dataTokens = line.split(delims);
				String[] dateTokens = dataTokens[0].split(dateDelims);
				System.out.println(Arrays.toString(dateTokens));
				short m = Short.parseShort(dateTokens[0]);
				short d = Short.parseShort(dateTokens[1]);
				short y = Short.parseShort(dateTokens[2]);
				
				short h1 = Short.parseShort(dateTokens[3]);
				short min1 = Short.parseShort(dateTokens[4]);

				float pow = Float.parseFloat(dataTokens[powerIndex]);
				
				
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				Date date = new Date(y - 1900, m - 1, d, h1, min1);
				cal.setTime(date);

				IntervalData interval = new IntervalData(cal.getTimeInMillis());

				if (pow > 0) {
					interval.setkW(pow);
					interval.setKWh(pow/4);
				} else {
					interval.setGenkW(pow*(-1));
					interval.setGenkWh(pow*(-1)/4);
				}

				interval.setDate(cal.getTime());
				interval.setDateValue(cal.getTime().getTime());


				intervalList.add(interval);
			}
		} catch (FileNotFoundException e) {
			// throws a dialog to make sure that the file is still there
			Dialogs.create().title("File not found").masthead("We couldn't find the file you want to read")
					.message("Please make sure that the file is still there").showError();

		} catch (IOException e) {
			// throws a dialog to make sure that the file is still there
			Dialogs.create().title("Unable to read file").masthead("We weren't able to process the file")
					.message("Please make sure that the file has the appropriate format").showError();
			//TODO: add more information on how the format of the text file should look
		}
		Collections.sort(intervalList);
		return intervalList;
	}

	private int retrieveAveragePowerIndex(String[] lineTokens) {
		
		for (int i = 0; i < lineTokens.length - 1; i++){
			if (lineTokens[i].equals("power_avg")){
				return i;
			}
		}
		
		return 0;
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

