package com.camilo.solarsizer.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import com.camilo.solarsizer.SolarSizerAppMain;
import com.camilo.solarsizer.model.Database;
import com.camilo.solarsizer.model.Meter;

public class ColorButtonCell extends TableCell<Meter, Color> {
	private ColorPicker cp;

	public ColorButtonCell(java.awt.Color color) {
		cp = new ColorPicker();
		Color c = Color.rgb(color.getRed(), color.getGreen(), color.getBlue());

		cp.setValue(c);
		cp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				commitEdit(cp.getValue());
				updateItem(cp.getValue(), isEmpty());
				int selectedIndex = getTableRow().getIndex();
                Database database = SolarSizerAppMain.getController().getDatabase();
                ObservableList<Meter> meters = database.getMeters();
                //getting the awt Color
                java.awt.Color mc = new java.awt.Color((int)Math.round(cp.getValue().getRed() *255),(int)Math.round(cp.getValue().getGreen() *255),(int) Math.round(cp.getValue().getBlue()*255));
                
                meters.get(selectedIndex).setColor(mc);
                SolarSizerAppMain.getController().changeSeriesColor(selectedIndex, mc);
			}
		});
	}

	// Display button if the row is not empty
	@Override
	protected void updateItem(Color color, boolean empty) {
		super.updateItem(color,empty);
		setGraphic(null);
		if (!empty) {
			setGraphic(cp);
		}
	}
	
	
}