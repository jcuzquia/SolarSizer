package com.camilo.solarsizer.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.camilo.solarsizer.SolarSizerAppMain;
import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.DailyWeatherData;
import com.camilo.solarsizer.model.IntervalData;
import com.camilo.solarsizer.model.IntervalDataFileWritter;
import com.camilo.solarsizer.model.TMY3DataDirectory;
import com.camilo.solarsizer.util.Assistant;
import com.camilo.solarsizer.util.Constants;
import com.camilo.solarsizer.util.ReadTMY3DataTask;

/**
 * This is the controller for the root layout. This provides the basic
 * application layout containing the menu bar and space where other JavaFX
 * elements can be places
 * 
 * @author Jose Camilo Uzquiano
 *
 */
@SuppressWarnings("deprecation")
public class RootLayoutController {

	@FXML
	private MenuItem heatMapConsumptionExportItem;

	@FXML
	private MenuItem heatMapCurtailmentExportItem;
	
	@FXML
	private Menu tmy3DataSelector;
	
	private Stage progressDialogStage;
	
	/**
	 * handles the beginning of the export dialog
	 */
	@FXML
	public void handleHeatMapConsumptionExport() {

		handleExportDialog();

	}
	
	/**
	 * This method is called when initializing the controller
	 */
	public void initialize(){
		Thread loadNRELThread = new Thread(new Runnable() {
			public void run() {
				initializeTMY3Menu();
			}
		});
		
		loadNRELThread.start();
	}
	
	/**
	 * This method is called when the controller is initialized as a separate thread.
	 * 
	 */
	public void initializeTMY3Menu(){
		//populate the state menu
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (String key: Constants.STATE_MAP.keySet()){
			Menu stateMenu = new Menu(key);
			
			Thread loadThread = new Thread(new Runnable() {
				public void run() {
					ObservableList<TMY3DataDirectory> directories = null;
					try{
						directories = Assistant.getTMY3ListFromNREL(key);
						//TODO: This is the print out of the size of each of the directories
						//System.out.println("Size directory " + Constants.STATE_MAP.get(key) + " is: " + directories.size());
						for(TMY3DataDirectory dir: directories){
							MenuItem locationItem = new MenuItem(dir.getLocation());
							stateMenu.getItems().add(locationItem);
							
							//executes the reader when the item is clicked
							locationItem.setOnAction(new EventHandler<ActionEvent>() {

								public void handle(ActionEvent event) {
									ReadTMY3DataTask tmy3Reader = new ReadTMY3DataTask(dir.getUrl());
									tmy3Reader.run();
								}
							});
						}
						tmy3DataSelector.getItems().add(stateMenu);
					}catch (Exception e){
						Dialogs.create().title("Page Not Found").masthead("We couldn't retrieve the information from the NREL website")
						.message("Please make sure you don't have any internet conection problems").showError();
					}
				}
			});
			threads.add(loadThread);
			
		}
		
		//these are the threads that start and wait to finish, they do all the work in populating the menu
		for (Thread t : threads){
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void handleWeatherFileImport(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a comma delimited Text File with NOAA formatting");

		// set the extension filter
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		// show open file dialog
		File file = fileChooser.showOpenDialog(SolarSizerAppMain.getPrimaryStage());

		if (file != null) {
			showWeatherProgressDialogController(file);
		}
	}
	
	/**
	 * This method calls the layout, loads the FXML file and finally creates the controller
	 * to read the weather file
	 * @param file
	 */
	private void showWeatherProgressDialogController(File file) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SolarSizerAppMain.class.getResource("view/WeatherImportDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			progressDialogStage = new Stage();
			progressDialogStage.setTitle("Importing Weather File...");
			progressDialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			progressDialogStage.setScene(scene);

			WeatherProgressDialogController controller = loader.getController();
			controller.setProgressDialogStage(progressDialogStage);
			controller.setFile(file);
			//we set the listener to the main controller, this way only the main controller is
			//able to listen the event after the data has been processed
			controller.setWeatherDataProcessedListener(SolarSizerAppMain.getController());
			// Show the dialog and wait until the user closes it
			progressDialogStage.show();
			// calls to process data
			controller.processData();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * handles the export and sends the file address where the file is to be exported
	 */
	public void handleHeatMapCurtailmentExport() {

		// first we get the controller reference to get the daily list
		EnergyOverviewController controller = SolarSizerAppMain.getController();

		ObservableList<DailyData> dailyIntervalData = controller.getDailyIntervalData();
		if (dailyIntervalData == null) {
			// no data has been able to process
			Dialogs.create().title("No data has been processed yet").masthead("No data is available to write the file")
					.message("Please import an appropriate interval data").showError();
			return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export the heat map as a comma delimited text file");

		// set the extension filter
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		// show open file dialog
		File file = fileChooser.showSaveDialog(SolarSizerAppMain.getPrimaryStage());

		if (file != null) {
			IntervalDataFileWritter.writeGenerationHeatMap(dailyIntervalData, file);
		}
	}
	
	/**
	 * This method puts together the Dialog and shows it in the screen
	 * It creates the export dialog controller where it handles the export 
	 * of heat maps
	 */
	public void handleExportDialog(){
		try{
			//Load the fxml file and create a new stage for the popup
			FXMLLoader loader = new FXMLLoader(SolarSizerAppMain.class.getResource("view/ExportDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Export Chooser");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(SolarSizerAppMain.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			//set the controller
			ExportDialogController controller = loader.getController();
			controller.setStage(dialogStage);
			
			dialogStage.showAndWait();
			
			
		}catch(IOException e){
			e.printStackTrace();
			
		}
	}
	
	/**
	 * This method handles the export of the weather data in a 96x96 matrix
	 */
	public void handleHeatMapWeatherDataExport(){
		// first we get the controller reference to get the daily list
		EnergyOverviewController controller = SolarSizerAppMain.getController();

		ObservableList<DailyWeatherData> dailyWeatherData = controller.getDailyWeatherData();
		if (dailyWeatherData == null) {
			// no data has been able to process
			Dialogs.create().title("No data has been processed yet").masthead("No data is available to write the file")
					.message("Please import an appropriate weather data").showError();
			return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export the heat map as a comma delimited text file");

		// set the extension filter
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		// show open file dialog
		File file = fileChooser.showSaveDialog(SolarSizerAppMain.getPrimaryStage());

		if (file != null) {
			IntervalDataFileWritter.writeWeatherDataMatrix(dailyWeatherData, file);
		}
	}
	
	/**
	 * This method handles the export of the weather data in a 96x96 matrix
	 */
	public void handleIntervalDataNormalExport(){
		// first we get the controller reference to get the daily list
		EnergyOverviewController controller = SolarSizerAppMain.getController();

		ObservableList<IntervalData> IntervalDataList = controller.getIntervalDataList();
		if (IntervalDataList == null) {
			// no data has been able to process
			Dialogs.create().title("No data has been processed yet").masthead("No data is available to write the file")
					.message("Please import an appropriate weather data").showError();
			return;
		}

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export the Interval Data List as a comma delimited text file");

		// set the extension filter
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		// show open file dialog
		File file = fileChooser.showSaveDialog(SolarSizerAppMain.getPrimaryStage());

		if (file != null) {
			IntervalDataFileWritter.writeIntervalDataList(IntervalDataList, file);
		}
	}
	
}
