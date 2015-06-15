package com.camilo.solarsizer.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

import com.camilo.solarsizer.SolarSizerAppMain;
import com.camilo.solarsizer.model.Database;
import com.camilo.solarsizer.model.Meter;

public class CheckBoxCell extends TableCell<Meter, Boolean>{
	private CheckBox cellCheckBox = new CheckBox();

    public CheckBoxCell() {
    	cellCheckBox.setSelected(true);
    	cellCheckBox.centerShapeProperty();
    	setAlignment(Pos.CENTER);
        cellCheckBox.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                int selectedIndex = getTableRow().getIndex();
                Database database = SolarSizerAppMain.getController().getDatabase();
                ObservableList<Meter> meters = database.getMeters();
                
                meters.get(selectedIndex).setActivated(cellCheckBox.isSelected());
                SolarSizerAppMain.getController().activateSeries(selectedIndex,meters.get(selectedIndex));

            }
        });
    }

    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        setGraphic(null);
        if(!empty){
        	setGraphic(cellCheckBox);
        }
        
    }
}

