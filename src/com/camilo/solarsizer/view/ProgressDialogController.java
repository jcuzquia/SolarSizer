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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import com.camilo.solarsizer.events.DataProcessedEvent;
import com.camilo.solarsizer.listeners.DataProcessedListener;
import com.camilo.solarsizer.model.IntervalData;
import com.camilo.solarsizer.util.DeleteRepeatedDatesTask;
import com.camilo.solarsizer.util.FillUpMissingDataTask;
import com.camilo.solarsizer.util.ReadFile1Task;
import com.camilo.solarsizer.util.ReadFile2Task;
import com.camilo.solarsizer.util.ReadFile3Task;
import com.camilo.solarsizer.util.ReadSolarDataTask1;
import com.camilo.solarsizer.util.TrimForNearestDayTask;

/**
 * Controller that handles all the progress dialog and updates it
 * 
 * @author Jose Camilo Uzquiano
 *
 */
public class ProgressDialogController {

	@FXML
	private Label progressLabel;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Button cancelButton;

	private Task<ObservableList<IntervalData>> readerWorker;
	private DeleteRepeatedDatesTask deleteRepeatedDatesTask;
	private FillUpMissingDataTask fillUpMissingDataTask;
	private TrimForNearestDayTask trimForNearestDayTask;
	private ObservableList<IntervalData> intervalDataList;

	private File file;
	// this loop is used to loop once in order to delete the repeated date
	// and filter again the data to account for the daylight savings

	private DataProcessedListener dataProcessedListener;

	/**
	 * Contructor of the progress controller
	 */
	public ProgressDialogController() {
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
		case ("green button"):
			readerWorker = new ReadFile1Task();
			((ReadFile1Task) readerWorker).setFile(file);
			progressBar.progressProperty().bind(readerWorker.progressProperty());
			readerWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.progressProperty().unbind();
					handleDeleteRepeatedDates();
				}
			});
			executor.execute(readerWorker);
			progressLabel.setText(ReadFile1Task.READ_FILE_1_TITLE);
			break;
		case ("normal"):
			readerWorker = new ReadFile2Task();
			((ReadFile2Task) readerWorker).setFile(file);
			progressBar.progressProperty().bind(readerWorker.progressProperty());
			readerWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.progressProperty().unbind();
					handleDeleteRepeatedDates();
				}
			});
			executor.execute(readerWorker);
			progressLabel.setText(ReadFile2Task.READ_FILE_2_TITLE);
			break;
		case ("green button 2"):
			readerWorker = new ReadFile3Task();
			((ReadFile3Task) readerWorker).setFile(file);
			progressBar.progressProperty().bind(readerWorker.progressProperty());
			readerWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.progressProperty().unbind();
					handleDeleteRepeatedDates();
				}
			});
			executor.execute(readerWorker);
			progressLabel.setText(ReadFile3Task.READ_FILE_3_TITLE);
			break;
		case ("Solar Data"):
			readerWorker = new ReadSolarDataTask1();
		((ReadSolarDataTask1) readerWorker).setFile(file);
		progressBar.progressProperty().bind(readerWorker.progressProperty());
		readerWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				progressBar.progressProperty().unbind();
				handleDeleteRepeatedDates();
			}
		});
		executor.execute(readerWorker);
		progressLabel.setText(ReadSolarDataTask1.READ_FILE_1_TITLE);
		break;
			
		}

	}

	/**
	 * This method handles all the repeated dates
	 */
	private void handleDeleteRepeatedDates() {

		Executor executor = Executors.newSingleThreadExecutor();

		try {
			intervalDataList = readerWorker.get();
			deleteRepeatedDatesTask = new DeleteRepeatedDatesTask(intervalDataList);
			deleteRepeatedDatesTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.progressProperty().unbind();
					handleMissingData();

				}
			});
			progressBar.progressProperty().bind(deleteRepeatedDatesTask.progressProperty());
			progressLabel.setText(DeleteRepeatedDatesTask.DELETE_REPEATED_TASK_TITLE);
			executor.execute(deleteRepeatedDatesTask);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that handles the missing data
	 */
	private void handleMissingData() {
		Executor executor = Executors.newSingleThreadExecutor();

		try {
			intervalDataList = deleteRepeatedDatesTask.get();
			fillUpMissingDataTask = new FillUpMissingDataTask(intervalDataList);
			fillUpMissingDataTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.progressProperty().unbind();
					handleTrimForNearestDay();

				}
			});
			progressBar.progressProperty().bind(fillUpMissingDataTask.progressProperty());
			progressLabel.setText(FillUpMissingDataTask.TITLE);
			executor.execute(fillUpMissingDataTask);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is a method that handles the trimming to the nearest day, it is very
	 * fast
	 */
	private void handleTrimForNearestDay() {
		Executor executor = Executors.newSingleThreadExecutor();

		try {
			intervalDataList = fillUpMissingDataTask.get();
			trimForNearestDayTask = new TrimForNearestDayTask(intervalDataList);

			// what happens when it finishes doing all the processing
			trimForNearestDayTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					progressBar.setVisible(false);
					try {
						intervalDataList = trimForNearestDayTask.get();

					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}

					fireDataProcessedListener(new DataProcessedEvent(this, intervalDataList));

				}
			});
			progressBar.progressProperty().bind(trimForNearestDayTask.progressProperty());
			progressLabel.setText(TrimForNearestDayTask.TITLE);
			executor.execute(trimForNearestDayTask);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void fireDataProcessedListener(DataProcessedEvent dataProcessedEvent) {
		if (dataProcessedListener != null) {
			dataProcessedListener.processedIntervalDataEmitted(dataProcessedEvent);
		}
	}

	/**
	 * This method is called from the main controller when the progress dialog
	 * is called after the interval data file has been submitted
	 * 
	 * @param dataProcessedListener
	 */
	public void setDataProcessedListener(DataProcessedListener dataProcessedListener) {
		this.dataProcessedListener = dataProcessedListener;
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
			String delims = "[	]+";
			String[] dataTokens = line.split(delims);
			firstWord = dataTokens[0];

			if (firstWord.equals("Name")) {
				String type = "green button";
				return type;
			} else if (firstWord.equals("SERVICE_POINT_ID") || dataTokens.length == 8) {
				String type = "normal";
				return type;
			} else if (firstWord.equals("SPID")) {
				String type = "green button 2";
				return type;
			} else if (firstWord.equals("Local time: Pacific Time (US & Canada)")){
				String type = "Solar Data";
				return type;
			}
			bufferedReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// if any of these fails return null
		return null;
	}

}
