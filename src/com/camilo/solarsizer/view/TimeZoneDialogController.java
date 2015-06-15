package com.camilo.solarsizer.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import com.camilo.solarsizer.events.TimeZoneSelectionEvent;
import com.camilo.solarsizer.listeners.TimeZoneSelectionListener;
import com.camilo.solarsizer.util.TimeZones;

/**
 * This class is the controller that handles the change in TimeZone
 * @author Jose Camilo Uzquiano 
 *
 */
public class TimeZoneDialogController {

	private final ToggleGroup group = new ToggleGroup();
	
	@FXML
	private ComboBox<TimeZones> timeZoneComboBox;
	
	@FXML
	private Button acceptButton, cancelButton;
	
	@FXML
	private RadioButton farenheitRB;
	
	@FXML
	private RadioButton celciusRB;
	
	private Stage timeZoneStage;
	
	private TimeZoneSelectionListener timeZoneSelectionListener;

	private String typeTemperatureImport;
	
	/**
	 * regular constructor that is called from the FXML file
	 */
	public TimeZoneDialogController() {
	}
	
	@FXML
	public void initialize(){
		timeZoneComboBox.getItems().setAll(TimeZones.values());
		timeZoneComboBox.getSelectionModel().select(3);
		
		//set up the toggle group for the radio buttons
		farenheitRB.setToggleGroup(group);
		celciusRB.setToggleGroup(group);
		//set up the user data
		farenheitRB.setUserData("Farenheit");
		celciusRB.setUserData("Celcius");
		
		farenheitRB.setSelected(true);//always the farenheit button is selected
		typeTemperatureImport = "Farenheit";
		
		
	}
	
	/**
	 * this method handles the change on the radio buttons
	 * it sets the right type of export either in F or C
	 */
	@FXML
	public void handleRBChange(){
		if (group.getSelectedToggle()!=null){
			typeTemperatureImport = group.getSelectedToggle().getUserData().toString();
		}
	}
	
	/**
	 * 
	 */
	public void handlesTimeZoneSelection(){
		TimeZones z = timeZoneComboBox.getSelectionModel().getSelectedItem();
		
		
		TimeZoneSelectionEvent event = new TimeZoneSelectionEvent(this,z,typeTemperatureImport );
		timeZoneStage.close();
		
		fireTimeZoneSelectionListener(event);
	}
	
	private void fireTimeZoneSelectionListener(TimeZoneSelectionEvent event) {
		if(timeZoneSelectionListener!=null){
			timeZoneSelectionListener.timeZoneSelectionEmitted(event);
		}
	}


	

	
	public void setTimeZoneStage(Stage timeZoneStage) {
		this.timeZoneStage = timeZoneStage;
	}


	/**
	 * this method is called from the WeatherDialogController at the moment when the 
	 * TimeZone dialog is shown
	 * @param timeZoneSelectionListener
	 */
	public void setTimeZoneSelectionListener(TimeZoneSelectionListener timeZoneSelectionListener) {
		this.timeZoneSelectionListener = timeZoneSelectionListener;
	}
	
	/**
	 * Method that simply handles
	 */
	public void handlesCancelButton(){
		timeZoneStage.close();
		//TODO: cancel the import
	}
	
	
}
