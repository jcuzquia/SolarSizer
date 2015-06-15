package com.camilo.solarsizer.view;

import java.io.File;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.camilo.solarsizer.SolarSizerAppMain;
import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.IntervalDataFileWritter;

public class ExportDialogController {

	private final ToggleGroup group = new ToggleGroup();

	@FXML
	private RadioButton kWhRB;

	@FXML
	private RadioButton kWRB;
	
	@FXML
	private Button exportBtn;
	
	@FXML
	private Button cancelBtn;
	
	private String typeExport;

	private Stage dialogStage;

	public ExportDialogController() {

	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after fxml file has been loaded.
	 */
	@FXML
	public void initialize() {
		//set up the toggle group
		kWhRB.setToggleGroup(group);
		kWRB.setToggleGroup(group);
		//set up the user data
		kWhRB.setUserData("kWh");
		kWRB.setUserData("kW");
		
		
		kWRB.setSelected(true);//always set the kW button selected
		typeExport = "kW";

	}
	
	/**
	 * this method handles the change on the radio buttons
	 * it sets the right type of export either in kW or kWh
	 */
	@FXML
	public void handleRBChange(){
		if (group.getSelectedToggle()!=null){
			typeExport = group.getSelectedToggle().getUserData().toString();
		}
	}

	/**
	 * Handles the fileChooser and the export of the interval Data
	 */
	@FXML
	public void handleExport() {
		// first we get the controller reference to get the daily list
		EnergyOverviewController controller = SolarSizerAppMain.getController();

		ObservableList<DailyData> dailyIntervalData = controller.getDailyIntervalData();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export the heat map as a comma delimited text file");

		// set the extension filter
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		// show open file dialog
		File file = fileChooser.showSaveDialog(SolarSizerAppMain.getPrimaryStage());

		if (file != null) {
			//hide the stage if some file is selected
			dialogStage.close();
			switch(typeExport){
			case ("kW"):
				IntervalDataFileWritter.writeDemandHeatMap(dailyIntervalData, file);
			break;
			case ("kWh"):
				IntervalDataFileWritter.writeConsumptionHeatMap(dailyIntervalData, file);
			break;
			}
		}
		
	}
	
	/**
	 * Method that handles the export cancelled, simply exits the stage
	 */
	@FXML
	public void handleCancelExport(){
		dialogStage.close();
	}

	/**
	 * this method is called from the root layout controller in order to be able to close the stage
	 * from this controller
	 * @param dialogStage
	 */
	public void setStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
}
