package com.camilo.solarsizer.events;

import java.util.EventObject;

import javafx.collections.ObservableList;

import com.camilo.solarsizer.model.IntervalData;

/**
 * This event stores the interval list momentarily and is passed to the main controller
 * @author Camilo Uzquiano
 *
 */
public class DataProcessedEvent extends EventObject{

	public DataProcessedEvent(Object source, ObservableList<IntervalData> intervalList) {
		super(source);
		this.intervalList = intervalList;
	}

	private static final long serialVersionUID = 336256056563598949L;
	private ObservableList<IntervalData> intervalList;
	
	
	public ObservableList<IntervalData> getIntervalList() {
		return intervalList;
	}
	
	

}
