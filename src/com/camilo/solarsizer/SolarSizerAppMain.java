/*================================================================================
 * SolarSizer : a tool that processes and displays interval data and weather data
 *================================================================================
 * (C) Copyright 2013-2014, by Camilo Uzquiano
 * 
 * This Program is able to process interval data
 * 
 * 
 * The main method to be executed
 * 
 * Original Author: Jose Camilo Uzquiano
 *
 * 
 */

package com.camilo.solarsizer;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.camilo.solarsizer.view.EnergyOverviewController;

/**
 * The Main Class that is used to run the program. This program is based on
 * javafx and jfreechart. It deploys linecharts, and heat maps for millions of
 * data points.
 * 
 * @author Jose Camilo Uzquiano Sandy
 *
 */
public class SolarSizerAppMain extends Application {

	private static Stage primaryStage;
	private BorderPane rootLayout;
	private static EnergyOverviewController controller;

	@Override
	public void start(Stage primaryStage) {
		SolarSizerAppMain.primaryStage = primaryStage;
		SolarSizerAppMain.primaryStage.setTitle("PV Sizer App");

		printHeapSize();

		initRootLayout();

		showSolarSizerOverview();
	}

	/**
	 * This method simply prints the heap size of the system, used to check the
	 * memory necessary to run the application
	 */
	private void printHeapSize() {
		// get current size of heap in bytes
		long heapSize = Runtime.getRuntime().totalMemory();
		double size = heapSize / 1000000;

		// get maximum size of heap in bytes. The heap cannot grow beyond this
		// size.
		// the attempt with throw the out of memory exception
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		double maxSize = heapMaxSize / 1000000;

		// get the amount of free memory within the heap in bytes. This size
		// will increase
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		double freeSize = heapFreeSize / 1000000;

		System.out.println("==============================================");
		System.out.println("The heap size is: " + size);
		System.out.println("The heap max size is: " + maxSize);
		System.out.println("The heap free size is: " + freeSize);

//		ObservableList<TMY3DataDirectory> tmy3DataDirectories = Assistant.getTMY3ListFromNREL();
//		
//		ReadTMY3DataTask test = new ReadTMY3DataTask(tmy3DataDirectories.get(0).getUrl());
//		test.run();
	}

	/**
	 * Initializes the root Layout
	 */
	private void initRootLayout() {
		try {
			// load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SolarSizerAppMain.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// show the scene containinf the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMaximized(true);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * show the solar sizer overview inside the root layout
	 */
	private void showSolarSizerOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SolarSizerAppMain.class.getResource("view/EnergyOverview.fxml"));
			AnchorPane sizerOverview = (AnchorPane) loader.load();

			// set the energy overview into the center of root layout.
			rootLayout.setCenter(sizerOverview);

			// give the controller access to the main app.
			controller = loader.getController();
			controller.showChartPanel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage
	 * @return
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static EnergyOverviewController getController() {
		return controller;
	}
}
