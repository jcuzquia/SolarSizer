package com.camilo.solarsizer.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import com.camilo.solarsizer.model.IntervalData;

public class DeleteRepeatedDatesTask extends Task<ObservableList<IntervalData>> {

	private ObservableList<IntervalData> intervalList;
	public static final String DELETE_REPEATED_TASK_TITLE = "Deleting Repeated Dates";
	
	public DeleteRepeatedDatesTask(ObservableList<IntervalData> intervalList) {
		this.intervalList = intervalList;
	}

	@Override
	protected ObservableList<IntervalData> call() throws Exception {

		double workDone = 0;
		double max = intervalList.size();
		updateProgress(workDone, max);
		ObservableList<IntervalData> deleteDataList = FXCollections.observableArrayList(intervalList);
		ObservableList<IntervalData> toAddList = FXCollections.observableArrayList();
		
		toAddList.add(deleteDataList.get(0));//set up so that one is before
		deleteDataList.remove(0);			//remove

		
		while (deleteDataList.size() > 1) {
			workDone = toAddList.size();
			max = deleteDataList.size();
			updateProgress(workDone, max);
			IntervalData data1 = toAddList.get(toAddList.size()-1);//get the last of the list
			IntervalData data2 = deleteDataList.get(0);//get the first of the list
			if (data1.getDateValue() == data2.getDateValue()) {// if they are the same time
				
				if (data1.getKWh() == data2.getKWh() || data1.getGenkWh() == data2.getGenkWh()){
					float con1 = data1.getKWh();
					float gen1 = data1.getGenkWh();
					
					float con2 = data2.getKWh();
					float gen2 = data2.getGenkWh();
					if(con1 != con2 || gen1 != gen2){
						data1.setGenkWh(gen1+gen2);
						data1.setKWh(con1+con2);
					}
					
				} 
				deleteDataList.remove(0);
				
					
				
			}else {
				toAddList.add(data2);
				deleteDataList.remove(0);
			}
		}

		intervalList = toAddList;

		return intervalList;
	}
	

}
