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
public class ReadFile1Task extends Task<ObservableList<IntervalData>> {

	private File file;
	public static final String READ_FILE_1_TITLE = "Reading Green Button File...";

	public ReadFile1Task() {
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
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			String delims = "[	]+";
			String dateDelims = "[/]+";
			String timeDelims = "[:]+";
			String costDelims = "[$]";

			String line = null;

			// while there is still line in the textfile keep looping
			while ((line = bufferedReader.readLine()) != null) {

				// ///////////set up the date//////////////
				String[] dataTokens = line.split(delims);
				String[] dateTokens = dataTokens[1].split(dateDelims);
				short m = Short.parseShort(dateTokens[0]);
				short d = Short.parseShort(dateTokens[1]);
				short y = Short.parseShort(dateTokens[2]);

				// ///////////set up the time///////////////
				String[] timeTokens1 = dataTokens[2].split(timeDelims);
				short h1 = Short.parseShort(timeTokens1[0]);
				short min1 = Short.parseShort(timeTokens1[1]);

				float con = Float.parseFloat(dataTokens[4]);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				Date date = new Date(y - 1900, m - 1, d, h1, min1);
				cal.setTime(date);

				IntervalData interval = new IntervalData(cal.getTimeInMillis());

				if (con < 0) {
					interval.setGenkWh(con * (-1));
					interval.setGenkW(con * (-1) * 4);
				} else {
					interval.setKWh(con);
					interval.setkW(con * 4);
				}

				interval.setDate(cal.getTime());
				interval.setDateValue(cal.getTime().getTime());

				if (dataTokens.length > 8) {
					try {
						String[] costTokens = dataTokens[6].split(costDelims);
						float cos = Float.parseFloat(costTokens[1]);
						interval.setCost(cos);

						if (dataTokens.length == 10) {
							float pow = Float.parseFloat(dataTokens[9]);
							interval.setkW(pow);
						} else if (dataTokens.length == 9) {
							float pow = Float.parseFloat(dataTokens[8]);
							interval.setkW(pow);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

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
