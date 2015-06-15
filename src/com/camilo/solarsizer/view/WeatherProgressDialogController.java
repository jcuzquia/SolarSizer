package com.camilo.solarsizer.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.camilo.solarsizer.SolarSizerAppMain;
import com.camilo.solarsizer.events.TimeZoneSelectionEvent;
import com.camilo.solarsizer.events.WeatherDataProcessedEvent;
import com.camilo.solarsizer.listeners.TimeZoneSelectionListener;
import com.camilo.solarsizer.listeners.WeatherDataProcessedListener;
import com.camilo.solarsizer.model.WeatherData;
import com.camilo.solarsizer.util.ChangeTimeZoneTask;
import com.camilo.solarsizer.util.MatchTimeWeatherDataTask;
import com.camilo.solarsizer.util.ReadWeatherFileTask;
import com.camilo.solarsizer.util.TimeZones;

/**
 * Controller that handles all the progress dialog and updates it
 * 
 * @author Jose Camilo Uzquiano
 *
 */
@SuppressWarnings("deprecation")
public class WeatherProgressDialogController implements TimeZoneSelectionListener {

	@FXML
	private Label progressLabel;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Button cancelButton;
	
	@FXML
	private Button acceptTimeZoneButton;

	private Task<ObservableList<WeatherData>> readerWorker;
	private ChangeTimeZoneTask changeTimeZoneTask;
	private MatchTimeWeatherDataTask matchTimeWeatherDataTask;
	private ObservableList<WeatherData> weatherDataList;
	
	private Stage timeZoneDialogStage, progressDialogStage;

	private File file;

	private WeatherDataProcessedListener weatherDataProcessedListener;

	/**
	 * Contructor of the progress controller
	 */
	public WeatherProgressDialogController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after fxml file has been loaded.
	 */
	@FXML
	private void initialize() throws InterruptedException {
		progressBar.setProgress(0);

		progressBar.progressProperty().unbind();

	}

	/**
	 * set the file from the main application
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * This starts after everything has been set and is called from the main app
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void processData() throws FileNotFoundException, IOException {

		// first determine what kind of file it is, if green button or regular
		String fileType = determineFileType(file);
		Executor executor = Executors.newSingleThreadExecutor();
		switch (fileType) {
		case ("NOAA"):
			readerWorker = new ReadWeatherFileTask();
			((ReadWeatherFileTask) readerWorker).setFile(file);
			progressBar.progressProperty().bind(readerWorker.progressProperty());
			/**
			 * this method is called when the file was correctly read by the program
			 */
			readerWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				public void handle(WorkerStateEvent event) {
					//before continuing we call this method
					loadTimeZoneDialog();
					
					progressBar.progressProperty().unbind();
					try {
						weatherDataList = readerWorker.get();

					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			executor.execute(readerWorker);
			progressLabel.setText(ReadWeatherFileTask.READ_WEATHER_FILE_1_TITLE);
			break;
		case ("error"):
			progressDialogStage.close();
		}

	}
	
	/**
	 * This method handles the loading of the timezone. 
	 */
	@FXML
	public void loadTimeZoneDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			
			loader.setLocation(SolarSizerAppMain.class.getResource("view/TimeZoneDialog.fxml"));
			
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			timeZoneDialogStage = new Stage();
			timeZoneDialogStage.setTitle("Time Zone Selection");
			timeZoneDialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			timeZoneDialogStage.setScene(scene);
			
			TimeZoneDialogController controller = loader.getController();
			controller.setTimeZoneStage(timeZoneDialogStage);
			controller.setTimeZoneSelectionListener(this);
			timeZoneDialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * This method handles all the repeated dates
	 * @param timeZones 
	 * @param temperatureType 
	 */
	private void handleChangeTimeZone(TimeZones timeZones, String temperatureType) {

		Executor executor = Executors.newSingleThreadExecutor();

		try {
			weatherDataList = readerWorker.get();
			changeTimeZoneTask = new ChangeTimeZoneTask(weatherDataList, temperatureType);
			changeTimeZoneTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.progressProperty().unbind();
					
					handleCreateMatchingWeatherData();
					
					
				}
			});
			progressBar.progressProperty().bind(changeTimeZoneTask.progressProperty());
			changeTimeZoneTask.setTimeZone(timeZones.getTimeZoneIndex());
			progressLabel.setText(ChangeTimeZoneTask.CHANGE_TIME_ZONE_TASK_TITLE);
			executor.execute(changeTimeZoneTask);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that handles the missing data
	 */
	private void handleCreateMatchingWeatherData() {
		Executor executor = Executors.newSingleThreadExecutor();

		try {
			weatherDataList = changeTimeZoneTask.get();

			matchTimeWeatherDataTask = new MatchTimeWeatherDataTask(weatherDataList);
			matchTimeWeatherDataTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.progressProperty().unbind();
					try {
						weatherDataList = matchTimeWeatherDataTask.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					fireDataProcessedListener(new WeatherDataProcessedEvent(this, weatherDataList));
					progressDialogStage.close();

				}
			});
			progressBar.progressProperty().bind(matchTimeWeatherDataTask.progressProperty());
			progressLabel.setText(MatchTimeWeatherDataTask.TITLE);
			executor.execute(matchTimeWeatherDataTask);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this is fired after the data has been processed succesfully, 
	 * this is then sent to the root layout controller
	 * @param weatherDataProcessedEvent
	 */
	private void fireDataProcessedListener(WeatherDataProcessedEvent weatherDataProcessedEvent) {
		if (weatherDataProcessedListener != null) {
			weatherDataProcessedListener.processedWeatherDataEmitted(weatherDataProcessedEvent);
		}
	}

	/**
	 * This is a method that returns the type of file that is being read it
	 * could be either green button or ARCX file
	 * 
	 * @param file
	 * @return
	 */
	private String determineFileType(File file) {

		String firstWord = null;
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			String line = bufferedReader.readLine();
			String delims = "[, ]+";
			String[] dataTokens = line.split(delims);
			firstWord = dataTokens[0];

			if (firstWord.equals("Identification")) {
				
				String type = "NOAA";
				return type;
			} else {
				// throws a dialog that shows that the file is not being read because of formatting
				Dialogs.create().title("Unable to read File").masthead("The file couldn't be read.")
						.message("Make sure the file has the right format").showError();
			}
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// if any of these fails return null
		return "error";
	}

	
	/**
	 * Method that sets the weather data processed listenercalled from the rootLayoutCOntroller
	 * from the method to 
	 * @param weatherDataProcessedListener
	 */
	public void setWeatherDataProcessedListener(WeatherDataProcessedListener weatherDataProcessedListener) {
		this.weatherDataProcessedListener = weatherDataProcessedListener;
	}

	
	/**
	 * this method sets the Dialog stage and it is called from the rootLayoutController
	 * @param timeZoneDialogStage
	 */
	public void setTimeZoneDialogStage(Stage timeZoneDialogStage) {
		this.timeZoneDialogStage = timeZoneDialogStage;
	}

	
	/**
	 * This event comes from the timeZoneDialogControlelr
	 * @param event
	 */
	@Override
	public void timeZoneSelectionEmitted(TimeZoneSelectionEvent event) {
		
		handleChangeTimeZone(event.getTimeZone(), event.getTypeTemperatureImport());
		
	}
	
	/**
	 * this method is called from the RootLayoutController, where it was initially created
	 * @param progressDialogStage
	 */
	public void setProgressDialogStage(Stage progressDialogStage) {
		this.progressDialogStage = progressDialogStage;
	}

}
