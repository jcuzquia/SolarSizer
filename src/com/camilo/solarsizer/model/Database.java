package com.camilo.solarsizer.model;

import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Database {

	private ObservableList<Meter> meters;
	private int index = 0;
	private static Database instance = new Database();

	private Database() {
		meters = FXCollections.observableArrayList();
	}

	public ObservableList<Meter> getMeters() {
		return meters;
	}

	public void setMeters(ObservableList<Meter> meters) {
		this.meters = meters;
	}
	
	public void addMeter(Meter meter){
		meters.add(meter);
	}
	
	public void deleteMeter(String id){
		//TODO: add code to delete selected meters
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public LinkedList<Meter> getActiveMeters() {
		
		LinkedList<Meter> activeMeters = new LinkedList<Meter>();
		for (Meter meter : meters){
		
			if(meter.isActivated()){
				activeMeters.add(meter);
			}
		}
		
		return activeMeters;
	}

	/**
	 * Get the static instance, there is only one Database
	 * singleton pattern is applied
	 * @return
	 */
	public static Database getInstance() {
		return instance;
	}
}
