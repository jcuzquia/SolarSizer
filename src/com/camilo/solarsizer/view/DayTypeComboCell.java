package com.camilo.solarsizer.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.DayType;

/**
 * This DayType Combocell creates a cell with a combobox to select the daytype
 * of the data
 * 
 * @author Jose Camilo Uzquiano
 *
 */
public class DayTypeComboCell extends TableCell<DailyData, String> {
	private final ComboBox<DayType> cellComboBox = new ComboBox<DayType>();
	// this is called
	private static int dayIndex = 0;
	private DailyData dailyData;
	private ObservableList<DailyData> dailyDataList;

	public DayTypeComboCell(TableView<DailyData> dateTable) {
		dailyDataList = FXCollections.observableArrayList(dateTable.getItems());
//		System.out.println("This is the index: " + dayIndex);
		dailyData = dailyDataList.get(dayIndex);
		System.out.println(dailyData.getDayType() + "... date: " + dailyData.getDate().toString());
		cellComboBox.centerShapeProperty();
		setAlignment(Pos.CENTER);
		cellComboBox.getItems().addAll(DayType.values());
		cellComboBox.getSelectionModel().select(dailyData.getDayType());
		cellComboBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

			}
		});
		// if the index is greater or equal to the size of the list, then set it
		// to zero
	}

	@Override
	protected void updateItem(String dayType, boolean empty) {
		super.updateItem(dayType, empty);
		
		if (empty){
			setGraphic(null);
		} else {
			
			setGraphic(cellComboBox);
			dayIndex++;
		}

//		if (!empty || dayType != null) {
//			
//			System.out.println("This is the daytype: " + dailyData.getDayType().toString());
//			
//		}
	}
	
//	public static int getDayIndex() {
//		return dayIndex;
//	}
//
//	public static void setDayIndex(int dayIndex) {
//		DayTypeComboCell.dayIndex = dayIndex;
//	}

}
