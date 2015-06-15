package com.camilo.solarsizer.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javafx.collections.ObservableList;

/**
 * This class takes care of the writing of processed textfiles
 * it takes all of the heat maps in numerical form for everyday
 * this only contains static methods
 * @author Jose Camilo Uzquiano
 *
 */
public class IntervalDataFileWritter {

	/**
	 * this method writes the data as a comma delimited for every day for the whole
	 * range of the interval data that has been processed and trimmed
	 * @param dailyIntervalData
	 */
	public static void writeConsumptionHeatMap(ObservableList<DailyData> dailyIntervalData, File file){
		
		try {

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("DATE");
			bw.write(",");
			// set up the time
			writeTheTime(bw);
			
			for (DailyData dailyList : dailyIntervalData) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String date = sdf.format(dailyList.getDate());
				
				bw.write(date);
				
				bw.write(",");
				
				for (IntervalData data : dailyList.getDailyIntervalList()) {
					bw.write(String.valueOf(data.getKWh()));
					bw.write(",");
				}
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * this method writes the data as a comma delimited for every day for the whole
	 * range of the interval data that has been processed and trimmed
	 * @param dailyIntervalData
	 */
	public static void writeGenerationHeatMap(ObservableList<DailyData> dailyIntervalData, File file){
		
		try {

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("DATE");
			bw.write(",");
			// set up the time
			writeTheTime(bw);
			
			for (DailyData dailyList : dailyIntervalData) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String date = sdf.format(dailyList.getDate());
				
				bw.write(date);
				
				bw.write(",");
				
				for (IntervalData data : dailyList.getDailyIntervalList()) {
					bw.write(String.valueOf(data.getGenkWh()));
					bw.write(",");
				}
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * This method writes a comma delimited file that exports the demand in (kW) as a heat map
	 * @param dailyIntervalData
	 * @param file
	 */
	
	public static void writeDemandHeatMap(ObservableList<DailyData> dailyIntervalData, File file) {
		try {

			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("DATE");
			bw.write(",");
			// set up the time
			writeTheTime(bw);
			
			for (DailyData dailyList : dailyIntervalData) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String date = sdf.format(dailyList.getDate());
				
				bw.write(date);
				
				bw.write(",");
				
				for (IntervalData data : dailyList.getDailyIntervalList()) {
					bw.write(String.valueOf(data.getkW()));
					bw.write(",");
				}
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeWeatherDataMatrix(ObservableList<DailyWeatherData> dailyWeatherData, File file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("DATE");
			bw.write(",");
			// set up the time
			writeTheTime(bw);
			
			for (DailyWeatherData dailyList : dailyWeatherData) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String date = sdf.format(dailyList.getDate());
				
				bw.write(date);
				
				bw.write(",");
				
				for (WeatherData data : dailyList.getDailyWeatherList()) {
					bw.write(String.valueOf(data.getTemperature()));
					bw.write(",");
				}
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	/**
	 * This method handles the export of the interval data
	 * @param intervalDataList
	 * @param file
	 */
	public static void writeIntervalDataList(ObservableList<IntervalData> intervalDataList, File file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
			
			for (IntervalData interval : intervalDataList) {
				String dateValue = String.valueOf(interval.getDateValue()/1000);
				
				String date = interval.getDate().toString();
				
				bw.write(dateValue);
				
				bw.write(",");
				
				bw.write(date);
				
				bw.write(",");
				
				bw.write(String.valueOf(interval.getKWh()));
				
				bw.newLine();
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	
	
	
	/**
	 * This method writes the the time in the file
	 * @param bw
	 * @throws IOException
	 */
	private static void writeTheTime(BufferedWriter bw) throws IOException {
		bw.write("0:00");
		bw.write(",");
		bw.write("0:15");
		bw.write(",");
		bw.write("0:30");
		bw.write(",");
		bw.write("0:45");
		bw.write(",");
		bw.write("1:00");
		bw.write(",");
		bw.write("1:15");
		bw.write(",");
		bw.write("1:30");
		bw.write(",");
		bw.write("1:45");
		bw.write(",");
		bw.write("2:00");
		bw.write(",");
		bw.write("2:15");
		bw.write(",");
		bw.write("2:30");
		bw.write(",");
		bw.write("2:45");
		bw.write(",");
		bw.write("3:00");
		bw.write(",");
		bw.write("3:15");
		bw.write(",");
		bw.write("3:30");
		bw.write(",");
		bw.write("3:45");
		bw.write(",");
		bw.write("4:00");
		bw.write(",");
		bw.write("4:15");
		bw.write(",");
		bw.write("4:30");
		bw.write(",");
		bw.write("4:45");
		bw.write(",");
		bw.write("5:00");
		bw.write(",");
		bw.write("5:15");
		bw.write(",");
		bw.write("5:30");
		bw.write(",");
		bw.write("5:45");
		bw.write(",");
		bw.write("6:00");
		bw.write(",");
		bw.write("6:15");
		bw.write(",");
		bw.write("6:30");
		bw.write(",");
		bw.write("6:45");
		bw.write(",");
		bw.write("7:00");
		bw.write(",");
		bw.write("7:15");
		bw.write(",");
		bw.write("7:30");
		bw.write(",");
		bw.write("7:45");
		bw.write(",");
		bw.write("8:00");
		bw.write(",");
		bw.write("8:15");
		bw.write(",");
		bw.write("8:30");
		bw.write(",");
		bw.write("8:45");
		bw.write(",");
		bw.write("9:00");
		bw.write(",");
		bw.write("9:15");
		bw.write(",");
		bw.write("9:30");
		bw.write(",");
		bw.write("9:45");
		bw.write(",");
		bw.write("10:00");
		bw.write(",");
		bw.write("10:15");
		bw.write(",");
		bw.write("10:30");
		bw.write(",");
		bw.write("10:45");
		bw.write(",");
		bw.write("11:00");
		bw.write(",");
		bw.write("11:15");
		bw.write(",");
		bw.write("11:30");
		bw.write(",");
		bw.write("11:45");
		bw.write(",");
		bw.write("12:00");
		bw.write(",");
		bw.write("12:15");
		bw.write(",");
		bw.write("12:30");
		bw.write(",");
		bw.write("12:45");
		bw.write(",");
		bw.write("13:00");
		bw.write(",");
		bw.write("13:15");
		bw.write(",");
		bw.write("13:30");
		bw.write(",");
		bw.write("13:45");
		bw.write(",");
		bw.write("14:00");
		bw.write(",");
		bw.write("14:15");
		bw.write(",");
		bw.write("14:30");
		bw.write(",");
		bw.write("14:45");
		bw.write(",");
		bw.write("15:00");
		bw.write(",");
		bw.write("15:15");
		bw.write(",");
		bw.write("15:30");
		bw.write(",");
		bw.write("15:45");
		bw.write(",");
		bw.write("16:00");
		bw.write(",");
		bw.write("16:15");
		bw.write(",");
		bw.write("16:30");
		bw.write(",");
		bw.write("16:45");
		bw.write(",");
		bw.write("17:00");
		bw.write(",");
		bw.write("17:15");
		bw.write(",");
		bw.write("17:30");
		bw.write(",");
		bw.write("17:45");
		bw.write(",");
		bw.write("18:00");
		bw.write(",");
		bw.write("18:15");
		bw.write(",");
		bw.write("18:30");
		bw.write(",");
		bw.write("18:45");
		bw.write(",");
		bw.write("19:00");
		bw.write(",");
		bw.write("19:15");
		bw.write(",");
		bw.write("19:30");
		bw.write(",");
		bw.write("19:45");
		bw.write(",");
		bw.write("20:00");
		bw.write(",");
		bw.write("20:15");
		bw.write(",");
		bw.write("20:30");
		bw.write(",");
		bw.write("20:45");
		bw.write(",");
		bw.write("21:00");
		bw.write(",");
		bw.write("21:15");
		bw.write(",");
		bw.write("21:30");
		bw.write(",");
		bw.write("21:45");
		bw.write(",");
		bw.write("22:00");
		bw.write(",");
		bw.write("22:15");
		bw.write(",");
		bw.write("22:30");
		bw.write(",");
		bw.write("22:45");
		bw.write(",");
		bw.write("23:00");
		bw.write(",");
		bw.write("23:15");
		bw.write(",");
		bw.write("23:30");
		bw.write(",");
		bw.write("23:45");
		bw.write(",");
		bw.newLine();
	}
	

	
}
