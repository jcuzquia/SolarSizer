package com.camilo.solarsizer.factories;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleInsets;

import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.DayType;
import com.camilo.solarsizer.model.IntervalData;
import com.camilo.solarsizer.model.MonthlyData;
import com.camilo.solarsizer.model.WeatherData;
import com.camilo.solarsizer.model.XYZConsumptionArrayDataset;
import com.camilo.solarsizer.util.Assistant;
import com.camilo.solarsizer.util.Constants;
import com.camilo.solarsizer.util.TimeNumberFormat;

public class ChartComponentsFactory {

	/**
	 * This static method return an already created time series chart with an
	 * specific theme this is the initial chart
	 * 
	 * @param title
	 * @param xAxisLabel
	 * @param yAxisLabel
	 * @param dataset
	 * @param consumptionRenderer
	 * @param timeDisplay
	 * @return jfreechart
	 */
	public static JFreeChart createTimeSeriesChart(String title, String xAxisLabel, String yAxisLabel,
			XYDataset dataset, XYItemRenderer consumptionRenderer, String timeDisplay) {

		String formatOverride = "15 minutes";

		switch (timeDisplay) {
		case ("15 minutes"):
			formatOverride = "MMM-dd-yy ' ' hh:mm";
			break;
		case ("hourly"):
			formatOverride = "MMM-dd-yy ' ' hh";
			break;
		case ("daily"):
			formatOverride = "MMM-dd-yy";
			break;
		case ("monthly"):
			formatOverride = "MMM-yy";
			break;

		}

		ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());

		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, // title
				xAxisLabel, // x-axis label
				yAxisLabel, // y-axis label
				dataset, // data
				true, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);
		XYPlot plot = (XYPlot) chart.getPlot();

		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
		DateFormat formater = new SimpleDateFormat(formatOverride);
		dateAxis.setDateFormatOverride(formater);

		return chart;
	}

	/**
	 * This method creates a bar chart and is called from the controller
	 * 
	 * @param chartTitle
	 * @param xAxisLabel
	 * @param yAxisLabel
	 * @param monthlyConsumptionDataset
	 * @param consumptionRenderer
	 * @param string
	 * @return
	 */
	public static JFreeChart createBarChart(String chartTitle, String xAxisLabel, String yAxisLabel,
			CategoryDataset monthlyConsumptionDataset, XYLineAndShapeRenderer consumptionRenderer, String string) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		JFreeChart chart = ChartFactory.createBarChart(chartTitle, // chart
																	// title
				"Date", // domain axis label
				"Consumption (kWh)", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false); // URLs?
		return chart;
	}

	/*
	 * This method is in charge to create the daily chart. It is called from the
	 * constructor of the DailyDayTypeChartPanel
	 * 
	 * @param chartTitle
	 * 
	 * @param xAxisLabel
	 * 
	 * @param yAxisLabel
	 * 
	 * @param consumptionCollection
	 * 
	 * @param consumptionRenderer
	 * 
	 * @param string
	 * 
	 * @return
	 */
	public static JFreeChart createDayTypeTimeSeries(String chartTitle, String xAxisLabel, String yAxisLabel,
			TimeSeriesCollection consumptionCollection, String string) {

		ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());

		JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, // title
				xAxisLabel, // x-axis label
				yAxisLabel, // y-axis label
				consumptionCollection, // data
				false, // create legend?
				true, // generate tooltips?
				false // generate URLs?
				);
		XYPlot plot = (XYPlot) chart.getPlot();

		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		return chart;
	}

	/**
	 * This method create the block chart that is used to create the heat maps
	 * 
	 * @param title
	 * @param xAxisLabel
	 * @param yAxisLabel
	 * @param dataset
	 * @param consumptionRenderer
	 * @param timeDisplay
	 * @return
	 */
	public static JFreeChart createBlockChart(String title, String xAxisLabel, String yAxisLabel, XYZDataset dataset,
			XYItemRenderer consumptionRenderer, String timeDisplay) {

		NumberAxis xAxis = new NumberAxis("Date");
		xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		xAxis.setAxisLinePaint(Color.WHITE);
		xAxis.setAxisLinePaint(Color.WHITE);
		xAxis.setLabelPaint(Color.WHITE);
		xAxis.setTickMarkPaint(Color.WHITE);
		xAxis.setTickLabelPaint(Color.WHITE);

		NumberAxis yAxis = new NumberAxis("Time");
		yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		yAxis.setAxisLinePaint(Color.WHITE);
		yAxis.setAxisLinePaint(Color.WHITE);
		yAxis.setLabelPaint(Color.WHITE);
		yAxis.setTickMarkPaint(Color.WHITE);
		yAxis.setTickLabelPaint(Color.WHITE);
		NumberFormat yAxisFormat = new TimeNumberFormat();
		yAxis.setNumberFormatOverride(yAxisFormat);

		XYBlockRenderer renderer = new XYBlockRenderer();
		PaintScale scale = new GrayPaintScale(0.0, 20.0);
		renderer.setPaintScale(scale);
		XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
		plot.setBackgroundPaint(Color.BLACK);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinePaint(Color.WHITE);

		JFreeChart chart = new JFreeChart(title, plot);
		chart.getTitle().setPaint(Color.WHITE);
		plot.getDomainAxis().setAxisLinePaint(Color.WHITE);
		plot.getDomainAxis().setAxisLinePaint(Color.WHITE);
		plot.getDomainAxis().setTickMarkPaint(Color.WHITE);
		plot.getDomainAxis().setLabelPaint(Color.WHITE);
		plot.getDomainAxis().setTickMarkPaint(Color.WHITE);
		plot.getDomainAxis().setTickLabelPaint(Color.WHITE);
		chart.setBackgroundPaint(Color.BLACK);
		chart.removeLegend();

		return chart;
	}

	public static TimeSeries createIntervalSeries(String title, ObservableList<IntervalData> intervalList) {
		TimeSeries series = new TimeSeries(title);
		Thread ts = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < intervalList.size() - 1; i++) {
					double cons = intervalList.get(i).getkW();// getting the
																// demand
					Minute min = new Minute(new Date(intervalList.get(i).getDateValue()));
					try {
						series.add(min, cons);

					} catch (Exception e) {
					}

				}
				series.addOrUpdate(new Minute(new Date(intervalList.get(intervalList.size() - 1).getDateValue())),
						intervalList.get(intervalList.size() - 1).getkW());
			}
		});
		ts.start();

		try {
			ts.join();
			return series;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return series;

	}

	/**
	 * Method that creates the TimeSeries object that will be used in the
	 * interval graph panel this method return a new time series that jfreechart
	 * is able to render the chart
	 * 
	 * @param title
	 * @param weatherDataList
	 * @return timeseries
	 */
	public static TimeSeries createWeatherTimeSeries(String title, ObservableList<WeatherData> weatherDataList) {
		TimeSeries series = new TimeSeries(title);
		for (int i = 0; i < weatherDataList.size() - 1; i++) {
			double cons = weatherDataList.get(i).getTemperature();// getting the
																	// demand
			Minute min = weatherDataList.get(i).getMinute();
			try {
				series.add(min, cons);
			} catch (Exception e) {
			}

		}
		series.addOrUpdate(weatherDataList.get(weatherDataList.size() - 1).getMinute(),
				weatherDataList.get(weatherDataList.size() - 1).getTemperature());
		return series;
	}

	public static XYZDataset createXYZDataset(String type, ObservableList<DailyData> intervalDataList,
			ObservableList<IntervalData> intervalList) {

		XYZDataset dataset = null;

		switch (type) {
		case ("Consumption"):
			dataset = new XYZConsumptionArrayDataset(type, intervalDataList);
			break;
		case ("Generation"):
			dataset = new XYZConsumptionArrayDataset(type, intervalDataList);
			break;
		}

		return dataset;
	}

	/**
	 * This method creates the heat map gradient based on the minimum value,
	 * maximum and the image located in the resources. This is called from the
	 * HeatMapPanel class
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static LookupPaintScale createHeatMapGradient(double min, double max) {
		LookupPaintScale paintScale = new LookupPaintScale(min, max, Color.BLACK);
		BufferedImage img = Constants.HEAT_MAP_IMAGE;
		int[] rgb;
		for (int i = 0; i < img.getWidth(); i++) {
			rgb = Assistant.getPixelData(img, i, 3);
			int r = rgb[0];
			int g = rgb[1];
			int b = rgb[2];
			Color color = new Color(r, g, b);
			double val = max * i / img.getWidth();
			paintScale.add(val, color);

		}

		return paintScale;
	}

	/**
	 * Method that Creates a monthly Category Dataset this is called from the
	 * MonthlyBarChartPanel
	 * 
	 * @param dailyDataList
	 * @return
	 */
	public static DefaultCategoryDataset createMonthlyCategoryDataset(ObservableList<DailyData> dailyDataList) {
		ObservableList<MonthlyData> monthlyConsumption = FXCollections.observableArrayList();

		ObservableList<DailyData> trimmedDailyDataWholeMonth = FXCollections.observableArrayList(dailyDataList);

		// loops that finds the beginning of a whole month
		for (int i = 0; i < trimmedDailyDataWholeMonth.size() - 1; i++) {
			// check if it is the data is first day of the month
			if (trimmedDailyDataWholeMonth.get(i).getDate().getDate() > 1) {
				trimmedDailyDataWholeMonth.remove(0);
				i = 0;
			} else {
				// breaks out of the loop
				trimmedDailyDataWholeMonth.remove(0);
				break;
			}
		}

		// loops that trims to the end of the nearest month
		Calendar cal = Calendar.getInstance();
		Date lastDayDate = trimmedDailyDataWholeMonth.get(trimmedDailyDataWholeMonth.size() - 1).getDate();
		cal.setTime(lastDayDate);
		System.out.println(lastDayDate.toString());
		System.out.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		while (lastDayDate.getDate() != cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			trimmedDailyDataWholeMonth.remove(trimmedDailyDataWholeMonth.size() - 1);
			lastDayDate = trimmedDailyDataWholeMonth.get(trimmedDailyDataWholeMonth.size() - 1).getDate();
			cal.setTime(lastDayDate);
		}

		// retrieve the new monthly consumption that will be used at the
		// barchart
		monthlyConsumption = getMonthylConsumptionFromTrimmedDailyData(trimmedDailyDataWholeMonth);

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (MonthlyData data : monthlyConsumption) {
			DateFormat format = new SimpleDateFormat("MMM-yyyy");
			String key = format.format(data.getDate());
			dataset.addValue(data.getMonthlyKWh(), "Consumption kWh", key);
		}

		return dataset;
	}

	/**
	 * Method that returns a list of values of whole months worth of data
	 * 
	 * @param trimmedDailyDataWholeMonth
	 * @return
	 */
	private static ObservableList<MonthlyData> getMonthylConsumptionFromTrimmedDailyData(
			ObservableList<DailyData> trimmedDailyDataWholeMonth) {
		ObservableList<MonthlyData> monthlyConsumptionList = FXCollections.observableArrayList();
		DailyData firstData = trimmedDailyDataWholeMonth.get(0);
		DailyData secondData = trimmedDailyDataWholeMonth.get(1);
		float monthlykWh = firstData.getTotalDailykWh();

		for (int i = 1; i < trimmedDailyDataWholeMonth.size() - 1; i++) {
			firstData = trimmedDailyDataWholeMonth.get(i - 1);
			secondData = trimmedDailyDataWholeMonth.get(i);
			Date firstDate = firstData.getDate();
			Date secondDate = secondData.getDate();
			if (firstDate.getMonth() == secondDate.getMonth()) {
				monthlykWh = monthlykWh + secondData.getTotalDailykWh();
			} else {
				MonthlyData data = new MonthlyData(firstDate);
				data.setMonthlyKWh(monthlykWh);
				monthlyConsumptionList.add(data);
				monthlykWh = 0;
			}
		}

		// TODO Auto-generated method stub
		return monthlyConsumptionList;
	}

	/**
	 * this method create the collection based on the daytype of the data
	 * 
	 * @param dailyDataList
	 * @return
	 */
	public static ArrayList<TimeSeriesCollection> createDailyCollectionSeries(ObservableList<DailyData> dailyDataList) {

		TimeSeriesCollection schoolDaySeriesCollection = new TimeSeriesCollection();
		TimeSeriesCollection weekendSeriesCollection = new TimeSeriesCollection();
		TimeSeriesCollection holidaySeriesCollection = new TimeSeriesCollection();
		TimeSeriesCollection summerDaySeriesCollection = new TimeSeriesCollection();

		ArrayList<TimeSeriesCollection> dailyDayTypeSeriesCollections = new ArrayList<>();
		
		for (DailyData data : dailyDataList) {
			DayType dayType = data.getDayType();
			ObservableList<IntervalData> intervalData = data.getDailyIntervalList();
			switch (dayType) {
			case SCHOOL_DAY:
				TimeSeries series = createDailyTimeSeries(intervalData);
				schoolDaySeriesCollection.addSeries(series);
				break;
			case SUMMER_DAY:
				TimeSeries summerSeries = createDailyTimeSeries(intervalData);
				summerDaySeriesCollection.addSeries(summerSeries);
				break;
			case HOLIDAY:
				TimeSeries holidaySeries = createDailyTimeSeries(intervalData);
				holidaySeriesCollection.addSeries(holidaySeries);
				break;
			case WEEKEND:
				TimeSeries weekendSeries = createDailyTimeSeries(intervalData);
				weekendSeriesCollection.addSeries(weekendSeries);
				break;
			}
		}
		
		dailyDayTypeSeriesCollections.add(schoolDaySeriesCollection); // index 0
		dailyDayTypeSeriesCollections.add(weekendSeriesCollection); // index 1
		dailyDayTypeSeriesCollections.add(summerDaySeriesCollection); // index 2
		dailyDayTypeSeriesCollections.add(holidaySeriesCollection); // index 3

		return dailyDayTypeSeriesCollections;
	}

	private static TimeSeries createDailyTimeSeries(ObservableList<IntervalData> intervalData) {
		TimeSeries series = new TimeSeries(intervalData.get(0).getDate());
		for (IntervalData data : intervalData) {
			series.addOrUpdate(new Minute(data.getDate().getMinutes(), data.getDate().getHours(), 1, 1, 2000),
					data.getKWh());
		}
		return series;
	}

	/**
	 * From the interval data provided create a new time series
	 * 
	 * @param timeSeries
	 * @param intervalData
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	private static void addIntervalDataToSeries(TimeSeriesCollection timeSeries,
			ObservableList<IntervalData> intervalData) {
		TimeSeries series = new TimeSeries(intervalData.get(0).getDate());
		for (IntervalData data : intervalData) {
			series.addOrUpdate(new Minute(data.getDate().getMinutes(), data.getDate().getHours(), 1, 1, 2000),
					data.getKWh());
		}
		timeSeries.addSeries(series);
	}
}
